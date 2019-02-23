package com.fido.tro.serializer;

import com.fido.tro.utils.StringUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public abstract class SerializerBase {
    private final static Logger LOGGER = Logger.getLogger(SerializerBase.class);

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
                LOGGER.warn(StringUtils.ucfirst(simpleName) + " loaded successfully!");
            else
                LOGGER.error(StringUtils.ucfirst(simpleName) + " not loaded!");
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
            if (saveObject(fos, object))
                LOGGER.warn(simpleName + " saved successfully to " + filePath);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
