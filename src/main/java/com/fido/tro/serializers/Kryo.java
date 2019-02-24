package com.fido.tro.serializers;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fido.tro.data.Entity;
import com.fido.tro.data.Storage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class Kryo extends AbstractSerializer {
    @Override
    public String description() {
        return "Kryo is a fast and efficient binary object graph serialization framework for Java. The goals of the project are high speed, low size, and an easy to use API.";
    }

    private void registerKryoClasses(com.esotericsoftware.kryo.Kryo kryo) {
        kryo.register(Storage.class);
        kryo.register(Entity.class);
        kryo.register(HashMap.class);
        kryo.register(LinkedHashSet.class);
        kryo.register(BitSet.class);
        kryo.register(long[].class);
    }

    @Override
    public Object loadObject(FileInputStream fis, Class objectClass) {
        com.esotericsoftware.kryo.Kryo kryo = new com.esotericsoftware.kryo.Kryo();
        registerKryoClasses(kryo);

        Input input = new Input(fis);
        Object readObject = kryo.readObject(input, objectClass);
        input.close();
        return readObject;
    }


    @Override
    public boolean saveObject(FileOutputStream fos, Object engine) {
        try {
            com.esotericsoftware.kryo.Kryo kryo = new com.esotericsoftware.kryo.Kryo();
            registerKryoClasses(kryo);

            Output output = new Output(fos);
            kryo.writeObject(output, engine);
            output.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}