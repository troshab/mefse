package com.fido.tro.Engine.type;

import com.fido.tro.DB.Record;

import java.util.*;

public class TwoWordInvertedIndex extends InvertedIndex {
    private static String previousWord = "";
    private static String previousPath = "";

    @Override
    protected boolean search(String[] queryParts) {
        System.out.println("search is not implementable for two word inverted index");
        return false;
    }

    @Override
    public void add(Record word, Integer fileCounter, String filePath, Long position) {
        if (previousPath != filePath) {
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
