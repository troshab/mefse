package com.fido.tro;

import com.fido.tro.DB.DB;
import morfologik.stemming.Dictionary;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.morfologik.MorfologikFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.uk.UkrainianMorfologikAnalyzer;
import org.apache.lucene.util.Version;
import org.tartarus.snowball.ext.PorterStemmer;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class Mefse {
    private static DB db = new DB();
    public static Set<String> parsedFiles = new LinkedHashSet<>();

    static URL dictionaryUrl = UkrainianMorfologikAnalyzer.class.getClassLoader().getResource("ua/net/nlp/ukrainian.dict");
    static Dictionary dictionary;

    public static void main(String[] args) {
        try {
            dictionary = Dictionary.read(dictionaryUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        populate("d:\\ddf", true);
//        db.find("aa and bb and !cc or cc and !cc");
//        db.save("d:\\finalDfinal.dat");
//        db.load("d:\\finalDfinal.dat");
//        System.out.println("Press Any Key To Continue...");
        db.find("холод and ялинки");
        //db.find("aa and bb and !cc");
        System.out.println("Press Any Key To Continue...");
        new java.util.Scanner(System.in).nextLine();
        System.exit(1);

        Scanner terminalInput = new Scanner(System.in);

        String input, command;
        String[] inputArray, commandArgs;

        boolean waitingForExit = true;
        while(waitingForExit) {
            System.out.print("mefse > ");
            input = terminalInput.nextLine();
            if (!input.isEmpty()) {
                inputArray = input.split(" ");
                command = inputArray[0].toLowerCase();
                commandArgs = getCommandArgs(inputArray);
                String type = "dictionary";
                String file = commandArgs.length != 0 ? commandArgs[0] : "";
                if (!command.equals("find")) {
                    if (command.equals("list")) {
                        if (commandArgs.length > 0)
                            type = commandArgs[0].toLowerCase();
                    } else {
                        if (commandArgs.length >= 2) {
                            type = commandArgs[0].toLowerCase();
                            file = commandArgs[1];
                        }
                    }
                }

                /*Engine engineFinded = null;
                for(Engine engine : ENGINES)
                    if (engine.getClass().getSimpleName().toLowerCase().equals(type))
                        engineFinded = engine;

                switch (command) {

                    case "populate":
                        if (needArgument("type of file and file or directory which will be loaded", commandArgs)) {
                            populate(commandArgs[0]);
                        }
                        break;

                    case "load":
                        if (needArgument("type of file and file or directory which will be loaded", commandArgs)) {
                            if (Objects.nonNull(engineFinded))
                                engineFinded.load(file, serialization);
                            else
                                System.out.println("Engine " + type + " not found!");
                        }
                        break;

                    case "list":
                        if (Objects.nonNull(engineFinded))
                            engineFinded.list();
                        else
                            System.out.println("Engine " + type + " not found!");
                        break;

                    case "save":
                        if (needArgument("type of file and file or directory which will be saved", commandArgs)) {
                            if (Objects.nonNull(engineFinded))
                                engineFinded.save(file, serialization);
                            else
                                System.out.println("Engine " + type + " not found!");
                        }
                        break;

                    /*case "find":
                        if (needArgument("query to find", commandArgs)) {
                            dictionary.findInMatrix(String.join(" ", commandArgs));
                        }
                        break;

                    case "serializer":
                        if (needArgument("0 - JAVA, 1 - KRYO, 2 - JACKSON", commandArgs)) {
                            int serialization = Integer.parseInt(commandArgs[0]);
                            if (Arrays.asList(Dictionary.SERIALIZATIONS).contains(serialization)) {
                                dictionary.serialization = serialization;
                            } else
                                System.out.println("Unknown serialization");
                        }
                        break;

                    case "debug":
                        dictionary.debug();
                        break;

                    case "help":
                        System.out.println("list [%type%] - to show DB");
                        System.out.println("load [%type%] %path% - to load file (or files in dir(depth 1))");
                        System.out.println("save [%type%] %path% - to save file");
                        System.out.println("find [%type%] - to boolean find (supports AND OR ! commands)");
                        System.out.println("serializer [%serializer_type%] - to change serializer");
                        System.out.println("exit - to exit (:");
                        System.out.print("\n");
                        System.out.println("type: vocabulary, indexes, matrix");
                        System.out.println("serializer_type: 0 - JAVA, 1 - KRYO, 2 - JACKSON, 3 - JAXB");
                        break;

                    case "exit":
                        System.out.println("Bye!");
                        waitingForExit = false;
                        break;

                    default:
                        needCommand();
                }*/
            } else
                needCommand();
        }
    }

    private static boolean needArgument(String message, String[] commandArgs) {
        if (commandArgs.length < 1) {
            System.out.println("Error: arguments needed. " + message);
            return false;
        }

        return true;
    }


    private static String[] getCommandArgs(String[] inputArray) {
        String[] commandArgs = new String[inputArray.length - 1];
        for (int i = 1, k = 0; i < inputArray.length; i++) {
            commandArgs[k++] = inputArray[i];
        }
        return commandArgs;
    }

    private static void needCommand() {
        System.out.println("No command, enter 'help' to get commands list");
    }

    public static String ucfirst(String string) {
        return string.toUpperCase().substring(0, 1) + string.toLowerCase().substring(1);
    }

    private static void populate(String filePath, boolean force) {
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println(filePath + " doesn't exists!");
            return;
        }

        if(file.isDirectory()) {
            loadDirectory(file, force);
        } else {
            loadFile(file, true);
        }
    }

    private static void loadFileParalleled(File file) {
        long startTime = System.currentTimeMillis();
        try {
/*
            byte[] fileBytes = Files.readAllBytes(path);
            String str = new String(fileBytes);
            Analyzer analyzer = new StandardAnalyzer();
            TokenStream termSetStream =
            termSetStream = new PorterStemFilter(tokenStream);
            return preProcess(termSetStream, INDEX_TERMS_PATH);
*/
/*            Analyzer analyzer = new StandardAnalyzer();
            List<String> parallelStream = Files.readAllLines(Paths.get(file.getAbsolutePath()));
            Files.readAllLines(Paths.get(file.getAbsolutePath())).
            TokenStream stream  = analyzer.tokenStream();
            stream.reset();
            Long wordCounter = 0L;
            while (stream.incrementToken())
                wordCounter = addWord(stream.getAttribute(CharTermAttribute.class).toString(), file.getAbsolutePath(), wordCounter);*/
            /*AtomicReference<Long> wordCounter = new AtomicReference<>(0L);
            Files.readAllLines(Paths.get(file.getAbsolutePath()))
                    .parallelStream()                               // Start streaming the lines
                    .map(line -> line.split("\\s+"))                // Split line into individual words
                    .flatMap(Arrays::stream)                        // Convert stream of String[] to stream of String
                    .parallel()                                     // Convert to parallel stream
                    .map(String::toLowerCase)                       // Convert to lower case
                    .filter(w -> w.matches("[а-їґ]+"))                 // Filter out non-word items
                    .forEach(word -> {                              // Use an AtomicAdder to tally word counts
                        wordCounter.set(addWord(word, file.getAbsolutePath(), wordCounter.get()));
                    });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        timeTaken(startTime);
    }

    private static void loadDirectory(File directory, boolean force) {
        File[] fileList = directory.listFiles();

        if (fileList == null) {
            System.out.println("fileList variable is null in Dictionary.loadFile");
            return;
        }

        boolean anyFile = false;
        for (final File fileEntry : fileList) {
            if (fileEntry.isFile()) {
                anyFile = true;
                break;
            }
        }

        if (!anyFile) {
            System.out.println(directory.getAbsolutePath() + " is directory, but there is no files inside.");
            return;
        }

        System.out.println(directory.getAbsolutePath() + " is directory, listing (depth 1):");

        for (final File fileEntry : fileList)
            if (fileEntry.isFile())
                if (!checkFileAlreadyExist(fileEntry))
                    System.out.println(fileEntry.getName());

        String decision = getDecision(force);
        if (decision.equals("y")) {
            for (final File fileEntry : fileList) {
                if (fileEntry.isFile()) {
                    loadFile(fileEntry, true);
                    loadFileParalleled(fileEntry);
                }
            }
        } else {
            System.out.println("Dictionary load declined.");
        }
    }

    private static String getDecision(boolean force) {
        if (force) {
            System.out.println("Force loading");
            return "y";
        }
        String decision = "";
        Scanner scanner = new Scanner(System.in);
        while(!decision.equals("y") && !decision.equals("n")) {
            System.out.print("Y or N to load: ");
            decision = scanner.nextLine();
        }
        return decision;
    }

    private static void loadFile(File file, boolean directlyFile) {
        long startTime = System.currentTimeMillis();
        if (directlyFile)
            if (checkFileAlreadyExist(file))
                return;

        if (!file.canRead()) {
            System.out.println(file.getAbsolutePath() + " can't be read!");
            return;
        }

        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String readLine;
            Long wordCounter = 0L;

            db.addFile(file.getAbsolutePath());

            Analyzer analyzer = new StandardAnalyzer();
            //Analyzer analyzer = new UkrainianMorfologikAnalyzer();
            PorterStemmer stem = new PorterStemmer();
            while ((readLine = bufferedReader.readLine()) != null) {
                TokenStream stream = analyzer.tokenStream(null, new StringReader(readLine));
                //stream = new LowerCaseFilter(stream);
                //stream
                stream = new MorfologikFilter(stream, dictionary);
                stream.reset();
                while (stream.incrementToken()) {
                    String word = stream.getAttribute(CharTermAttribute.class).toString();
                    stem.setCurrent(word);
                    stem.stem();
                    wordCounter = addWord(stem.getCurrent(), file.getAbsolutePath(), wordCounter);
                }
                stream.end();
                stream.close();
            }

            db.incrementFileCounter();

            System.out.println("File " + file.getAbsolutePath() + " successfully loaded!");
            bufferedReader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + file.getAbsolutePath() + " not found!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File " + file.getAbsolutePath() + " IO error during reading!");
            e.printStackTrace();
        }
        timeTaken(startTime);
    }

    private static boolean checkFileAlreadyExist(File file) {
        if (parsedFiles.contains(file.getAbsolutePath())) {
            System.out.println("File " + file.getAbsolutePath() + " already added.");

            String decision = getDecision(false);
            if (decision.equals("n")) {
                System.out.println("Ignoring already added file.");
                return true;
            }
        }
        return false;
    }

    private static Long addWord(String word, String filePath, Long wordCounter) {
        String cleanedWord = word.replaceAll(" ", "").replaceAll("\\s", "");
        if (!cleanedWord.isEmpty()) {
            db.add(cleanedWord, filePath, wordCounter);
            wordCounter++;
        }
        return wordCounter;
    }

    public static void timeTaken(long startTime) {
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms");
    }
}
