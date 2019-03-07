package com.fido.tro.data.indices.threading;

import com.fido.tro.Controller;
import com.fido.tro.data.Record;
import com.fido.tro.data.indices.Index;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;

public class InitialLineParsing implements Runnable {
    String line;
    String filePath;
    Long position;
    int filesCount;
    Analyzer analyzer = new StandardAnalyzer();

    public InitialLineParsing(int filesCount, String line, String filePath, Long position) {
        this.filesCount = filesCount;
        this.line = line;
        this.filePath = filePath;
        this.position = position;
    }

    @Override
    public void run() {
        try {
            TokenStream stream = analyzer.tokenStream(null, new StringReader(line));
            stream.reset();
            while (stream.incrementToken()) {
                String word = stream.getAttribute(CharTermAttribute.class).toString();
                Record record = new Record(word);
                record.addPosition(filePath, filesCount - 1, position);

                for (Index index : Controller.indices.values()) {
                    index.add(record, filesCount, filePath, position);
                }
            }
            stream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        analyzer.close();
        super.finalize();
    }
}