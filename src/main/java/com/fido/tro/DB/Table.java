package com.fido.tro.DB;

import org.cliffc.high_scale_lib.NonBlockingHashMap;

import java.io.Serializable;
import java.util.Map;

public class Table implements Serializable {
    public Map<String, Record> db = new NonBlockingHashMap<>();
    Map<Integer, String> files = new NonBlockingHashMap<>();
    Integer filesCounter = 0;

    Table() {}

    @Override
    protected void finalize() { System.out.println("DBEngine successfully garbage cleaned"); }
}
