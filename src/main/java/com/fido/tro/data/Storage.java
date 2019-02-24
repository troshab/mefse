package com.fido.tro.data;

import com.fido.tro.serializers.Serializer;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Storage implements Serializable {
    public Map<String, Entity> data = new ConcurrentHashMap<>();
    Map<Integer, String> files = new ConcurrentHashMap<>();
    public Integer filesCounter = 0;

    public Storage() {}

    @Override
    protected void finalize() throws Throwable {
        System.out.println("data.Table successfully garbage cleaned");
        super.finalize();
    }

    public void save(String filename, Serializer serializer) {
        serializer.getSerializer().save(filename, data);
    }
}
