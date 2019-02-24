package com.fido.tro.data.indices;

import com.fido.tro.data.Entity;
import com.fido.tro.data.fields.Filepath;

public class Trigram extends Inverted {
    @Override
    public String description() {
        return "trigrams in index variation of inverted index";
    }

    @Override
    public boolean isSearchable(String query) {
        return false;
    }

    public void add(Entity entity, int fileCounter, String filePath, Long position) {
        String word = "  " + entity.getTerm() + "  ";
        for (int beginIndex = 0, endIndex = beginIndex + 3; beginIndex < word.length() - 2; beginIndex++, endIndex++) {
            String key = word.substring(beginIndex, endIndex);
            if (!data.containsKey(key))
                data.put(key, new Filepath());

            data.get(key).add(filePath);
        }
    }
}
