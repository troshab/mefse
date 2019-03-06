package com.fido.tro.data.indices;

import com.fido.tro.data.Record;
import com.fido.tro.data.fields.MatrixRow;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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

    @Override
    public void allAdded() {

    }

    public void add(Record record, int fileNumber, String filePath, Long position) {
        header.put(fileNumber - 1, filePath);
        MatrixRow newMatrix = record.getMatrixRow();
        MatrixRow currentMatrix = body.get(record.getTerm());
        if (Objects.nonNull(currentMatrix)) {
            newMatrix.or(currentMatrix);
        }
        body.put(record.getTerm(), newMatrix);
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
        query.stream().forEach(key -> System.out.println("File: " + header.get(key)));
    }

    private MatrixRow findMatrixRow(String word, boolean invertArray) {
        MatrixRow queryPartBits = body.get(word);
        if (Objects.isNull(queryPartBits)) {
            System.out.println("Error: word '" + word + "' didn't find in matrix");
            return null;
        }

        if (invertArray) {
            queryPartBits.flip(0, header.size());
        }

        return queryPartBits;
    }
}