package com.fido.tro;

public class Mefse {
    public static boolean debug = false;

    public static void main(final String[] args) {
        String commandLine;

        Router router = new Router();
        do {
            commandLine = router.getCommandLine();
        } while (router.execCommandLine(commandLine));
    }
}
