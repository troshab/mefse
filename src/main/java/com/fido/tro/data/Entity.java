package com.fido.tro.data;

import com.fido.tro.Mefse;
import com.fido.tro.data.fields.Filepath;
import com.fido.tro.data.fields.FilepathWithPositions;
import com.fido.tro.data.fields.MatrixRow;
import com.fido.tro.data.fields.Term;

import java.io.Serializable;

public class Entity implements Serializable {
    private Term term;
    public String getTerm() {
        return term.getTerm();
    }

    private Filepath filepaths;
    Filepath getFilepaths() {
        return filepaths;
    }

    private FilepathWithPositions filepathsWithPositions;
    FilepathWithPositions getFilepathsWithPositions() {
        return filepathsWithPositions;
    }

    private MatrixRow matrixRow;
    public MatrixRow getMatrixRow() {
        return matrixRow;
    }

    Entity() {
        term = new Term();
        constructorAdditional();
    }

    public Entity(String word) {
        term = new Term(word);
        constructorAdditional();
    }

    private void constructorAdditional() {
        filepaths = new Filepath();
        filepathsWithPositions = new FilepathWithPositions();
        matrixRow = new MatrixRow();
    }

    public void addPosition(String filePath, int column, Long position) {
        filepathsWithPositions.get(filePath).add(position);

        matrixRow.set(column);
    }

    @Override
    protected void finalize() throws Throwable {
        if (Mefse.debug) {
            System.out.println("data.Record successfully garbage cleaned");
        }

        super.finalize();
    }
}
