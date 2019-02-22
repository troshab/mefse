package com.fido.tro.Engine.data;

import org.cliffc.high_scale_lib.NonBlockingHashMap;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;

public class Matrix implements Serializable {
    public NonBlockingHashMap<Integer, String> header = new NonBlockingHashMap<>();
    public NonBlockingHashMap<String, BitSet> body = new NonBlockingHashMap<>();

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