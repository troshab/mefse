package com.fido.tro.serializer;

import com.fido.tro.db.Table;
import com.fido.tro.engine.EngineBase;
import org.apache.log4j.Logger;
import org.reflections.Reflections;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Serializer {
    private final static Logger LOGGER = Logger.getLogger(Serializer.class);

    private static List<SerializerBase> serializers = new LinkedList<>();
    private static List<String> serializerNames = new LinkedList<>();

    public static void loadSerializers() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Serializer initialization: ");
        }
        try {
            Reflections reflections = new Reflections(SerializerBase.class);
            Set<Class<? extends EngineBase>> serializersSet = reflections.getSubTypesOf(EngineBase.class);
            for(Class serializerClass : serializersSet) {
                SerializerBase serializer = (SerializerBase) serializerClass.newInstance();
                serializers.add(serializer);
                serializerNames.add(serializer.getClass().getSimpleName().toLowerCase());
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("+ " + serializerClass.getSimpleName() + ": " + serializer.description());
                }
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("--- DONE ---");
            }
        } catch (Exception exception) {
            LOGGER.fatal("Error during loading serializations", exception);
        }
    }
}
