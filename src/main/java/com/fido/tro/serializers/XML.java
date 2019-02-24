package com.fido.tro.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class XML extends AbstractSerializer {
    @Override
    public String description() {
        return "This projects contains Jackson extension component for reading and writing XML encoded structure. Further, the goal is to emulate how JAXB structure-binding works with \"Code-first\" approach (that is, no support is added for \"Schema-first\" approach).";
    }

    @Override
    public Object loadObject(FileInputStream fis, Class objectClass) {
        try {
            ObjectMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(fis, objectClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveObject(FileOutputStream fos, Object object) {
        try {
            ObjectMapper objectMapper = new XmlMapper();
            objectMapper.writeValue(fos, object);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
