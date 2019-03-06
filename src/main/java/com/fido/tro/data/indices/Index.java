package com.fido.tro.data.indices;

import com.fido.tro.data.Entity;

public interface Index {
    String description();
    void add(Entity entity, int fileNumber, String filepath, Long position);
    void list();
    boolean isSearchable(String query);
    void allAdded();
}
