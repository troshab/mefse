package com.fido.tro.data.indices;

import com.fido.tro.data.fields.FilepathWithPositions;
import com.fido.tro.data.fields.Positions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coordinated extends Inverted {
    protected Map<String, FilepathWithPositions> data = new HashMap<>();

    @Override
    public void list() {
        if (data.isEmpty()) {
            System.err.println("Indexes database is empty!");
            return;
        }
        for (Map.Entry<String, FilepathWithPositions> entry : data.entrySet()) {
            String word = entry.getKey();
            FilepathWithPositions paths = entry.getValue();
            System.out.println("Word '" + word + "':");
            for (Map.Entry<String, Positions> pathEntry : paths.entrySet()) {
                String path = pathEntry.getKey();
                List<Long> positions = pathEntry.getValue().getPositions();
                StringBuilder positionsString = new StringBuilder();
                for (Long position : positions) {
                    positionsString.append(position).append(", ");
                }
                System.out.println(" => " + path + ": " + positionsString.substring(0, positionsString.length() - 2));
            }
        }
    }

    @Override
    public String description() {
        return "variation of inverted index with positions with what words appears in books";
    }

    @Override
    public boolean isSearchable(String query) {
        return false;
    }
}
