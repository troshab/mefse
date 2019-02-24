package com.fido.tro.data.fields;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Positions implements Iterable {
    private List<Long> positions;

    Positions() {
        positions = new LinkedList<>();
    }

    public void add(Long position) {
        positions.add(position);
    }

    @Override
    public Iterator<Long> iterator() {
        return positions.iterator();
    }

    public List<Long> getPositions() {
        return positions;
    }
}
