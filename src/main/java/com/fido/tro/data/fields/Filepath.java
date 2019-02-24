package com.fido.tro.data.fields;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Filepath implements Iterable<String> {
    private Set<String> filepath;

    public Filepath() {
        filepath = new LinkedHashSet<>();
    }

    public void addAll(Filepath addFilepath) {
        filepath.addAll(addFilepath.getFilepath());
    }

    private Set<String> getFilepath() {
        return filepath;
    }

    public void removeAll(Filepath removeFilepath) {
        filepath.removeAll(removeFilepath.getFilepath());
    }

    public void add(String newFilepath) {
        filepath.add(newFilepath);
    }

    public void retainAll(Filepath retainFilepath) {
        filepath.retainAll(retainFilepath.getFilepath());
    }

    @Override
    public Iterator<String> iterator() {
        return filepath.iterator();
    }
}
