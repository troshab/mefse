package com.fido.tro.serializers;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import com.fido.tro.data.Record;
import com.fido.tro.data.indices.entities.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class Kryo extends AbstractSerializer {
    int processorsCount = Runtime.getRuntime().availableProcessors();
    Pool<com.esotericsoftware.kryo.Kryo> kryoPool = new Pool<com.esotericsoftware.kryo.Kryo>(true, false, processorsCount * 2) {
        protected com.esotericsoftware.kryo.Kryo create() {
            com.esotericsoftware.kryo.Kryo kryo = new com.esotericsoftware.kryo.Kryo();
            registerKryoClasses(kryo);
            return kryo;
        }
    };

    @Override
    public String description() {
        return "Kryo is a fast and efficient binary object graph serialization framework for Java. The goals of the project are high speed, low size, and an easy to use API.";
    }

    private void registerKryoClasses(com.esotericsoftware.kryo.Kryo kryo) {
        kryo.register(HashMap.class);
        kryo.register(ConcurrentHashMap.class);
        kryo.register(Record.class);
        kryo.register(Filepath.class);
        kryo.register(LinkedHashSet.class);
        kryo.register(FilepathWithPositions.class);
        kryo.register(Positions.class);
        kryo.register(LinkedList.class);
        kryo.register(MatrixRow.class);
        kryo.register(BitSet.class);
        kryo.register(long[].class);
        kryo.register(Term.class);
        kryo.register(Long.class);
    }

    @Override
    public Object loadObject(FileInputStream fis, Class objectClass) {
        com.esotericsoftware.kryo.Kryo kryo;
        synchronized (kryoPool) {
            kryo = kryoPool.obtain();
        }
        try {
            Input input = new Input(fis);
            Object readObject = kryo.readObject(input, objectClass);
            input.close();
            synchronized (kryoPool) {
                kryoPool.free(kryo);
            }
            return readObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean saveObject(FileOutputStream fos, Object engine) {
        com.esotericsoftware.kryo.Kryo kryo = kryoPool.obtain();
        try {
            Output output = new Output(fos);
            kryo.writeObject(output, engine);
            output.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        kryoPool.free(kryo);
        return false;
    }
}