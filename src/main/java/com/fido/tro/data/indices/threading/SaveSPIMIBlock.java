package com.fido.tro.data.indices.threading;

import com.fido.tro.Controller;
import com.fido.tro.data.indices.entities.Filepath;

import java.util.HashMap;
import java.util.Map;

public class SaveSPIMIBlock implements Runnable {
    Map<String, Filepath> block;
    String filepath;

    public SaveSPIMIBlock(Map<String, Filepath> block, String filepath) {
        this.block = new HashMap<>(block);
        this.filepath = filepath;
    }

    @Override
    public void run() {
        synchronized (block) {
            Controller.serializer.getSerializer().save(filepath, block, false);
            block.clear();
        }
    }
}
