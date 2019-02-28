package com.fido.tro.data.indices.entity.suffixtree;

import java.util.*;

class EdgeBag {
    private int[] chars;
    private Edge[] values;
    private static final int BSEARCH_THRESHOLD = 6;

    public Edge put(Character character, Edge e) {
        char c = character;
        if (c != (char) (short) c) {
            throw new IllegalArgumentException("Illegal input character " + c + ".");
        }

        if (chars == null) {
            chars = new int[0];
            values = new Edge[0];
        }
        int idx = search(c);
        Edge previous = null;

        if (idx < 0) {
            int currentSize = chars.length;
            int[] copy = new int[currentSize + 1];
            System.arraycopy(chars, 0, copy, 0, currentSize);
            chars = copy;
            Edge[] copy1 = new Edge[currentSize + 1];
            System.arraycopy(values, 0, copy1, 0, currentSize);
            values = copy1;
            chars[currentSize] = (int) c;
            values[currentSize] = e;
            currentSize++;
            if (currentSize > BSEARCH_THRESHOLD) {
                sortArrays();
            }
        } else {
            previous = values[idx];
            values[idx] = e;
        }
        return previous;
    }

    public Edge get(Object maybeCharacter) {
        return get(((Character) maybeCharacter).charValue());
    }

    public Edge get(char c) {
        if (c != (char) (short) c) {
            throw new IllegalArgumentException("Illegal input character " + c + ".");
        }

        int idx = search(c);
        if (idx < 0) {
            return null;
        }
        return values[idx];
    }

    private int search(char c) {
        if (chars == null)
            return -1;

        if (chars.length > BSEARCH_THRESHOLD) {
            return Arrays.binarySearch(chars, (int) c);
        }

        for (int i = 0; i < chars.length; i++) {
            if (c == chars[i]) {
                return i;
            }
        }
        return -1;
    }

    Collection<Edge> values() {
        return Arrays.asList(values == null ? new Edge[0] : values);
    }

    private void sortArrays() {
        for (int i = 0; i < chars.length; i++) {
            for (int j = i; j > 0; j--) {
                if (chars[j-1] > chars[j]) {
                    int swap = chars[j];
                    chars[j] = chars[j-1];
                    chars[j-1] = swap;

                    Edge swapEdge = values[j];
                    values[j] = values[j-1];
                    values[j-1] = swapEdge;
                }
            }
        }
    }
}
