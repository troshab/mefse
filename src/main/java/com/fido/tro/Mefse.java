package com.fido.tro;

import com.fido.tro.cli.CliBenchmark;

public class Mefse {
    public static boolean debug = true;

    public static void main(final String[] args) {
        String commandLine;

        Router router = new Router();
        CliBenchmark cliBenchmark = new CliBenchmark(true);
        //router.execCommandLine("populate d:\\bigdata");
        cliBenchmark.timeTaken(true);
        router.execCommandLine("find spimi trump");
        router.execCommandLine("find spimi donald");
        /*cliBenchmark = new CliBenchmark(true);
        router.execCommandLine("find spimi test");
        router.execCommandLine("find spimi lol");
        router.execCommandLine("find spimi trump");
        cliBenchmark.timeTaken();
        cliBenchmark = new CliBenchmark(true);
        router.execCommandLine("find spimi test");
        router.execCommandLine("find spimi lol");
        router.execCommandLine("find spimi trump");
        cliBenchmark.timeTaken();
        cliBenchmark = new CliBenchmark(true);
        router.execCommandLine("find spimi test");
        router.execCommandLine("find spimi lol");
        router.execCommandLine("find spimi trump");
        cliBenchmark.timeTaken(true);

        //router.execCommandLine("find suffixtree в*го*");
        //router.execCommandLine("find kgram в*го*");

        /*System.out.println("\ngra*ate test\n");
        router.execCommandLine("find suffixtree gra*ate");
        router.execCommandLine("find kgram gra*ate");

        /*System.out.println("\npassion* test\n");
        router.execCommandLine("find suffixtree passion**");
        router.execCommandLine("find kgram passion**");

        System.out.println("\n*nan test\n");
        router.execCommandLine("find suffixtree **nana");
        router.execCommandLine("find kgram **nana");

        System.out.println("\npas*ion*uit test\n");
        router.execCommandLine("find suffixtree pas**ion**uit");
        router.execCommandLine("find kgram pas**ion**uit");*/

        /*System.out.println("\npom*gra*ate test\n");
        router.execCommandLine("find suffixtree pom*gra*ate");
        router.execCommandLine("find kgram pom*gra*ate");

        System.out.println("\nfull test\n");
        router.execCommandLine("find suffixtree pas*ion*uit and pom*gra*ate");
        router.execCommandLine("find kgram pas*ion*uit and pom*gra*ate");

        System.out.println("\nfull test\n");
        router.execCommandLine("find suffixtree ban* and *ple");
        router.execCommandLine("find kgram ban* and *ple");

        //router.execCommandLine("save d:\\saved.db");
        //router.execCommandLine("exit");
        do {
            commandLine = router.getCommandLine();
        } while (router.execCommandLine(commandLine));*/
    }
}