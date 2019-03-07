package com.fido.tro.data.indices;

import com.fido.tro.data.Record;
import com.fido.tro.data.indices.entities.Filepath;
import com.fido.tro.data.indices.entities.SuffixTreeNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SuffixTree extends Inverted {
    private Map<String, Filepath> data = new HashMap<>();
    private SuffixTreeNode tree = new SuffixTreeNode();

    public void add(Record record, int fileCounter, String filePath, Long position) {
        String word = record.getTerm();

        synchronized (data) {
            if (!data.containsKey(word)) {
                synchronized (tree) {
                    SuffixTreeNode currentNode = tree;
                    SuffixTreeNode childNode;
                    for (int charIndex = 0; charIndex < word.length(); charIndex++) {
                        childNode = currentNode.getNode(word.charAt(charIndex));
                        if (Objects.isNull(childNode)) {
                            childNode = new SuffixTreeNode();
                            currentNode.putNode(word.charAt(charIndex), childNode);
                        }
                        currentNode = currentNode.getNode(word.charAt(charIndex));
                    }
                    currentNode.putNode('\0', null);
                }
            }
        }

        super.add(record, fileCounter, filePath, position);
    }

    /*@Override
    List<String> preQuery(List<String> queryParts) {
        List<String> resultQueryParts = new ArrayList<>();
        for(String part : queryParts) {
            if (part.contains("*") && !getModes().contains(part)) {
                List<String> partsOfWord = Arrays.asList(part.split("\\*", -1));
                List<String> relativesWordsGlobal = new ArrayList<>();
                int partI = 1;
                for (String partOfWord : partsOfWord) {
                    if (!partOfWord.equals("")) {
                        Collection<Integer> results = data.search(partOfWord);
                        Set<String> possibleWords = new HashSet<>();
                        for (Integer resultIndex : results) {
                           String wt = indexToWord.get(resultIndex);
                            if (partI == 1) {
                                    if (wt.length() >= partOfWord.length() && wt.substring(0, partOfWord.length()).equals(partOfWord)) {
                                        possibleWords.add(wt);
                                    }
                            }
                            else if (partI == partsOfWord.size()) {
                                    if (wt.length() >= partOfWord.length() && wt.substring(wt.length() - partOfWord.length()).equals(partOfWord)) {
                                        possibleWords.add(wt);
                                    }
                            } else {
                                possibleWords.add(wt);
                            }
                        }
                        if (relativesWordsGlobal.isEmpty()) {
                            relativesWordsGlobal.addAll(possibleWords);
                        } else {
                            relativesWordsGlobal.retainAll(possibleWords);
                        }
                    }
                    partI++;
                }
                if (relativesWordsGlobal.isEmpty()) {
                    System.err.println("Error: wildcard word '" + part + "' didn't find in suffixtree index");
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
                    }
                    *
                    System.out.println("Wildcard word \"" + part + "\" replaced via suffixtree as or-expression with: " + relativesWordsGlobal);
                }
            } else {
                resultQueryParts.add(part);
            }
        }
        return resultQueryParts;
    }*/
}
