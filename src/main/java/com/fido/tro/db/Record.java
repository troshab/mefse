package com.fido.tro.db;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;

public class Record implements Serializable {
    private final static Logger LOGGER = Logger.getLogger(Record.class);

    private String word;

    Map<String, Set<Long>> paths;

    BitSet row;

    Record() {}
    Record(String word) {
        this.word = word;
    }

    void addPosition(String filePath, Integer col, Long position) {
        if (Objects.isNull(paths))
            paths = new HashMap<>();

        Set<Long> positions = paths.get(filePath);
        if (Objects.isNull(positions))
            positions = new LinkedHashSet<>();

        positions.add(position);
        paths.put(filePath, positions);

        if (Objects.isNull(row))
            row = new BitSet();

        row.set(col);
    }

    void increaseMatrix(Integer filesCounter) {
        row.set(filesCounter, false);
    }

    @Override
    protected void finalize() throws Throwable {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("db.Record successfully garbage cleaned");
        }
        super.finalize();
    }

    public String getWord() {
        return word;
    }

    Map<String, Set<Long>> getPaths() {
        return paths;
    }

    public BitSet getRow() {
        return row;
    }
}
