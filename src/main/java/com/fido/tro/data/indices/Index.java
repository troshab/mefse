package com.fido.tro.data.indices;

import com.fido.tro.data.Record;

public interface Index {
    String description();
    void add(Record record, int fileNumber, String filepath, Long position);
    void list();
    boolean isSearchable(String query);
    void allAdded();
}
