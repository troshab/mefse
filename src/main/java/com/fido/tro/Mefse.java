package com.fido.tro;

public class Mefse {
    public static boolean debug = true;

    public static void main(final String[] args) {
        String commandLine;

        Router router = new Router();
        router.execCommandLine("populate d:\\df");

        /*System.out.println("\ngra*ate test\n");
        router.execCommandLine("find suffixtree gra*ate");
        router.execCommandLine("find kgram gra*ate");

        System.out.println("\npas*ion*uit test\n");
        router.execCommandLine("find suffixtree pas*ion*uit");
        router.execCommandLine("find kgram pas*ion*uit");

        System.out.println("\npom*gra*ate test\n");
        router.execCommandLine("find suffixtree pom*gra*ate");
        router.execCommandLine("find kgram pom*gra*ate");

        System.out.println("\nfull test\n");
        router.execCommandLine("find suffixtree pas*ion*uit and pom*gra*ate");
        router.execCommandLine("find kgram pas*ion*uit and pom*gra*ate");*/

        System.out.println("\nfull test\n");
        router.execCommandLine("find suffixtree ban* and *ple");
        router.execCommandLine("find kgram ban* and *ple");

        //router.execCommandLine("save d:\\saved.db");
        //router.execCommandLine("exit");
        do {
            commandLine = router.getCommandLine();
        } while (router.execCommandLine(commandLine));
    }
}