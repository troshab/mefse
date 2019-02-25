package com.fido.tro.data.indices;

import com.fido.tro.data.Entity;
import com.fido.tro.data.fields.Filepath;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Inverted extends Searchable {

    protected Map<String, Filepath> data = new HashMap<>();

    private Filepath query = new Filepath();

    public String description() {
        return "list of words and files in which they are appeared";
    }

    public boolean isSearchable(String query) {
        search(query);
        return true;
    }

    public void add(Entity entity, int fileCounter, String filePath, Long position) {
        String word = entity.getTerm();

        if (!data.containsKey(word))
            data.put(word, new Filepath());

        data.get(word).add(filePath);
    }


    public void list() {
        if (data.isEmpty()) {
            System.err.println("Indexes database is empty!");
            return;
        }
        for (Map.Entry<String, Filepath> entry : data.entrySet()) {
            String word = entry.getKey();
            Filepath paths = entry.getValue();
            System.out.println("Word '" + word + "':");
            for (String path : paths) {
                System.out.println(" => " + path);
            }
        }
    }

    public void initSearch(String word, boolean invertArray) {
        Filepath queryPartSet = findSetForWord(word, invertArray);
        if (Objects.isNull(queryPartSet)) {
            return;
        }

        if (invertArray) {
            Filepath queryAllSet = getAllFilepath();
            queryAllSet.removeAll(queryPartSet);
            query = queryAllSet;
        } else {
            query = queryPartSet;
        }
    }

    public void or(String word, boolean invertArray) {
        Filepath queryPartSet = findSetForWord(word, invertArray);
        if (Objects.isNull(queryPartSet)) {
            return;
        }
        query.addAll(queryPartSet);
    }

    public void and(String word, boolean invertArray) {
        Filepath queryPartSet = findSetForWord(word, invertArray);
        if (Objects.isNull(queryPartSet)) {
            return;
        }
        query.retainAll(Objects.requireNonNull(queryPartSet));
    }

    public void searchResult() {
        for (String filePath : query) {
            System.out.println("File: " + filePath);
        }
    }

    protected Filepath findSetForWord(String word, boolean invertArray) {
        Filepath queryPartSet = data.get(word);
        if (Objects.isNull(queryPartSet)) {
            System.err.println("Error: word '" + word + "' didn't find in inverted index");
            return null;
        }

        if (invertArray) {
            Filepath queryAllSet = getAllFilepath();
            queryAllSet.removeAll(queryPartSet);
            queryPartSet = queryAllSet;
        }
        return queryPartSet;
    }

    Filepath getAllFilepath() {
        Filepath queryAllSet = new Filepath();

        Set<Map.Entry<String, Filepath>> queryAllSetEntries = data.entrySet();
        for (Map.Entry<String, Filepath> queryAllSetEntry : queryAllSetEntries) {
            queryAllSet.addAll(queryAllSetEntry.getValue());
        }

        return queryAllSet;
    }
}
