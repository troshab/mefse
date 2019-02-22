package com.fido.tro.Engine.type;

import com.fido.tro.DB.Record;
import com.fido.tro.Engine.EngineBase;

import java.util.LinkedHashSet;

public class Dictionary extends EngineBase {
    private LinkedHashSet<String> data = new LinkedHashSet<>();

    @Override
    public void add(Record record, Integer fileCounter, String filePath, Long position) {
        data.add(record.getWord());
    }

    @Override
    protected boolean search(String[] queryParts) {
        System.out.println("search is not implementable for dictionary");
        return false;
    }

    @Override
    public String description() {
        return "just simple list of words";
    }

    @Override
    public void list() {
        for(String word : data)
            System.out.println("Word '" + word +"'");
    }

    @Override
    public boolean isSearchable() {
        return false;
    }
}
