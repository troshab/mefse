package com.fido.tro;

import com.fido.tro.data.Storage;
import com.fido.tro.data.Entity;
import com.fido.tro.data.indices.*;
import com.fido.tro.data.indices.Dictionary;
import com.fido.tro.serializers.*;
import com.fido.tro.utils.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Controller {
    private Storage db = new Storage();
    private Serializer serializer = new Serializer();
    private Map<String, Index> indices = new HashMap<>();

    Controller() {
        indices.put("matrix", new Matrix());
        indices.put("coordinated", new Coordinated());
        indices.put("dictionary", new Dictionary());
        indices.put("inverted", new Inverted());
        indices.put("twoword", new TwoWord());

        serializer.add(new Kryo());
        serializer.add(new POJO());
        serializer.add(new Json());
        serializer.add(new XML());
    }

    void populate(String filename) {
        List<String> filepaths = FileUtils.getPaths(filename);

        Analyzer analyzer = new StandardAnalyzer();
        for(String filepath : filepaths) {
            Stream<String> lines = FileUtils.linesStream(filepath);
            AtomicLong wordPosition = new AtomicLong(0L);
            Objects.requireNonNull(lines).forEach(line -> {
                try {
                    TokenStream stream = analyzer.tokenStream(null, new StringReader(line));
                    stream.reset();
                    while (stream.incrementToken()) {
                        add(stream.getAttribute(CharTermAttribute.class).toString(), filepath, wordPosition.getAndIncrement());
                    }
                    stream.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
        }
        System.out.println("populated");
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("data.data successfully garbage cleaned");
        super.finalize();
    }

    public void add(String word, String filePath, Long position) {
        Entity record;

        record = db.data.get(word);
        if (Objects.isNull(record)) {
            record = new Entity(word);
        }

        record.addPosition(filePath, db.filesCounter, position);
        db.data.put(word, record);
        addToIndices(record, db.filesCounter++, filePath, position);
    }

    private void addToIndices(Entity record, Integer filesCounter, String filePath, Long position) {
        for(Index index : indices.values())
            index.add(record, filesCounter, filePath, position);
    }

    void find(String engineType, String query) {
        Index index = indices.get(engineType);
        if (Objects.nonNull(index)) {
            index.isSearchable(query);
        } else {
            System.err.println("engine " + engineType + " not found");
        }
    }

    void listData(String engineType) {
        Index engine = indices.get(engineType);
        if (Objects.nonNull(engine)) {
            engine.list();
        } else {
            System.err.println("engine " + engineType + " not found");
        }
    }

    void load(String filename) {
    }

    void save(String filename) {
        db.save(filename, serializer);
    }

    String listSerializers() {
        return null;
    }

    void setSerializerEngine(String commandArgument) {
    }
}
