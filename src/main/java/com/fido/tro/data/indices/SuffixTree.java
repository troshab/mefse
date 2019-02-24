package com.fido.tro.data.indices;

import com.abahgat.suffixtree.GeneralizedSuffixTree;
import com.fido.tro.data.Entity;

import java.util.*;

public class SuffixTree implements Index {
    int wordCounter = 1;
    Map<Integer, String> data;
    Set<String> findedWords;

    GeneralizedSuffixTree tree;

    public SuffixTree() {
        data = new HashMap<>();
        findedWords = new HashSet<>();
        tree = new GeneralizedSuffixTree();
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public void add(Entity entity, int fileNumber, String filepath, Long position) {
        if (!findedWords.contains(entity.getTerm())) {
            data.put(wordCounter, entity.getTerm());
            tree.put(entity.getTerm(), wordCounter);
            findedWords.add(entity.getTerm());
            wordCounter++;
        }
    }

    @Override
    public void list() {

    }

    @Override
    public boolean isSearchable(String query) {
        Collection<Integer> results = tree.search(query);
        if (!results.isEmpty()) {
            for(Integer key : results) {
                System.out.println("Maybe word: \"" + data.get(key) + "\"");
            }
        } else {
            System.err.println("Nothing found");
        }
        return true;
    }
}
