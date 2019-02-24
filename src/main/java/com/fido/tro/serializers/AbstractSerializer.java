package com.fido.tro.serializers;

import com.fido.tro.utils.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public abstract class AbstractSerializer {
    public abstract String description();
    public abstract Object loadObject(FileInputStream fis, Class objectClass);
    public abstract boolean saveObject(FileOutputStream fos, Object object);

    public Object load(String filePath, Object object) {
        Class objectClass = object.getClass();
        String simpleName = objectClass.getSimpleName();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            Object loadedObject = loadObject(fis, objectClass);
            fis.close();
            if (Objects.nonNull(loadedObject))
                System.out.println(StringUtils.ucfirst(simpleName) + " loaded successfully!");
            else
            return loadedObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String filePath, Object object) {
        String simpleName = object.getClass().getSimpleName();
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            if (saveObject(fos, object)) {
                System.out.println(simpleName + " saved successfully to " + filePath);
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
