package com.fido.tro.data.indices.entities;

public class FilepathWithId extends Filepath {
    private Integer id;

    public FilepathWithId(int wordIndex) {
        super();
        id = wordIndex;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
