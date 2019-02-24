package com.fido.tro.data.fields;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FilepathWithPositions {
    private Map<String, Positions> filepathsWithPositions;

    public FilepathWithPositions() {
        filepathsWithPositions = new HashMap<>();
    }

    public Positions get(String key) {
        if (!exist(key)) {
            filepathsWithPositions.put(key, new Positions());
        }

        return filepathsWithPositions.get(key);
    }

    private boolean exist(String key) {
        return filepathsWithPositions.containsKey(key);
    }

    public Set<Map.Entry<String, Positions>> entrySet() {
        return filepathsWithPositions.entrySet();
    }
}
