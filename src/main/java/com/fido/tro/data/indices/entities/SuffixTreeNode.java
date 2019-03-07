package com.fido.tro.data.indices.entities;

import java.util.*;

public class SuffixTreeNode {
    private HashMap<Character, SuffixTreeNode> childs = new HashMap<>();

    public SuffixTreeNode getNode(Character character) {
        return childs.get(character);
    }

    public void putNode(char charAt, SuffixTreeNode childNode) {
        childs.put(charAt, childNode);
    }
}
