package com.fido.tro.data.indices;

import java.util.Arrays;

public abstract class Searchable implements Index {
    abstract void initSearch(String queryPart, boolean invertArray);
    abstract void or(String queryPart, boolean invertArray);
    abstract void and(String queryPart, boolean invertArray);
    abstract void searchResult();

    public boolean search(String condition) {
        String[] queryParts = condition.replaceAll("\\s+", " ").replaceAll("!\\s*", "!").split(" ");
        return search(queryParts);
    }

    private boolean search(String[] queryParts) {
        String mode = "start";
        String[] modes = {"start", "and", "or"};

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
                        return false;
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
                    if (Arrays.stream(modes).noneMatch(queryPart::equals)) {
                        System.err.println("Error: wrong query (can't repeat words in query)");
                        return false;
                    }

                    mode = queryPart.toLowerCase();
                    break;
            }
        }

        searchResult();

        return true;
    }
}
