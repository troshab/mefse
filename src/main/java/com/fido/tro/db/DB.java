package com.fido.tro.db;

import com.fido.tro.cli.CliBenchmark;
import com.fido.tro.engine.Engine;
import com.fido.tro.serializer.Serializer;
import com.fido.tro.utils.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class DB {
    private final static Logger LOGGER = Logger.getLogger(DB.class);

    private static Table dbEngine = new Table();

    public static void populate(String filename) {
        List<String> filepaths = FileUtils.getPaths(filename);
        Analyzer analyzer = new StandardAnalyzer();
        for(String filepath : filepaths) {
            Stream<String> lines = FileUtils.linesStream(filepath);
            Objects.requireNonNull(lines).forEach(line -> {
                try {
                    long wordCounter = 0L;
                    TokenStream stream = analyzer.tokenStream(null, new StringReader(line));
                    stream.reset();
                    while (stream.incrementToken()) {
                        add(stream.getAttribute(CharTermAttribute.class).toString(), filepath, wordCounter++);
                    }
                    stream.close();
                } catch (IOException exception) {
                    LOGGER.error("Error during addign word", exception);
                }
            });
        }
    }

    public static void add(String word, String filePath, Long position) {
        Record record;

        record = dbEngine.db.get(word);
        if (Objects.isNull(record)) {
            record = new Record(word);
        }

        record.addPosition(filePath, dbEngine.filesCounter, position);
        dbEngine.db.put(word, record);
        Engine.add(record, dbEngine.filesCounter++, filePath, position);
    }

    @Override
    protected void finalize() throws Throwable {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("db.db successfully garbage cleaned");
        }
        super.finalize();
    }
}
