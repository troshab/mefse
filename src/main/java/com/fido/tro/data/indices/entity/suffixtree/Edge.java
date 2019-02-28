package com.fido.tro.data.indices.entity.suffixtree;

class Edge {
    private String label;
    private Node dest;

    String getLabel() {
        return label;
    }

    void setLabel(String label) {
        this.label = label;
    }

    Node getDest() {
        return dest;
    }

    Edge(String label, Node dest) {
        this.label = label;
        this.dest = dest;
    }
}
