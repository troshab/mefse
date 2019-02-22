package com.fido.tro.Engine.type;

import com.fido.tro.DB.Record;
import com.fido.tro.Engine.EngineBase;
import com.fido.tro.Engine.data.MatrixData;

import java.util.*;

public class Matrix extends EngineBase {
    private MatrixData data = new MatrixData();
    private BitSet query = new BitSet();

    @Override
    public void add(Record record, Integer fileCounter, String filePath, Long position) {
        data.header.put(fileCounter, filePath);
        data.body.put(record.getWord(), record.getRow());
    }

    @Override
    public String description() {
        return "matrix-like variation of inverted index";
    }

    @Override
    public void initSearch(String word, boolean invertArray) {
        BitSet queryPartBits = findBitsForWord(word, invertArray);
        if (Objects.isNull(queryPartBits)) {
            return;
        }

        if (invertArray) {
            BitSet allFiles = new BitSet();
            Set<Map.Entry<Integer, String>> allFilesEntries = data.header.entrySet();
            for(Map.Entry<Integer, String> allFilesEntry : allFilesEntries) {
                allFiles.set(allFilesEntry.getKey());
            }
            allFiles.andNot(Objects.requireNonNull(queryPartBits));
            query = allFiles;
        } else
            query = queryPartBits;
    }

    @Override
    public void or(String word, boolean invertArray) {
        BitSet queryPartBits = findBitsForWord(word, invertArray);
        if (Objects.isNull(queryPartBits)) {
            return;
        }
        query.or(Objects.requireNonNull(queryPartBits));
    }

    @Override
    public void and(String word, boolean invertArray) {
        BitSet queryPartBits = findBitsForWord(word, invertArray);
        if (Objects.isNull(queryPartBits)) {
            return;
        }
        query.and(Objects.requireNonNull(queryPartBits));
    }

    @Override
    public void searchResult() {
        PrimitiveIterator.OfInt queryResults = query.stream().iterator();
        while(queryResults.hasNext()) {
            System.out.println("File: " + data.header.get(queryResults.next()));
        }
    }

    private BitSet findBitsForWord(String word, boolean invertArray) {
        BitSet queryPartBits = data.body.get(word);
        if (Objects.isNull(queryPartBits)) {
            System.out.println("Error: word '" + word + "' didn't find in matrix");
            return null;
        }

        if (invertArray) {
            queryPartBits.flip(0, queryPartBits.length());
        }

        return queryPartBits;
    }

    @Override
    public void list() {
        System.out.println(data);
    }

    @Override
    public boolean isSearchable() {
        return true;
    }
}