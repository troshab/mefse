package com.fido.tro.DB;

import com.fido.tro.Engine.EngineBase;
import com.fido.tro.Mefse;
import com.fido.tro.Serializer.type.Java;
import com.fido.tro.Serializer.SerializerBase;
import org.reflections.Reflections;

import java.util.*;

public class DB {
    private static Table dbEngine = new Table();
    private List<EngineBase> engines = new LinkedList<>();

    private static SerializerBase serializer = new Java();

    public DB() {
        Mefse.logger.info("DB engines initialization: ");
        try {
            Reflections reflections = new Reflections(EngineBase.class);
            Set<Class<? extends EngineBase>> enginesSet = reflections.getSubTypesOf(EngineBase.class);

            for(Class engineClass : enginesSet) {
                EngineBase engine = (EngineBase) engineClass.newInstance();
                engines.add(engine);
                Mefse.logger.info("+ " + engineClass.getSimpleName() + ": " + engine.description());
            }

            Mefse.logger.info("--- DONE ---");
        } catch (Exception e) {
            Mefse.logger.info("!!! FAILED !!!");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void addFile(String filePath) {
        for (Map.Entry<String, Record> stringRecordEntry : dbEngine.db.entrySet())
            stringRecordEntry.getValue().increaseMatrix(dbEngine.filesCounter);

        dbEngine.files.put(dbEngine.filesCounter, filePath);
    }

    public void add(String word, String filePath, Long position) {
        Record record;

        record = dbEngine.db.get(word);
        if (Objects.isNull(record)) {
            record = new Record(word);
        }

        record.addPosition(filePath, dbEngine.filesCounter, position);
        dbEngine.db.put(word, record);
        addToEngines(record, dbEngine.filesCounter, filePath, position);
    }

    private void addToEngines(Record record, Integer fileCounter, String filePath, Long position) {
        for (EngineBase engine : engines) engine.add(record, fileCounter, filePath, position);
    }

    void list() {
        for (EngineBase engine : engines) {
            Mefse.logger.info(engine.getClass().getSimpleName());
            engine.list();
        }
        Mefse.logger.info("point");
    }

    public void incrementFileCounter() {
        dbEngine.filesCounter++;
    }

    public void find(String searchQuery) {
        for (EngineBase engine : engines) {
            if (engine.isSearchable()) {
                Mefse.logger.info(engine.getClass().getSimpleName());
                engine.search(searchQuery);
            }
        }
    }

    // @TODO: fix matrix loading шкіра and земля
    void load(String filePath) {
        long startTime = System.currentTimeMillis();
        Object object = serializer.load(filePath, dbEngine);

        if (object instanceof Table) {
            dbEngine = new Table();
            Mefse.parsedFiles = new LinkedHashSet<>();
            dbEngine = (Table) object;
            System.gc();

            for (Record record : dbEngine.db.values())
                for (Map.Entry<String, Set<Long>> path : record.getPaths().entrySet()) {
                    for (Long position : path.getValue()) {
                        add(record.getWord(), path.getKey(), position);
                    }

                    Mefse.parsedFiles.add(path.getKey());
                }
        }
        Mefse.timeTaken(startTime);
    }

    void save(String filePath) {
        long startTime = System.currentTimeMillis();
        serializer.save(filePath, dbEngine);
        Mefse.timeTaken(startTime);
    }

    @Override
    protected void finalize() throws Throwable {
        Mefse.logger.info("DB.DB successfully garbage cleaned");
        super.finalize();
    }
}
