package com.fido.tro.cli;

import java.util.Scanner;

public class CliUtils {
    private final static Scanner scanner = new Scanner(System.in);

    public static boolean decide(final boolean forced) {
        if (forced) {
            System.err.println("Forced");
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

    public static String ask(final String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static boolean argumentsCountNeeded(final String message, final CliCommand command, final Integer argumentsNeededCount) {
        Integer commandArgumentsCount = command.getCommandArgumentsCount();
        if (argumentsNeededCount > commandArgumentsCount) {
            System.err.println(message);
            return false;
        }
        return true;
    }

    public static void getHelp() {
        System.out.println("list [%indices%] - to show data");
        System.out.println("load [%indices%] %path% - to load file (or files in dir(depth 1))");
        System.out.println("save [%indices%] %path% - to save file");
        System.out.println("find [%indices%] - to boolean find (supports AND OR ! commands)");
        System.out.println("serializers [%serializer_type%] - to change serializers");
        System.out.println("exit - to exit (:");
        System.out.print("\n");
        System.out.println("indices: vocabulary, indexes, matrix");
        System.out.println("serializer_type: 0 - JAVA, 1 - KRYO, 2 - JACKSON, 3 - JAXB");
    }

    public static boolean exit() {
        System.out.println("Bye!");
        return true;
    }
}
