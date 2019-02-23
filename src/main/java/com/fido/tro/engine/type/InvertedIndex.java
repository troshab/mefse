package com.fido.tro.engine.type;

import com.fido.tro.db.Record;
import com.fido.tro.engine.EngineBase;
import org.apache.log4j.Logger;

import java.util.*;

public class InvertedIndex extends EngineBase {
    private final static Logger LOGGER = Logger.getLogger(InvertedIndex.class);

    Map<String, Set<String>> data = new HashMap<>();
    private Set<String> query = new LinkedHashSet<>();
    private LinkedHashSet<String> queryAllSet;

    @Override
    public void add(Record record, Integer fileCounter, String filePath, Long position) {
        Set<String> files = data.get(record.getWord());
        if (Objects.isNull(files)) {
            files = new LinkedHashSet<>();
        }

        files.add(filePath);
        data.put(record.getWord(), files);
    }

    @Override
    public void list() {
        if (data.isEmpty()) {
            LOGGER.error("Indexes database is empty!");
            return;
        }
        for (Map.Entry<String, Set<String>> entry : data.entrySet()) {
            String word = entry.getKey();
            Set<String> paths = entry.getValue();
            LOGGER.warn("Word '" + word + "':");
            for (String path : paths) {
                LOGGER.warn(" => " + path);
            }
        }
    }

    @Override
    public boolean isSearchable() {
        return true;
    }

    @Override
    public void initSearch(String word, boolean invertArray) {
        Set<String> queryPartSet = findSetForWord(word, invertArray);
        if (Objects.isNull(queryPartSet)) {
            return;
        }

        if (invertArray) {
            LinkedHashSet<String> queryAllSet = getAllSet();
            queryAllSet.removeAll(queryPartSet);
            query = queryAllSet;
        } else {
            query = queryPartSet;
        }
    }

    private LinkedHashSet<String> getAllSet() {
        if (Objects.nonNull(queryAllSet))
            return queryAllSet;

        queryAllSet = new LinkedHashSet<>();
        Set<Map.Entry<String, Set<String>>> queryAllSetEntries = data.entrySet();
        for(Map.Entry<String, Set<String>> queryAllSetEntry : queryAllSetEntries) {
            queryAllSet.addAll(queryAllSetEntry.getValue());
        }

        return queryAllSet;
    }

    @Override
    public void or(String word, boolean invertArray) {
        Set<String> queryPartSet = findSetForWord(word, invertArray);
        if (Objects.isNull(queryPartSet)) {
            return;
        }
        query.addAll(queryPartSet);
    }

    @Override
    public void and(String word, boolean invertArray) {
        Set<String> queryPartSet = findSetForWord(word, invertArray);
        if (Objects.isNull(queryPartSet)) {
            return;
        }
        query.retainAll(Objects.requireNonNull(queryPartSet));
    }

    @Override
    public void searchResult() {
        for (String filePath : query) LOGGER.warn("File: " + filePath);
    }

    private Set<String> findSetForWord(String word, boolean invertArray) {
        Set<String> queryPartSet = data.get(word);
        if (Objects.isNull(queryPartSet)) {
            LOGGER.error("Error: word '" + word + "' didn't find in inverted index");
            return null;
        }

        if (invertArray) {
            LinkedHashSet<String> queryAllSet = getAllSet();
            queryAllSet.removeAll(queryPartSet);
            queryPartSet = queryAllSet;
        }
        return queryPartSet;
    }

    @Override
    public String description() {
        return "list of words and files in which they are appeared";
    }
}
