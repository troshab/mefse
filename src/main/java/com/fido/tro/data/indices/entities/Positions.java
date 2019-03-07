package com.fido.tro.data.indices.entities;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Positions implements Iterable {
    private List<Long> positions = new LinkedList<>();

    public void add(Long position) {
        positions.add(position);
    }

    @Override
    @Nonnull
    public Iterator<Long> iterator() {
        return positions.iterator();
    }

    public List<Long> getPositions() {
        return positions;
    }
}
