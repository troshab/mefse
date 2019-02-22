package com.fido.tro.Engine.data;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class MatrixData implements Serializable {
    public Map<Integer, String> header = new HashMap<>();
    public Map<String, BitSet> body = new HashMap<>();

    public String toString() {
        StringBuilder result = new StringBuilder("Header:\n");

        for(Map.Entry<Integer, String> headerCol : header.entrySet())
            result.append(headerCol.getKey()).append(" - ").append(headerCol.getValue()).append("\n");

        result.append("Body:");

        for(Map.Entry<String, BitSet> bodyRow : body.entrySet())
            result.append("\n").append(bodyRow.getKey()).append(": ").append(bodyRow.getValue());

        return result.toString();
    }
}