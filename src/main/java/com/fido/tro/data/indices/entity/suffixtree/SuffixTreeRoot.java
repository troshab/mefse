package com.fido.tro.data.indices.entity.suffixtree;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SuffixTreeRoot {
    private HashMap<Character, SuffixTreeNode> letters = new HashMap<>();
    private Map<Integer, String> wordsId = new HashMap<>();
    private Map<String, Integer> IdWords = new HashMap<>();
    private Integer wordIndex = 0;

    public void addNode(String word) {
        Integer wordId = IdWords.get(word);
        if (Objects.isNull(wordId)) {
            IdWords.put(word, wordIndex);
            wordsId.put(wordIndex, word);
            wordIndex++;
        }
        HashMap<Character, SuffixTreeNode> childNode = letters;
        for(int letterIndex = word.length() - 1; 0 <= letterIndex; letterIndex--) {
            Character letter = word.charAt(letterIndex);
            System.out.println("letter: " + letter);
            SuffixTreeNode letterNode = letters.get(word);
            if (Objects.isNull(letterNode)) {
                childNode = new SuffixTreeNode(letter, wordId, childNode);
                letters.put(letter, childNode);
            } else {
                childNode = letterNode.addNode(letter, wordId, childNode);
            }
        }
    }
}
