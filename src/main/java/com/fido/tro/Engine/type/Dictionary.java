package com.fido.tro.Engine.type;

import com.fido.tro.DB.Record;
import com.fido.tro.Engine.EngineBase;

import java.util.LinkedHashSet;
import java.util.Set;

public class Dictionary extends EngineBase {
    private Set<String> data = new LinkedHashSet<>();

    @Override
    public void add(Record record, Integer fileCounter, String filePath, Long position) {
        data.add(record.getWord());
    }

    @Override
    protected boolean search(String[] queryParts) {
        Mefse.logger.info("search is not implementable for dictionary");
        return false;
    }

    @Override
    public String description() {
        return "just simple list of words";
    }

    @Override
    public void list() {
        for(String word : data)
            Mefse.logger.info("Word '" + word +"'");
    }

    @Override
    public boolean isSearchable() {
        return false;
    }
}
