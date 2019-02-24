package com.fido.tro.serializers;

import java.util.HashMap;
import java.util.Objects;

public class Serializer {
    private HashMap<String, AbstractSerializer> serializers;
    private AbstractSerializer serializer;

    public Serializer() {
        serializers = new HashMap<>();
    }

    public void add(AbstractSerializer newSerializer) {
        if (Objects.isNull(serializer))
            serializer = newSerializer;

        serializers.put(newSerializer.getClass().getSimpleName(), newSerializer);
    }

    public AbstractSerializer getSerializer() {
        return serializer;
    }
}
