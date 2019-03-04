package com.fido.tro.data.indices.entity;

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
