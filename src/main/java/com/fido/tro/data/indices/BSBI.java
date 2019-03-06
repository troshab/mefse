package com.fido.tro.data.indices;

import com.fido.tro.data.Entity;
import com.fido.tro.data.fields.Filepath;
import com.fido.tro.serializers.Serializer;

import java.lang.ref.WeakReference;
import java.util.*;

public class BSBI extends Inverted {
    private final String BLOCKS_PATH = "d:\\blocks_spimi\\";
    private final Serializer serializer;

    private int blocksSize;
    private int blockSizeCompleted = 0;
    private int blockNumber = 0;

    private Map<String, Filepath> block = new HashMap<>();

    public BSBI(int blocksSize, Serializer serializer){
        this.blocksSize = blocksSize - 1;
        this.serializer = serializer;
    }

    void saveBlock() {
        serializer.getSerializer().save(BLOCKS_PATH + "block_" + blockNumber + ".bin", block, false);
        blockNumber++;
        blockSizeCompleted = 0;
        block.clear();
        System.gc();
    }

    public void allAdded() {
        saveBlock();
    }

    public void add(Entity entity, int fileCounter, String filePath, Long position) {
        String word = entity.getTerm();

        if (!block.containsKey(word))
            block.put(word, new Filepath());

        block.get(word).add(filePath);

        if (blockSizeCompleted++ == blocksSize) {
            saveBlock();
        }
    }

    protected Filepath findSetForWord(String word, boolean invertArray) {
        Filepath queryPartSet = block.get(word);
        boolean finded = true;
        if (Objects.isNull(queryPartSet)) {
            finded = false;
            int blockNumberCurrent = blockNumber - 1;
            HashMap<String, Filepath> fileBlock = new HashMap<>();
            Set<String> findedInFiles = new HashSet<>();
            while(blockNumberCurrent-- > 0) {
                fileBlock = (HashMap<String, Filepath>) serializer.getSerializer().load(BLOCKS_PATH + "block_" + blockNumberCurrent + ".bin", fileBlock, false);
                Filepath fileQueryPartSet = fileBlock.get(word);
                if (Objects.nonNull(fileQueryPartSet)) {
                    finded = true;
                    findedInFiles.addAll(fileQueryPartSet.allFilepaths());
                }
            }
            if (finded) {
                queryPartSet = new Filepath();
                queryPartSet.addAll(findedInFiles);
            }
        }
        if (!finded) {
            System.err.println("Error: word '" + word + "' didn't find in inverted index");
            return null;
        }

        if (invertArray) {
            Filepath queryAllSet = getAllFilepath();
            queryAllSet.removeAll(queryPartSet);
            queryPartSet = queryAllSet;
        }
        return queryPartSet;
    }
}