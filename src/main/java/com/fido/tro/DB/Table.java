package com.fido.tro.DB;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Table implements Serializable {
    public Map<String, Record> db = new HashMap<>();
    Map<Integer, String> files = new HashMap<>();
    Integer filesCounter = 0;

    Table() {}

    @Override
    protected void finalize() { Mefse.logger.info("DBEngine successfully garbage cleaned"); }
}
