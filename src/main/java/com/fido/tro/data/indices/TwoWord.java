package com.fido.tro.data.indices;

import com.fido.tro.data.Record;
import com.fido.tro.data.fields.Filepath;

import java.util.HashMap;
import java.util.Map;

public class TwoWord extends Inverted {
    private Map<String, Filepath> data = new HashMap<>();

    private static String previousWord = "";
    private static String previousPath = "";

    @Override
    public String description() {
        return "two-words in index variation of inverted index";
    }

    @Override
    public boolean isSearchable(String query) {
        return false;
    }

    public void add(Record record, String filePath) {
        if (!previousPath.equals(filePath)) {
            previousWord = "";
            previousPath = filePath;
        }

        if (previousWord.isEmpty()) {
            previousWord = record.getTerm();
            return;
        }

        String key = previousWord + " " + record.getTerm();

        if (!data.containsKey(key))
            data.put(key, new Filepath());

        data.get(key).add(filePath);

        previousWord = record.getTerm();
    }
}
