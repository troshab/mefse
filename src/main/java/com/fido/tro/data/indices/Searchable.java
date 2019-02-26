package com.fido.tro.data.indices;

import java.util.Arrays;
import java.util.List;

abstract class Searchable implements Index {
    private List<String> modes = Arrays.asList("start", "and", "or");
    List<String> getModes() {
        return modes;
    }

    abstract void initSearch(String queryPart, boolean invertArray);

    abstract void or(String queryPart, boolean invertArray);

    abstract void and(String queryPart, boolean invertArray);

    abstract void searchResult();

    void search(String condition) {
        List<String> queryParts = Arrays.asList(condition.replaceAll("\\s+", " ").replaceAll("!\\s*", "!").split(" "));
        queryParts = preQuery(queryParts);
        search(queryParts);
    }
    List<String> preQuery(List<String> queryParts) {
        return queryParts;
    }

    private void search(List<String> queryParts) {
        String mode = "start";

        for (String queryPart : queryParts) {
            switch (mode) {
                case "start":
                case "and":
                case "or":
                    boolean invertArray = false;
                    if ("!".equals(queryPart.substring(0, 1))) {
                        queryPart = queryPart.substring(1);
                        invertArray = true;
                    }
                    if (Arrays.asList(modes).contains(queryPart)) {
                        System.err.println("Error: wrong query (can't repeat logical operators in query)");
                        return;
                    }
                    switch (mode) {
                        case "start":
                            initSearch(queryPart, invertArray);
                            break;

                        case "and":
                            and(queryPart, invertArray);
                            break;

                        case "or":
                            or(queryPart, invertArray);
                            break;
                    }
                    mode = "";
                    break;

                default:
                    if (!modes.contains(queryPart)) {
                        System.err.println("Error: wrong query (can't repeat words in query)");
                        return;
                    }
                    mode = queryPart.toLowerCase();
                    break;
            }
        }
        searchResult();
    }
}
