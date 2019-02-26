package com.fido.tro;

public class Mefse {
    public static boolean debug = true;

    public static void main(final String[] args) {
        String commandLine;

        Router router = new Router();
        router.execCommandLine("populate d:\\df");
        router.execCommandLine("find kgram ma*on*ry");
        //router.execCommandLine("save d:\\saved.db");
        //router.execCommandLine("exit");
        do {
            commandLine = router.getCommandLine();
        } while (router.execCommandLine(commandLine));
    }
}