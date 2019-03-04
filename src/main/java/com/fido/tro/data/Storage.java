package com.fido.tro.data;

import com.fido.tro.serializers.Serializer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Storage implements Serializable {
    public Map<String, Entity> data = new HashMap<>();
    public Set<String> files = new LinkedHashSet<>();

    public Storage() {
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("data.Table successfully garbage cleaned");
        super.finalize();
    }

    public void save(String filename, Serializer serializer) {
        serializer.getSerializer().save(filename, data);
    }

    public void load(String filename, Serializer serializer) {
        Map<String, Entity> loadData = new HashMap<>();
        serializer.getSerializer().load(filename, loadData);
    }
}
