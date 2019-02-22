package com.fido.tro.Serializer.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fido.tro.Serializer.SerializerBase;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class JacksonXml extends SerializerBase {
    @Override
    public String description() {
        return "This projects contains Jackson extension component for reading and writing XML encoded data. Further, the goal is to emulate how JAXB data-binding works with \"Code-first\" approach (that is, no support is added for \"Schema-first\" approach).";
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
