package com.fido.tro.data.fields;

import java.util.BitSet;
import java.util.stream.IntStream;

public class MatrixRow {
    private BitSet matrixRow;
    private BitSet getMatrixRow() {
        return matrixRow;
    }

    public MatrixRow() {
        this.matrixRow = new BitSet();
    }

    public void set(int column) {
        matrixRow.set(column);
    }

    public int length() {
        return matrixRow.length();
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
