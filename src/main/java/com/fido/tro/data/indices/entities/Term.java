package com.fido.tro.data.indices.entities;

public class Term {
    private String term;

    public Term(String constructorTerm) {
        term = constructorTerm;
    }

    public String getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return term;
    }
}
