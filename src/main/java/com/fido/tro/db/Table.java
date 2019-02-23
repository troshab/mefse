package com.fido.tro.db;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Table implements Serializable {
    private final static Logger LOGGER = Logger.getLogger(Table.class);

    public Map<String, Record> db = new HashMap<>();
    Map<Integer, String> files = new HashMap<>();
    Integer filesCounter = 0;

    Table() {}

    @Override
    protected void finalize() throws Throwable {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("db.Table successfully garbage cleaned");
        }
        super.finalize();
    }
}
