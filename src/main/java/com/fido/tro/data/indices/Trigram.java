package com.fido.tro.data.indices;

import com.fido.tro.data.Entity;
import com.fido.tro.data.fields.Filepath;

import java.util.*;

public class Trigram extends Inverted {
    private Map<String, HashSet<String>> trigramsData = new HashMap<>();

    @Override
    public String description() {
        return "trigrams in index variation of inverted index";
    }

    public void add(Entity entity, int fileCounter, String filePath, Long position) {
        Set<String> trigrams = wordTrigrams(entity.getTerm());
        String word = entity.getTerm();
        for (String trigram : trigrams) {
            if (!trigramsData.containsKey(trigram)) {
                trigramsData.put(trigram, new HashSet<>());
            }

            Set<String> words = trigramsData.get(trigram);
            words.add(word);

            super.add(entity, fileCounter, filePath, position);
        }
    }

    protected Filepath findSetForWord(String word, boolean invertArray) {
        Filepath queryPartSet = data.get(word);
        if (Objects.isNull(queryPartSet)) {
            Set<String> trigrams = wordTrigrams(word);
            if (!trigrams.isEmpty()) {
                Map<String, Integer> relativeWords = new HashMap<>();
                for (String trigramKey : trigrams) {
                    Set<String> words = trigramsData.get(trigramKey);
                    if (Objects.nonNull(words)) {
                        for (String wordKey : words) {
                            Integer value = relativeWords.get(wordKey);
                            if (Objects.isNull(value)) {
                                relativeWords.put(wordKey, 1);
                            } else {
                                relativeWords.put(wordKey, value + 1);
                            }
                        }
                    }
                }
                Integer maxValue = 0;
                String mostRelatedWord = "";
                for (Map.Entry<String, Integer> relativeWord : relativeWords.entrySet()) {
                    Integer value = relativeWord.getValue();
                    if (value > maxValue) {
                        maxValue = value;
                        mostRelatedWord = relativeWord.getKey();
                    }
                }
                System.err.println("Maybe mistyped in word \"" + word + "\" changed to " + mostRelatedWord);
                return findSetForWord(mostRelatedWord, invertArray);
            }
            System.err.println("Error: word '" + word + "' didn't find in inverted index");
            return null;
        }

        if (invertArray) {
            Filepath queryAllSet = getAllFilepath();
            queryAllSet.removeAll(queryPartSet);
            queryPartSet = queryAllSet;
        }
        return queryPartSet;
    }

    private Set<String> wordTrigrams(String word) {
        Set<String> results = new HashSet<>();
        word = "  " + word + "  ";
        for (int beginIndex = 0, endIndex = beginIndex + 3; beginIndex < word.length() - 2; beginIndex++, endIndex++) {
            results.add(word.substring(beginIndex, endIndex));
        }
        return results;
    }
}
