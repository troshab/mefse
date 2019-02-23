package com.fido.tro.engine;

import com.fido.tro.db.Record;
import org.apache.log4j.Logger;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Engine {
    private final static Logger LOGGER = Logger.getLogger(Engine.class);

    private static Map<String, EngineBase> engines = new HashMap<>();

    public static void loadEngines() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Engines initialization: ");
        }
        try {
            Reflections reflections = new Reflections(EngineBase.class);
            Set<Class<? extends EngineBase>> enginesSet = reflections.getSubTypesOf(EngineBase.class);

            for(Class engineClass : enginesSet) {
                EngineBase engine = (EngineBase) engineClass.newInstance();
                String engineName = engine.getClass().getSimpleName().toLowerCase();
                engines.put(engineName, engine);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("+ " + engineName + ": " + engine.description());
                }
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("--- DONE ---");
            }
        } catch (Exception exception) {
            LOGGER.fatal("Error during loading engines", exception);
        }
    }

    public static void add(Record record, Integer filesCounter, String filePath, Long position) {
        for(EngineBase engine : engines.values())
            engine.add(record, filesCounter, filePath, position);
    }

    public static void find(String engineType, String query) {
        EngineBase engine = engines.get(engineType);
        if (Objects.nonNull(engine)) {
            engine.search(query);
        } else {
            LOGGER.error("engine " + engineType + " not found");
        }
    }

    public static void listData(String engineType) {
        EngineBase engine = engines.get(engineType);
        if (Objects.nonNull(engine)) {
            engine.list();
        } else {
            LOGGER.error("engine " + engineType + " not found");
        }
    }
}
