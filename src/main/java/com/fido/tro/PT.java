package com.fido.tro;

import com.fido.tro.cli.CliBenchmark;
import com.fido.tro.utils.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

public class PT {
    private static String filePath = "d:\\tester.txt";

    static void createFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            for (int i = 1; i <= 20; i++) {
                writer.write(String.valueOf(i));
                if (i != 20) writer.write('\n');
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static Analyzer analyzer = new StandardAnalyzer();

    private static String[] tokenizeLine(String line) {
        String[] resultArray = new String[0];
        try {

            TokenStream stream = analyzer.tokenStream(null, new StringReader(line));
            stream.reset();
            List<String> result = new LinkedList<>();
            while (stream.incrementToken()) {
                result.add(stream.getAttribute(CharTermAttribute.class).toString());
            }
            stream.close();
            resultArray = new String[result.size()];
            result.toArray(resultArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultArray;
    }
    public static void main(final String[] args) {
        //createFile();
        try {

            Files.readAllLines(Paths.get(filePath))
                    .parallelStream()
                    .map(PT::tokenizeLine)
                    .flatMap(Arrays::stream)
                    .parallel()
                    .sorted()
                    .forEachOrdered(System.out::println);
        } catch (Exception ignored) {}
            /*
            Map<String, LongAdder> wordCounts = new ConcurrentHashMap<>();
            Map<String, LongAdder> wordCounts2 = new ConcurrentHashMap<>();
            Map<String, LongAdder> wordCounts3 = new ConcurrentHashMap<>();
            Map<String, LongAdder> wordCounts4 = new HashMap<>();

            System.out.println("WARMING UP");
            CliBenchmark cliBenchmark = new CliBenchmark(true);
            Files.readAllLines(Paths.get(filePath))
                    .parallelStream()
                    .map(line -> line.split("\\s+"))
                    .flatMap(Arrays::stream)
                    .parallel()
                    .forEach(word -> {
                        Math.pow(Math.random() * 100, Math.random() * 100);
                    });
            cliBenchmark.timeTaken();
            System.out.println("parallel concurent");
            cliBenchmark = new CliBenchmark(true);
            Files.readAllLines(Paths.get(filePath))
                    .parallelStream()
                    .map(line -> {
                        String[] resultArray = new String[0];
                        try {

                            TokenStream stream = analyzer.tokenStream(null, new StringReader(line));
                            stream.reset();
                            List<String> result = new LinkedList<>();
                            while (stream.incrementToken()) {
                                result.add(stream.getAttribute(CharTermAttribute.class).toString());
                            }
                            stream.close();
                            resultArray = new String[result.size()];
                            result.toArray(resultArray);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return resultArray;
                    })
                    .flatMap(Arrays::stream)
                    .parallel()
                    .forEach(word -> {                              // Use an AtomicAdder to tally word counts
                        if (!wordCounts.containsKey(word))          // If a hashmap entry for the word doesn't exist yet
                            wordCounts.put(word, new LongAdder());  // Create a new LongAdder
                        wordCounts.get(word).increment();           // Increment the LongAdder for each instance of a word
                    });
            long first = cliBenchmark.timeTaken();
            System.out.println("parallel non concurent");
            cliBenchmark = new CliBenchmark(true);
            Files.readAllLines(Paths.get(filePath))
                    .parallelStream()
                    .map(line -> {
                        String[] resultArray = new String[0];
                        try {

                            TokenStream stream = analyzer.tokenStream(null, new StringReader(line));
                            stream.reset();
                            List<String> result = new LinkedList<>();
                            while (stream.incrementToken()) {
                                result.add(stream.getAttribute(CharTermAttribute.class).toString());
                            }
                            stream.close();
                            resultArray = new String[result.size()];
                            result.toArray(resultArray);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return resultArray;
                    })
                    .flatMap(Arrays::stream)
                    .parallel()
                    .forEach(word -> {                              // Use an AtomicAdder to tally word counts
                        if (!wordCounts2.containsKey(word))          // If a hashmap entry for the word doesn't exist yet
                            wordCounts2.put(word, new LongAdder());  // Create a new LongAdder
                        wordCounts2.get(word).increment();           // Increment the LongAdder for each instance of a word
                    });
            long second = cliBenchmark.timeTaken();

            System.out.println("non parallel concurent");
            cliBenchmark = new CliBenchmark(true);

            FileUtils.linesStream(filePath).forEach(line -> {
                try {
                    TokenStream stream = analyzer.tokenStream(null, new StringReader(line));
                    stream.reset();
                    while (stream.incrementToken()) {
                        String word = stream.getAttribute(CharTermAttribute.class).toString();
                        if (!wordCounts3.containsKey(word))          // If a hashmap entry for the word doesn't exist yet
                            wordCounts3.put(word, new LongAdder());  // Create a new LongAdder
                        wordCounts3.get(word).increment();
                    }
                    stream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            long third = cliBenchmark.timeTaken();

            System.out.println("non parallel non concurent");
            cliBenchmark = new CliBenchmark(true);

            FileUtils.linesStream(filePath).forEach(line -> {
                try {
                    TokenStream stream = analyzer.tokenStream(null, new StringReader(line));
                    stream.reset();
                    while (stream.incrementToken()) {
                        String word = stream.getAttribute(CharTermAttribute.class).toString();
                        if (Objects.isNull(wordCounts4.get(word)))          // If a hashmap entry for the word doesn't exist yet
                            wordCounts4.put(word, new LongAdder());  // Create a new LongAdder
                        wordCounts4.get(word).increment();
                    }
                    stream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
            long fourth = cliBenchmark.timeTaken();

            System.out.println("first: " + first);
            System.out.println("second: " + second);
            System.out.println("third: " + third);
            System.out.println("fourth: " + fourth);
            System.out.println("K wordCounts.equals(wordCounts2): " + wordCounts.keySet().equals(wordCounts2.keySet()));
            System.out.println("K wordCounts.equals(wordCounts3): " + wordCounts.keySet().equals(wordCounts3.keySet()));
            System.out.println("K wordCounts.equals(wordCounts4): " + wordCounts.keySet().equals(wordCounts4.keySet()));
            System.out.println("K wordCounts2.equals(wordCounts3): " + wordCounts2.keySet().equals(wordCounts3.keySet()));
            System.out.println("K wordCounts2.equals(wordCounts4): " + wordCounts2.keySet().equals(wordCounts4.keySet()));
            System.out.println("K wordCounts3.equals(wordCounts4): " + wordCounts3.keySet().equals(wordCounts4.keySet()));
            System.out.println("done");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}
