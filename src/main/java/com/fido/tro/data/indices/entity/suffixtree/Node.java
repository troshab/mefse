package com.fido.tro.data.indices.entity.suffixtree;

import java.util.*;
class Node {
    private int[] data;
    private int lastIdx = 0;
    private static final int START_SIZE = 0;
    private static final int INCREMENT = 1;
    private final EdgeBag edges;
    private Node suffix;

    Node() {
        edges = new EdgeBag();
        suffix = null;
        data = new int[START_SIZE];
    }

    Collection<Integer> getData() {
        return getData(-1);
    }

    Collection<Integer> getData(int numElements) {
        Set<Integer> ret = new HashSet<>();
        for (int num : data) {
            ret.add(num);
            if (ret.size() == numElements) {
                return ret;
            }
        }
        for (Edge e : edges.values()) {
            if (-1 == numElements || ret.size() < numElements) {
                for (int num : e.getDest().getData()) {
                    ret.add(num);
                    if (ret.size() == numElements) {
                        return ret;
                    }
                }
            }
        }
        return ret;
    }

    void addRef(int index) {
        if (contains(index)) {
            return;
        }

        addIndex(index);

        // add this reference to all the suffixes as well
        Node iter = this.suffix;
        while (iter != null) {
            if (iter.contains(index)) {
                break;
            }
            iter.addRef(index);
            iter = iter.suffix;
        }

    }

    private boolean contains(int index) {
        int low = 0;
        int high = lastIdx - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = data[mid];

            if (midVal < index)
                low = mid + 1;
            else if (midVal > index)
                high = mid - 1;
            else
                return true;
        }
        return false;
    }

    void addEdge(char ch, Edge e) {
        edges.put(ch, e);
    }

    Edge getEdge(char ch) {
        return edges.get(ch);
    }

    Node getSuffix() {
        return suffix;
    }

    void setSuffix(Node suffix) {
        this.suffix = suffix;
    }

    private void addIndex(int index) {
        if (lastIdx == data.length) {
            int[] copy = new int[data.length + INCREMENT];
            System.arraycopy(data, 0, copy, 0, data.length);
            data = copy;
        }
        data[lastIdx++] = index;
    }
}
