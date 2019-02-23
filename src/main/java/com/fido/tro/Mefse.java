package com.fido.tro;

import com.fido.tro.cli.CliCommand;
import com.fido.tro.cli.CliUtils;
import com.fido.tro.db.DB;
import com.fido.tro.engine.Engine;
import com.fido.tro.serializer.Serializer;

public class Mefse {
    public static void main(final String[] args) {
        Engine.loadEngines();
        Serializer.loadSerializers();
        String commandLine;
        do {
            commandLine = CliUtils.ask("mefse > ");
        } while (runCommand(commandLine));
    }

    private static boolean runCommand(final String commandLine) {
        CliCommand command = new CliCommand(commandLine);
        boolean exit = false;
        switch (command.getCommand()) {
            case "populate":
                if (CliUtils.argumentsCountNeeded("file or directory which will be added to db", command, 1)) {
                    DB.populate(command.getFilename());
                }
                break;

            /*case "load":
                if (CliUtils.argumentsCountNeeded("path to saved db which will be loaded", command, 1)) {
                    DB.load(command.getFilename());
                }
                break;

            case "save":
                if (CliUtils.argumentsCountNeeded("path to file where db will be saved", command, 1)) {
                    DB.save(command.getFilename());
                }
                break;*/

            case "list":
                if (CliUtils.argumentsCountNeeded("engine type which will be listed", command, 1)) {
                    Engine.listData(command.getEngineType());
                }
                break;

            case "find":
                if (CliUtils.argumentsCountNeeded("engine type in which will be search, search query", command, 2)) {
                    Engine.find(command.getEngineType(), command.getArgumentsLine());
                }
                break;

            /*case "serializer":
                if (CliUtils.argumentsCountNeeded("serializer engine which will used, variants: " + Serializer.listSerializers(), command, 1)) {
                    Serializer.setSerializerEngine(command.getCommandArgument(0));
                }
                break;*/

            case "help":
                CliUtils.getHelp();
                break;

            case "exit":
                exit = CliUtils.exit();
                break;

            default:
                CliUtils.needCommand();
                break;
        }
        return !exit;
    }
}
