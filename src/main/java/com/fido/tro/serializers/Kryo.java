package com.fido.tro.serializers;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fido.tro.data.Record;
import com.fido.tro.data.fields.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class Kryo extends AbstractSerializer {
    private com.esotericsoftware.kryo.Kryo kryo = new com.esotericsoftware.kryo.Kryo();

    @Override
    public String description() {
        return "Kryo is a fast and efficient binary object graph serialization framework for Java. The goals of the project are high speed, low size, and an easy to use API.";
    }

    public Kryo() {
        registerKryoClasses(kryo);
    }

    private void registerKryoClasses(com.esotericsoftware.kryo.Kryo kryo) {
        kryo.register(HashMap.class);
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
    }

    @Override
    public Object loadObject(FileInputStream fis, Class objectClass) {
        Input input = new Input(fis);
        Object readObject = kryo.readObject(input, objectClass);
        input.close();
        return readObject;
    }


    @Override
    public boolean saveObject(FileOutputStream fos, Object engine) {
        try {
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