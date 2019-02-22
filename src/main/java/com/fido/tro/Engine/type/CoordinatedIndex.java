package com.fido.tro.Engine.type;

import com.fido.tro.DB.Record;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

import java.util.*;

public class CoordinatedIndex extends InvertedIndex {
    private NonBlockingHashMap<String,  NonBlockingHashMap<String, LinkedHashSet<Long>>> data = new NonBlockingHashMap<>();

    @Override
    protected boolean search(String[] queryParts) {
        System.out.println("search is not implementable for coordinated index");
        return false;
    }

    @Override
    public void add(Record record, Integer fileCounter, String filePath, Long position) {
        NonBlockingHashMap<String, LinkedHashSet<Long>> paths = data.get(record.getWord());
        if (Objects.isNull(paths))
            paths = new NonBlockingHashMap<>();

        LinkedHashSet<Long> positions = paths.get(filePath);
        if (Objects.isNull(positions))
            positions = new LinkedHashSet<>();

        positions.add(position);
        paths.put(filePath, positions);
        data.put(record.getWord(), paths);
    }

    @Override
    public void list() {
        if (data.isEmpty()) {
            System.out.println("Indexes database is empty!");
            return;
        }
        for (Map.Entry<String, NonBlockingHashMap<String, LinkedHashSet<Long>>> entry : data.entrySet()) {
            String word = entry.getKey();
            NonBlockingHashMap<String, LinkedHashSet<Long>> paths = entry.getValue();
            System.out.println("Word '" + word + "':");
            for (Map.Entry<String, LinkedHashSet<Long>> pathEntry : paths.entrySet()) {
                String path = pathEntry.getKey();
                LinkedHashSet<Long> positions = pathEntry.getValue();
                String positionsString = "";
                for (Long position : positions)
                    positionsString += position + ", ";
                System.out.println(" => " + path + ": " + positionsString.substring(0, positionsString.length() - 2));
            }
        }
    }

    @Override
    public String description() {
        return "variation of inverted index with positions with what words appears in books";
    }

    @Override
    public boolean isSearchable() {
        return false;
    }
}
