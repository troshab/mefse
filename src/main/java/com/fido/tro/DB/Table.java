package com.fido.tro.DB;

import com.fido.tro.Mefse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Table implements Serializable {
    public Map<String, Record> db = new HashMap<>();
    Map<Integer, String> files = new HashMap<>();
    Integer filesCounter = 0;

    Table() {}

    @Override
    protected void finalize() throws Throwable {
        Mefse.logger.info("DB.Table successfully garbage cleaned");
        super.finalize();
    }
}
