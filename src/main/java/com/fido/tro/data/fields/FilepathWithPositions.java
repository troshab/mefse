package com.fido.tro.data.fields;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FilepathWithPositions {
    private Map<String, Positions> filepathWithPositions;

    public FilepathWithPositions() {
        filepathWithPositions = new HashMap<>();
    }

    public Positions get(String key) {
        if (!exist(key)) {
            filepathWithPositions.put(key, new Positions());
        }

        return filepathWithPositions.get(key);
    }

    private boolean exist(String key) {
        return filepathWithPositions.containsKey(key);
    }

    public Set<Map.Entry<String, Positions>> entrySet() {
        return filepathWithPositions.entrySet();
    }
}
