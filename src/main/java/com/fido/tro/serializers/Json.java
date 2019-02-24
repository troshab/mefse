package com.fido.tro.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Json extends AbstractSerializer {
    @Override
    public String description() {
        return "This is the home page of the Jackson Project, formerly known as the standard JSON library for Java (or JVM platform in general), or, as the \"best JSON parser for Java.\" Or simply as \"JSON for Java.\"";
    }

    @Override
    public Object loadObject(FileInputStream fis, Class objectClass) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(fis, objectClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveObject(FileOutputStream fos, Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(fos, object);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
