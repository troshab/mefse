package com.fido.tro.data.indices;

import com.fido.tro.cli.CliBenchmark;
import com.fido.tro.data.Record;
import com.fido.tro.data.indices.entities.Filepath;
import com.fido.tro.data.indices.threading.CustomThreadPool;
import com.fido.tro.data.indices.threading.FindInSPIMI;
import com.fido.tro.data.indices.threading.SaveSPIMIBlock;
import com.fido.tro.serializers.Serializer;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class SPIMI extends Inverted {
    private int processorsCount = Runtime.getRuntime().availableProcessors();
    private double memoryLimitCoefficient = 1;
    private int blockNumber = 0;

    private final String BLOCKS_PATH = "d:\\blocks_spimi\\";
    /**
     * Direct GC call is bad so consider
     */
    private Serializer serializer;

    /** MEMORY MAGIC BEGINS (ツ)_/ ﾟ.*・｡ﾟ
     *
     * allocatedMemory - already allocated memory
     * presumableFreeMemory - memory that really available for us
     */

    private long allocatedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    private long presumableFreeMemory = Runtime.getRuntime().maxMemory() - allocatedMemory;

    private Set<String> findedInFiles = new LinkedHashSet<>();
    private long usedMemoryTotal = 0L;
    private Boolean savingToMemory = false;

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

    private Map<String, Filepath> block = new HashMap<>();

    public SPIMI(Serializer serializer){
        this.serializer = serializer;
    }

    private void saveBlock(boolean searchSave) {

        CliBenchmark cliBenchmark = new CliBenchmark(true);
        Map<String, Filepath> oldBlock;
        synchronized (block) {
            oldBlock = block;
            block = new HashMap<>();
        }

        SaveSPIMIBlock saveSPIMIBlock = new SaveSPIMIBlock(oldBlock, BLOCKS_PATH + "block_" + blockNumber + ".bin");
        new Thread(saveSPIMIBlock).start();

        usedMemoryTotal = 0L;
        blockNumber++;

        synchronized (savingToMemory) {
            savingToMemory = false;
        }

        cliBenchmark.timeTaken(true);

        System.out.println("Saved to block");
    }

    public void add(Record record, int fileCounter, String filePath, Long position) {
        String word = record.getTerm();

        synchronized (block) {
            if (!block.containsKey(word)) {
                block.put(word, new Filepath());
                usedMemoryTotal += calculateMemorySize(word);
            }
            Filepath wordInMap;
            do {
                wordInMap = block.get(word);
            } while (wordInMap == null);
            wordInMap.add(filePath);
            usedMemoryTotal += calculateMemorySize(filePath);
        }
        synchronized (savingToMemory) {
            if (!savingToMemory && blockMemoryLimit <= usedMemoryTotal) {
                savingToMemory = true;
                saveBlock(false);
            }
        }
    }
    private long blockMemoryLimit = Math.round((presumableFreeMemory * memoryLimitCoefficient) / processorsCount);

    protected Filepath findSetForWord(String word, boolean invertArray) {
        if (!block.isEmpty()) {
            System.out.println("Has unsaved block.");
            saveBlock(true);
        }

        CliBenchmark cliBenchmark = new CliBenchmark(true);
        int blockNumberCurrent = 0;
        findedInFiles.clear();
        CustomThreadPool customThreadPool = new CustomThreadPool(processorsCount);
        while (blockNumberCurrent < blockNumber) {
            FindInSPIMI findTask = new FindInSPIMI(BLOCKS_PATH + "block_" + blockNumberCurrent + ".bin", word, serializer, findedInFiles);
            do {
            } while (customThreadPool.queue.size() > processorsCount);
            customThreadPool.execute(findTask);
            blockNumberCurrent++;
            //findTask.run();
        }
        customThreadPool.shutdown();
        cliBenchmark.timeTaken(true);

        if (findedInFiles.isEmpty()) {
            System.err.println("Error: word '" + word + "' didn't find in SPIM index");
            return null;
        }

        Filepath queryPartSet = new Filepath();
        queryPartSet.addAll(findedInFiles);

        if (invertArray) {
            Filepath queryAllSet = getAllFilepath();
            queryAllSet.removeAll(queryPartSet);
            queryPartSet = queryAllSet;
        }
        return queryPartSet;
    }
}