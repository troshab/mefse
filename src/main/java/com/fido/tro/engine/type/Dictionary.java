package com.fido.tro.engine.type;

import com.fido.tro.db.Record;
import com.fido.tro.engine.EngineBase;
import org.apache.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;

public class Dictionary extends EngineBase {
    private final static Logger LOGGER = Logger.getLogger(Dictionary.class);

    private Set<String> data = new LinkedHashSet<>();

    @Override
    public void add(Record record, Integer fileCounter, String filePath, Long position) {
        data.add(record.getWord());
    }

    @Override
    protected boolean search(String[] queryParts) {
        LOGGER.error("search is not implementable for dictionary");
        return false;
    }

    @Override
    public String description() {
        return "just simple list of words";
    }

    @Override
    public void list() {
        for(String word : data) {
            LOGGER.warn("Word '" + word + "'");
        }
    }

    @Override
    public boolean isSearchable() {
        return false;
    }
}
