package com.fido.tro.serializer.type;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fido.tro.db.Table;
import com.fido.tro.engine.data.MatrixData;
import com.fido.tro.db.Record;
import com.fido.tro.engine.type.CoordinatedIndex;
import com.fido.tro.engine.type.Dictionary;
import com.fido.tro.engine.type.InvertedIndex;
import com.fido.tro.engine.type.TwoWordInvertedIndex;
import com.fido.tro.serializer.SerializerBase;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class KryoJava extends SerializerBase {
    @Override
    public String description() {
        return "Kryo is a fast and efficient binary object graph serialization framework for Java. The goals of the project are high speed, low size, and an easy to use API.";
    }

    private void registerKryoClasses(Kryo kryo) {
        /*kryo.register(Map.class);
        kryo.register(LinkedHashSet.class);
        kryo.register(MatrixData.class);
        kryo.register(Integer.class);
        kryo.register(Integer[].class);
        kryo.register(String.class);
        kryo.register(HashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(Dictionary.class);*/
        kryo.register(Table.class);
        kryo.register(Record.class);

        kryo.register(MatrixData.class);
        kryo.register(InvertedIndex.class);
        kryo.register(Dictionary.class);
        kryo.register(TwoWordInvertedIndex.class);
        kryo.register(CoordinatedIndex.class);

        kryo.register(HashMap.class);
        kryo.register(LinkedHashSet.class);
        kryo.register(BitSet.class);
        kryo.register(long[].class);
    }

    @Override
    public Object loadObject(FileInputStream fis, Class objectClass) {
        Kryo kryo = new Kryo();
        registerKryoClasses(kryo);

        Input input = new Input(fis);
        Object readObject = kryo.readObject(input, objectClass);
        input.close();
        return readObject;
    }


    @Override
    public boolean saveObject(FileOutputStream fos, Object engine) {
        try {
            Kryo kryo = new Kryo();
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