package com.fido.tro.data.indices;

import com.fido.tro.data.Entity;
import com.fido.tro.data.fields.MatrixRow;

import java.util.*;

public class Matrix extends Searchable {
    private Map<Integer, String> header = new HashMap<>();
    private Map<String, MatrixRow> body = new HashMap<>();
    private MatrixRow query = new MatrixRow();

    public String toString() {
        StringBuilder result = new StringBuilder("Header:\n");

        for (Map.Entry<Integer, String> headerCol : header.entrySet())
            result.append(headerCol.getKey()).append(" - ").append(headerCol.getValue()).append("\n");

        result.append("Body:");

        for (Map.Entry<String, MatrixRow> bodyRow : body.entrySet())
            result.append("\n").append(bodyRow.getKey()).append(": ").append(bodyRow.getValue());

        return result.toString();
    }

    public String description() {
        return "matrix-like variation of inverted index";
    }
    public boolean isSearchable(String query) {
        search(query);
        return true;
    }

    public void add(Entity entity, int fileNumber, String filePath, Long position) {
        header.put(fileNumber, filePath);
        body.put(entity.getTerm(), entity.getMatrixRow());
    }

    public void list() {
        System.out.println(this);
    }

    public void initSearch(String word, boolean invertArray) {
        MatrixRow queryPartBits = findMatrixRow(word, invertArray);
        if (Objects.isNull(queryPartBits)) {
            return;
        }

        if (invertArray) {
            MatrixRow allFiles = new MatrixRow();
            Set<Map.Entry<Integer, String>> allFilesEntries = header.entrySet();
            for (Map.Entry<Integer, String> allFilesEntry : allFilesEntries) {
                allFiles.set(allFilesEntry.getKey());
            }
            allFiles.andNot(Objects.requireNonNull(queryPartBits));
            query = allFiles;
        } else
            query = queryPartBits;
    }

    public void or(String word, boolean invertArray) {
        MatrixRow queryPartBits = findMatrixRow(word, invertArray);
        if (Objects.isNull(queryPartBits)) {
            return;
        }
        query.or(Objects.requireNonNull(queryPartBits));
    }

    public void and(String word, boolean invertArray) {
        MatrixRow queryPartBits = findMatrixRow(word, invertArray);
        if (Objects.isNull(queryPartBits)) {
            return;
        }
        query.and(Objects.requireNonNull(queryPartBits));
    }

    public void searchResult() {
        PrimitiveIterator.OfInt queryResults = query.stream().iterator();
        while (queryResults.hasNext()) {
            System.out.println("File: " + header.get(queryResults.next()));
        }
    }

    private MatrixRow findMatrixRow(String word, boolean invertArray) {
        MatrixRow queryPartBits = body.get(word);
        if (Objects.isNull(queryPartBits)) {
            System.out.println("Error: word '" + word + "' didn't find in matrix");
            return null;
        }

        if (invertArray) {
            queryPartBits.flip(0, queryPartBits.length());
        }

        return queryPartBits;
    }
}