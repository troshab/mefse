package com.fido.tro.data.indices;

import com.fido.tro.data.Entity;

import java.util.*;

public class Kgram extends Inverted {
    private int k = 2;
    private String emptiness;
    private Map<String, Set<String>> trigramsData = new HashMap<>();

    public Kgram(int Ksize) {
        k = Ksize;
        calculateEmptiness();
    }
    public Kgram() {
        calculateEmptiness();
    }

    private void calculateEmptiness() {
        StringBuilder newEmptiness = new StringBuilder();
        for(int i = 1; i < k; i++) {
            newEmptiness.append(" ");
        }
        emptiness = newEmptiness.toString();
    }

    @Override
    public String description() {
        return "trigrams in index variation of inverted index";
    }

    public void add(Entity entity, int fileCounter, String filePath, Long position) {
        List<String> trigrams = wordTrigrams(entity.getTerm());
        String word = entity.getTerm();
        for (String trigram : trigrams) {
            if (!trigramsData.containsKey(trigram)) {
                trigramsData.put(trigram, new HashSet<>());

            }
            trigramsData.get(trigram).add(word);

            super.add(entity, fileCounter, filePath, position);
        }
    }

    @Override
    List<String> preQuery(List<String> queryParts) {
        List<String> resultQueryParts = new ArrayList<>();
        for(String part : queryParts) {
            if (part.contains("*") && !getModes().contains(part)) {
                List<String> partsOfWord = Arrays.asList(part.split("\\*", -1));
                Set<String> relativesWordsGlobal = new HashSet<>();
                int partI = 1;
                for(String partOfWord : partsOfWord) {
                    if (!partOfWord.equals("")) {
                        if (partI > 0 && partI < (partsOfWord.size() - 1) && partOfWord.length() < k) {
                            System.err.println("Middle expressions in wildcard must be more than kgram size.");
                        } else {
                            List<String> pewNewRWG = findRelativeWords(partOfWord);
                            List<String> newRWG = new ArrayList<>();
                            if (partI == 1) {
                                for (String wt : pewNewRWG) {
                                    if (wt.substring(0, partOfWord.length()).equals(partOfWord)) {
                                        newRWG.add(wt);
                                    }
                                }
                            }
                            else if (partI == partsOfWord.size()) {
                                for (String wt : pewNewRWG) {
                                    if (wt.substring(wt.length() - partOfWord.length()).equals(partOfWord)) {
                                        newRWG.add(wt);
                                    }
                                }
                            } else {
                                newRWG.addAll(pewNewRWG);
                            }
                            if (relativesWordsGlobal.isEmpty()) {
                                relativesWordsGlobal.addAll(newRWG);
                            } else {
                                relativesWordsGlobal.retainAll(newRWG);
                            }
                        }
                    }
                    partI++;
                }
                if (relativesWordsGlobal.isEmpty()) {
                    System.err.println("Error: wildcard word '" + part + "' didn't find in kgram(" + k + ") index");
                    return null;
                } else {
                    boolean first = true;
                    for(String addWord : relativesWordsGlobal) {
                        if (first) {
                            first = false;
                        } else {
                            resultQueryParts.add("or");
                        }
                        resultQueryParts.add(addWord);
                    }
                    /*
                    Iterator<String> mostRelativeWordsIterator = relativesWordsGlobal.iterator();
                    if (mostRelativeWordsIterator.hasNext()) {
                        resultQueryParts.add(mostRelativeWordsIterator.next());
                        if (mostRelativeWordsIterator.hasNext()) {
                            resultQueryParts.add("or");
                        }
                    }*/
                    System.out.println("Wildcard word \"" + part + "\" replaced via kgram(" + k + ") as or-expression with: " + relativesWordsGlobal);
                }
            } else {
                resultQueryParts.add(part);
            }
        }
        return resultQueryParts;
    }

    List<String> findRelativeWords(String part) {
        List<String> trigrams = wordTrigrams(part);
        if (trigrams.isEmpty()) {
            System.err.println("Error: wrong query (can't repeat words in query)");
            return null;
        } else {
            List<String> results = new ArrayList<>();
            for (String trigram : trigrams) {
                Set<String> originalWords = trigramsData.get(trigram);
                if (Objects.nonNull(originalWords)) {
                    results.addAll(originalWords);
                }
            }
            return results;
        }
    }

    private List<String> wordTrigrams(final String word) {
        String parsedWord = word;
        List<String> results = new ArrayList<>();
        parsedWord = emptiness + parsedWord + emptiness;
        for (int beginIndex = 0, endIndex = k; beginIndex < parsedWord.length() - (k - 1); beginIndex++, endIndex++) {
            results.add(parsedWord.substring(beginIndex, endIndex));
        }
        return results;
    }
}
