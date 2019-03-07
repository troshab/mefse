package com.fido.tro.data.indices;

import com.fido.tro.data.Record;
import com.fido.tro.data.indices.entities.Filepath;
import com.fido.tro.serializers.Serializer;

import java.util.*;

public class SPIMI extends Inverted {
    private final String BLOCKS_PATH = "d:\\blocks_spimi\\";
    private final Serializer serializer;

    private long freeMemoryTotal = Runtime.getRuntime().maxMemory();
    private long blockMemoryLimit = Math.round(freeMemoryTotal * 0.8);
    private long usedMemoryTotal = 0;

    String arch = System.getenv("PROCESSOR_ARCHITECTURE");
    String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

    private int processArchitectureLinkSize = arch != null && arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64")  ? 8 : 4;

    private int calculateMemorySize(String string) {
        int onlyStringSize = 8 + 4 * 3 + processArchitectureLinkSize;
        int onlyCharArraySize = 8 + 4 + 2 * string.length();
        int diff = onlyCharArraySize % 8;
        int stabilizing = diff > 0 ? 8 - diff : 0;
        int stabilizedCharArraySize = onlyCharArraySize + stabilizing;
        return onlyStringSize + stabilizedCharArraySize;
    }

    private int blockNumber = 0;

    private Map<String, Filepath> block = new HashMap<>();

    public SPIMI(Serializer serializer){
        this.serializer = serializer;
    }

    void saveBlock() {
        serializer.getSerializer().save(BLOCKS_PATH + "block_" + blockNumber + ".bin", block, false);
        blockNumber++;
        usedMemoryTotal = 0;
        block.clear();
        badAndDirtyGC();
        System.out.println("Saved to block");
    }

    public void add(Record record, int fileCounter, String filePath, Long position) {
        String word = record.getTerm();

        if (!block.containsKey(word)) {
            block.put(word, new Filepath());
            usedMemoryTotal += calculateMemorySize(word);
        }

        block.get(word).add(filePath);
        usedMemoryTotal += calculateMemorySize(filePath);

        if (blockMemoryLimit <= usedMemoryTotal) {
            saveBlock();
        }
    }

    void badAndDirtyGC() {
        System.gc();
        System.runFinalization();
    }

    protected Filepath findSetForWord(String word, boolean invertArray) {
        if (!block.isEmpty()) {
            saveBlock();
        }

        Filepath queryPartSet;

        int blockNumberCurrent = blockNumber - 1;
        Set<String> findedInFiles = new HashSet<>();
        while(blockNumberCurrent-- > 0) {
            Map<String, Filepath> fileBlock = new HashMap<>();
            fileBlock = (HashMap<String, Filepath>) serializer.getSerializer().load(BLOCKS_PATH + "block_" + blockNumberCurrent + ".bin", fileBlock, false);
            Filepath fileQueryPartSet = fileBlock.get(word);
            if (Objects.nonNull(fileQueryPartSet)) {
                findedInFiles.addAll(fileQueryPartSet.allFilepaths());
            }
            fileBlock = null;
            badAndDirtyGC();
        }

        if (findedInFiles.isEmpty()) {
            System.err.println("Error: word '" + word + "' didn't find in inverted index");
            return null;
        }

        queryPartSet = new Filepath();
        queryPartSet.addAll(findedInFiles);

        if (invertArray) {
            Filepath queryAllSet = getAllFilepath();
            queryAllSet.removeAll(queryPartSet);
            queryPartSet = queryAllSet;
        }
        return queryPartSet;
    }
}