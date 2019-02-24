package com.fido.tro.data;

import com.fido.tro.Mefse;
import com.fido.tro.data.fields.Filepath;
import com.fido.tro.data.fields.FilepathWithPositions;
import com.fido.tro.data.fields.MatrixRow;
import com.fido.tro.data.fields.Term;

import java.io.Serializable;

public class Entity implements Serializable {
    private Term term;
    private Filepath filepaths;
    private FilepathWithPositions filepathsWithPositions;
    private MatrixRow matrixRow;

    public Entity(String word) {
        term = new Term(word);
        constructorAdditional();
    }

    public String getTerm() {
        return term.getTerm();
    }

    Filepath getFilepaths() {
        return filepaths;
    }

    FilepathWithPositions getFilepathsWithPositions() {
        return filepathsWithPositions;
    }

    public MatrixRow getMatrixRow() {
        return matrixRow;
    }

    private void constructorAdditional() {
        filepaths = new Filepath();
        filepathsWithPositions = new FilepathWithPositions();
        matrixRow = new MatrixRow();
    }

    public void addPosition(String filePath, int column, Long position) {
        filepaths.add(filePath);
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
