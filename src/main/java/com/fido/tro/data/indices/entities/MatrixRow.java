package com.fido.tro.data.indices.entities;

import java.util.BitSet;
import java.util.stream.IntStream;

public class MatrixRow {
    private BitSet matrixRow;

    public MatrixRow() {
        this.matrixRow = new BitSet();
    }

    private BitSet getMatrixRow() {
        return matrixRow;
    }

    public void set(int column) {
        matrixRow.set(column);
    }

    public void flip(int fromIndex, int toIndex) {
        matrixRow.flip(fromIndex, toIndex);
    }

    public void andNot(MatrixRow andNotMatrixRow) {
        matrixRow.andNot(andNotMatrixRow.getMatrixRow());
    }

    public void or(MatrixRow orMatrixRow) {
        matrixRow.or(orMatrixRow.getMatrixRow());
    }

    public IntStream stream() {
        return matrixRow.stream();
    }

    public void and(MatrixRow andMatrixRow) {
        matrixRow.and(andMatrixRow.getMatrixRow());
    }
}
