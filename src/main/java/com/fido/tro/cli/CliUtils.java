package com.fido.tro.cli;

import org.apache.log4j.Logger;

import java.util.Scanner;

public class CliUtils {
    private final static Logger LOGGER = Logger.getLogger(CliUtils.class);
    private final static Scanner scanner = new Scanner(System.in);

    public static boolean decide(boolean forced) {
        if (forced) {
            LOGGER.warn("Forced");
            return true;
        }

        String decision;
        do {
            decision = ask("Y or N to load: ").substring(0, 1).toLowerCase();
        } while (!decision.equals("y") && !decision.equals("n"));

        return decision.equals("y");
    }

    public static void needCommand() {
        System.err.println("No command, enter 'help' to get commands list");
    }

    public static String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static boolean argumentsCountNeeded(String message, CliCommand command, Integer argumentsNeededCount) {
        Integer commandArgumentsCount = command.getCommandArgumentsCount();
        if (argumentsNeededCount > commandArgumentsCount) {
            LOGGER.error(message);
            return false;
        }
        return true;
    }

    public static void getHelp() {
        System.out.println("list [%type%] - to show db");
        System.out.println("load [%type%] %path% - to load file (or files in dir(depth 1))");
        System.out.println("save [%type%] %path% - to save file");
        System.out.println("find [%type%] - to boolean find (supports AND OR ! commands)");
        System.out.println("serializer [%serializer_type%] - to change serializer");
        System.out.println("exit - to exit (:");
        System.out.println("");
        System.out.println("type: vocabulary, indexes, matrix");
        System.out.println("serializer_type: 0 - JAVA, 1 - KRYO, 2 - JACKSON, 3 - JAXB");
    }

    public static boolean exit() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Bye!");
        }
        return true;
    }
}
