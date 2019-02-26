package com.fido.tro.data.indices;

import com.abahgat.suffixtree.GeneralizedSuffixTree;
import com.fido.tro.data.Entity;
import com.fido.tro.data.fields.Filepath;
import com.fido.tro.data.fields.FilepathWithId;
import com.fido.tro.data.indices.entity.suffixtree.Tree;

import java.util.*;

public class SuffixTree extends Inverted {
    private Tree data = new Tree();
    private Map<Integer, String> indexToWord = new HashMap<>();
    private Map<String, FilepathWithId> invertedIndex = new HashMap<>();
    private int wordIndex = 1;

    public void add(Entity entity, int fileCounter, String filePath, Long position) {
        String word = entity.getTerm();

        if (!invertedIndex.containsKey(word)) {
            invertedIndex.put(word, new FilepathWithId(wordIndex));
            data.put(word, wordIndex);
            indexToWord.put(wordIndex, word);
            wordIndex++;
        }

        invertedIndex.get(word).add(filePath);
    }

    @Override
    public boolean isSearchable(String query) {
        String[] queryParts = query.split(" ");
        Collection<Integer> results = data.search(query);
        Set<String> possibleWords = new HashSet<>();
        for(Integer resultIndex : results) {
            possibleWords.add(indexToWord.get(resultIndex));
        }
        return true;
    }

    @Override
    void search(String condition) {

    }
}
