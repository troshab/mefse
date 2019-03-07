package com.fido.tro;

import com.fido.tro.data.indices.Index;
import com.fido.tro.data.indices.SPIMI;
import com.fido.tro.data.indices.threading.CustomThreadPool;
import com.fido.tro.data.indices.threading.InitialLineParsing;
import com.fido.tro.serializers.*;
import com.fido.tro.utils.FileUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Controller {
    public static Serializer serializer = new Serializer();

    private int filesCount = 0;
    public static Map<String, Index> indices = new HashMap<>();
    private int processorsCount = Runtime.getRuntime().availableProcessors();

    Controller() {
        serializer.add(new Kryo());
        serializer.add(new POJO());
        serializer.add(new Json());
        serializer.add(new XML());

        /*indices.put("matrix", new Matrix());
        indices.put("coordinated", new Coordinated());
        indices.put("dictionary", new Dictionary());
        indices.put("inverted", new Inverted());
        indices.put("twoword", new TwoWord());*/
        //indices.put("kgram", new Kgram(2));
        //indices.put("suffixtree", new SuffixTree());
        indices.put("spimi", new SPIMI(serializer));
    }

    void populate(String filename) {
        List<String> filepath = FileUtils.getPaths(filename);
        CustomThreadPool customThreadPool = new CustomThreadPool(processorsCount);
        for (String oneFilepath : filepath) {
            filesCount++;
            Stream<String> lines = FileUtils.linesStream(oneFilepath);
            Long wordPosition = 0L;
            Objects.requireNonNull(lines).forEach(line -> {
                InitialLineParsing initialLineParsing = new InitialLineParsing(filesCount, line, oneFilepath, wordPosition);
                do {
                } while (customThreadPool.queue.size() > processorsCount);
                customThreadPool.execute(initialLineParsing);
                //initialLineParsing.run();
            });
            System.out.println("Added file " + oneFilepath);
        }
        customThreadPool.shutdown();
        System.out.println("Populated");
    }

    void find(String engineType, String query) {
        Index index = indices.get(engineType);
        if (Objects.nonNull(index)) {
            System.out.println("Search query: " + query);
            if (!index.isSearchable(query)) {
                System.err.println("Search in " + engineType + " not implemented");
            }
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
        //storage.load(filename, serializer);
    }

    void save(String filename) {
        //storage.save(filename, serializer);
    }

    String listSerializers() {
        return null;
    }

    void setSerializerEngine(String commandArgument) {
    }
}
