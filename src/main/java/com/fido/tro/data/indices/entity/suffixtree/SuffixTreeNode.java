package com.fido.tro.data.indices.entity.suffixtree;

import java.util.*;

class SuffixTreeNode {
    private Character letter;
    private List<Integer> wordsId = new ArrayList<>();
    private HashMap<Character, List<SuffixTreeNode>> childes = new HashMap<>();

    SuffixTreeNode(Character newNodeLetter, Integer wordId, HashMap<Character, SuffixTreeNode> childNode) {
        letter = newNodeLetter;
        if (Objects.isNull(childNode)) {
            wordsId.add(wordId);
        } else {
            List<SuffixTreeNode> existingChilds
            childes.put(letter, childNode);
        }
    }

    public SuffixTreeNode addNode(Character letter, SuffixTreeNode childNode) {
        return childNode;
    }
}
