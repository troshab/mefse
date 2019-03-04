package com.fido.tro.serializers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public abstract class AbstractSerializer {
    public abstract String description();

    public abstract Object loadObject(FileInputStream fis, Class objectClass);

    public abstract boolean saveObject(FileOutputStream fos, Object object);

    public Object load(String filePath, Object object) {
        return load(filePath, object, true);
    }

    public Object load(String filePath, Object object, boolean verbose) {
        Object loadedObject = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            loadedObject = loadObject(fis, object.getClass());
            fis.close();
            if (Objects.nonNull(loadedObject) && verbose)
                System.out.println("Loaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadedObject;
    }

    public void save(String filePath, Object object) {
        save(filePath, object, true);
    }

    public void save(String filePath, Object object, boolean verbose) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            if (saveObject(fos, object) && verbose) {
                System.out.println("Saved successfully to " + filePath);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
