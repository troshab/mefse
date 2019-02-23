package com.fido.tro.engine.type;

import com.fido.tro.db.Record;
import org.apache.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class TwoWordInvertedIndex extends InvertedIndex {
    private final static Logger LOGGER = Logger.getLogger(TwoWordInvertedIndex.class);

    private static String previousWord = "";
    private static String previousPath = "";

    @Override
    protected boolean search(String[] queryParts) {
        LOGGER.fatal("search is not implementable for two word inverted index");
        return false;
    }

    @Override
    public void add(Record word, Integer fileCounter, String filePath, Long position) {
        if (!previousPath.equals(filePath)) {
            previousWord = "";
            previousPath = filePath;
        }

        if (previousWord.isEmpty()) {
            previousWord = word.getWord();
            return;
        }

        String key = previousWord + " " + word.getWord();
        Set<String> files = data.get(key);
        if (Objects.isNull(files)) {
            files = new LinkedHashSet<>();
        }

        files.add(filePath);
        data.put(key, files);

        previousWord = word.getWord();
    }

    @Override
    public String description() {
        return "two-words in index variation of inverted index";
    }

    @Override
    public boolean isSearchable() {
        return false;
    }
}
