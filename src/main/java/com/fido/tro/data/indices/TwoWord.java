package com.fido.tro.data.indices;

import com.fido.tro.data.Entity;
import com.fido.tro.data.fields.Filepath;

public class TwoWord extends Inverted {
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

    public void add(Entity entity, String filePath) {
        if (!previousPath.equals(filePath)) {
            previousWord = "";
            previousPath = filePath;
        }

        if (previousWord.isEmpty()) {
            previousWord = entity.getTerm();
            return;
        }

        String key = previousWord + " " + entity.getTerm();

        if (!data.containsKey(key))
            data.put(key, new Filepath());

        data.get(key).add(filePath);

        previousWord = entity.getTerm();
    }
}
