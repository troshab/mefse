package com.fido.tro.Engine.type;

import com.fido.tro.DB.Record;
import com.fido.tro.Mefse;

import java.util.*;

public class CoordinatedIndex extends InvertedIndex {
    private Map<String, Map<String, Set<Long>>> data = new HashMap<>();

    @Override
    protected boolean search(String[] queryParts) {
        Mefse.logger.info("search is not implementable for coordinated index");
        return false;
    }

    @Override
    public void add(Record record, Integer fileCounter, String filePath, Long position) {
        Map<String, Set<Long>> paths = data.get(record.getWord());
        if (Objects.isNull(paths)) {
            paths = new HashMap<>();
        }

        Set<Long> positions = paths.get(filePath);
        if (Objects.isNull(positions)) {
            positions = new LinkedHashSet<>();
        }
        positions.add(position);
        paths.put(filePath, positions);
        data.put(record.getWord(), paths);
    }

    @Override
    public void list() {
        if (data.isEmpty()) {
            Mefse.logger.info("Indexes database is empty!");
            return;
        }
        for (Map.Entry<String, Map<String, Set<Long>>> entry : data.entrySet()) {
            String word = entry.getKey();
            Map<String, Set<Long>> paths = entry.getValue();
            Mefse.logger.info("Word '" + word + "':");
            for (Map.Entry<String, Set<Long>> pathEntry : paths.entrySet()) {
                String path = pathEntry.getKey();
                Set<Long> positions = pathEntry.getValue();
                StringBuilder positionsString = new StringBuilder();
                for (Long position : positions) {
                    positionsString.append(position).append(", ");
                }
                Mefse.logger.info(" => " + path + ": " + positionsString.substring(0, positionsString.length() - 2));
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
