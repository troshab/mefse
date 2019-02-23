package com.fido.tro.engine;

import com.fido.tro.db.Record;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Arrays;

public abstract class EngineBase implements Serializable {
    private final static Logger LOGGER = Logger.getLogger(EngineBase.class);

    public abstract String description();
    public abstract void add(Record record, Integer fileCounter, String filePath, Long position);
    public abstract void list();
    public abstract boolean isSearchable();

    public void initSearch(String queryPart, boolean invertArray) {}
    public void or(String queryPart, boolean invertArray) {}
    public void and(String queryPart, boolean invertArray) {}
    public void searchResult() {

    }

    boolean search(String condition) {
        String[] queryParts = condition.replaceAll("\\s+", " ").replaceAll("!\\s*", "!").split(" ");
        return search(queryParts);
    }

    protected boolean search(String[] queryParts) {
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
                        LOGGER.error("Error: wrong query (can't repeat logical operators in query)");
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
                        LOGGER.error("Error: wrong query (can't repeat words in query)");
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
