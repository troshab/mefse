package com.fido.tro.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class FileUtils {
    public static List<String> getPaths(String filename) {
        File file = new File(filename);
        List<String> paths = new LinkedList<>();

        if (!file.exists()) {
            System.err.println(filename + " doesn't exists!");
            return new LinkedList<>();
        }

        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (final File fileEntry : Objects.requireNonNull(fileList)) {
                if (fileEntry.isFile()) {
                    paths.add(fileEntry.getAbsolutePath());
                }
            }
        } else {
            paths.add(file.getAbsolutePath());
        }
        return paths;
    }

    public static Stream<String> linesStream(String filepath) {
        Path path = Paths.get(filepath);
        try {
            return Files.lines(path);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
