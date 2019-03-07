package com.fido.tro.data.indices;

import com.fido.tro.data.Record;
import com.fido.tro.data.indices.entities.Filepath;
import com.fido.tro.serializers.Serializer;

import java.util.*;

public class SPIMI extends Inverted {

    private final String BLOCKS_PATH = "d:\\blocks_spimi\\";
    /**
     * Direct GC call is bad so consider
     */
    private boolean weAreBadAndDirty = false;
    private Serializer serializer;

    /** MEMORY MAGIC BEGINS (ツ)_/ ﾟ.*・｡ﾟ
     *
     * allocatedMemory - already allocated memory
     * presumableFreeMemory - memory that really available for us
     */

    private long allocatedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    private long presumableFreeMemory = Runtime.getRuntime().maxMemory() - allocatedMemory;

    private long blockMemoryLimit = Math.round(presumableFreeMemory * 0.0005);
    private long usedMemoryTotal = 0L;
    /**
     * onlyStringSize:
     *  new String ()
     *  Header: 8
     *  Fields: 4 * (int offset, int count, int hash)
     *  Reference to char array: if х64 then 8 else if x32 then 4
     *
     * onlyCharArraySize:
     *  new char [N]
     *  Header: 8 (int length)
     *  Fields: 4 * (int length)
     *  Primitives char size: 2 * N
     *
     * stabilizedCharArraySize:
     *  Also we need multiplicity up to 8
     *
     *  calculateMemorySize return in bytes
     */

    private String arch = System.getenv("PROCESSOR_ARCHITECTURE");
    private String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

    private int processArchitectureLinkSize = arch != null && arch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64") ? 8 : 4;

    private int onlyStringSize = 8 + 4 * 3 + processArchitectureLinkSize;
    private int calculateMemorySize(String string) {
        int onlyCharArraySize = 8 + 4 + 2 * string.length();
        int diff = onlyCharArraySize % 8;
        int stabilizing = diff > 0 ? 8 - diff : 0;
        int stabilizedCharArraySize = onlyCharArraySize + stabilizing;
        return (onlyStringSize + stabilizedCharArraySize) / 8;
    }

    /** MEMORY MAGIC ENDS ¯\_(ツ)_/¯ */

    private int blockNumber = 0;

    private Map<String, Filepath> block = new HashMap<>();

    public SPIMI(Serializer serializer){
        this.serializer = serializer;
    }

    private void saveBlock() {
        serializer.getSerializer().save(BLOCKS_PATH + "block_" + blockNumber + ".bin", block, false);
        blockNumber++;
        usedMemoryTotal = 0L;
        block.clear();
        if (weAreBadAndDirty) {
            badAndDirtyGC();
        }
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

    private void badAndDirtyGC() {
        System.gc();
        System.runFinalization();
    }

    @SuppressWarnings("unchecked")
    protected Filepath findSetForWord(String word, boolean invertArray) {
        if (!block.isEmpty()) {
            System.out.println("Has unsaved block.");
            saveBlock();
        }

        Filepath queryPartSet;

        int blockNumberCurrent = blockNumber;
        Set<String> findedInFiles = new HashSet<>();
        while(blockNumberCurrent-- > 0) {
            Map<String, Filepath> fileBlock = new HashMap<>();
            fileBlock = (HashMap<String, Filepath>) serializer.getSerializer().load(BLOCKS_PATH + "block_" + blockNumberCurrent + ".bin", fileBlock, false);
            Filepath fileQueryPartSet = fileBlock.get(word);
            if (Objects.nonNull(fileQueryPartSet)) {
                findedInFiles.addAll(fileQueryPartSet.allFilepaths());
            }
            fileBlock.clear();
            if (weAreBadAndDirty) {
                badAndDirtyGC();
            }
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