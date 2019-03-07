package com.fido.tro.data.indices.entities;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Filepath implements Iterable<String> {
    private Set<String> filepath = new LinkedHashSet<>();

    public Filepath() {
    }

    public Filepath(Filepath value) {
        filepath.addAll(value.getFilepath());
    }

    public void addAll(Filepath addFilepath) {
        filepath.addAll(addFilepath.getFilepath());
    }

    public void addAll(Set<String> filepaths) {
        filepath.addAll(filepaths);
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
    @Nonnull
    public Iterator<String> iterator() {
        return filepath.iterator();
    }

    public Set<String> allFilepaths() {
        return this.filepath;
    }
}
