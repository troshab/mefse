package com.fido.tro.serializers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class POJO extends AbstractSerializer {
    @Override
    public String description() {
        return "Object Serialization supports the encoding of objects and the objects reachable from them, into a stream of bytes. Serialization also supports the complementary reconstruction of the object graph from a stream. Serialization is used for lightweight persistence and for communication via sockets or Java Remote Method Invocation (Java RMI).";
    }

    @Override
    public Object loadObject(FileInputStream fis, Class objectClass) {
        try {
            ObjectInputStream in = new ObjectInputStream(fis);
            Object readObject = in.readObject();
            in.close();
            return readObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveObject(FileOutputStream fos, Object object) {
        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(fos);
            objectOutput.writeObject(object);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
