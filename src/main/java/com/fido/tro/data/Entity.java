package com.fido.tro.data;

import com.fido.tro.Mefse;
import com.fido.tro.data.fields.Filepath;
import com.fido.tro.data.fields.FilepathWithPositions;
import com.fido.tro.data.fields.MatrixRow;
import com.fido.tro.data.fields.Term;

import java.io.Serializable;

public class Entity implements Serializable {
    private Term term;
    private Filepath filepath;
    private FilepathWithPositions filepathWithPositions;
    private MatrixRow matrixRow;

    public Entity(String word) {
        term = new Term(word);
        constructorAdditional();
    }

    public String getTerm() {
        return term.getTerm();
    }

    public MatrixRow getMatrixRow() {
        return matrixRow;
    }

    private void constructorAdditional() {
        filepath = new Filepath();
        filepathWithPositions = new FilepathWithPositions();
        matrixRow = new MatrixRow();
    }

    public void addPosition(String filePath, int column, Long position) {
        filepath.add(filePath);
        filepathWithPositions.get(filePath).add(position);
        matrixRow.set(column);
    }
}
