package com.fido.tro.data.indices.threading;

import com.fido.tro.data.indices.entities.Filepath;
import com.fido.tro.serializers.Serializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class FindInSPIMI implements Runnable {
    private final String filepath;
    private final String word;
    private final Serializer serializer;
    private Set<String> findedInFiles;

    public FindInSPIMI(String filepath, String word, Serializer serializer, Set<String> findedInFiles) {
        this.filepath = filepath;
        this.word = word;
        this.serializer = serializer;
        this.findedInFiles = findedInFiles;
    }

    @Override
    public void run() {
        System.err.println(Thread.currentThread().getId() + "#: " + filepath);
        Map<String, Filepath> fileBlock = new HashMap<>();
        fileBlock = (HashMap<String, Filepath>) serializer.getSerializer().load(filepath, fileBlock, false);
        Filepath fileQueryPartSet = fileBlock.get(word);
        if (Objects.nonNull(fileQueryPartSet)) {
            findedInFiles.addAll(fileQueryPartSet.allFilepaths());
            //System.err.println(Thread.currentThread().getId() + "#: " + findedInFiles);
        }/* else {
            //System.err.println(Thread.currentThread().getId() + "#: none");
        }*/
    }
}