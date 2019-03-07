package com.fido.tro.data.indices;

import com.fido.tro.data.Record;

import java.util.LinkedHashSet;
import java.util.Set;

public class Dictionary implements Index {
    private Set<String> data = new LinkedHashSet<>();

    public String description() {
        return "just simple list of words";
    }

    public boolean isSearchable(String query) {
        return false;
    }

    public void add(Record record, int fileCounter, String filePath, Long position) {
        data.add(record.getTerm());
    }

    public void list() {
        for (String word : data) {
            System.out.println("Word '" + word + "'");
        }
    }
}
