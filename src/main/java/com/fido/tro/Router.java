package com.fido.tro;

import com.fido.tro.cli.CliCommand;
import com.fido.tro.cli.CliUtils;

class Router {
    private final Controller engine = new Controller();

    String getCommandLine() {
        return CliUtils.ask("mefse > ");
    }

    boolean execCommandLine(final String commandLine) {
        CliCommand command = new CliCommand(commandLine);
        boolean exit = false;
        switch (command.getCommand()) {
            case "populate":
                if (CliUtils.argumentsCountNeeded("file or directory which will be added to data", command, 1)) {
                    engine.populate(command.getFilename());
                }
                break;

            case "load":
                if (CliUtils.argumentsCountNeeded("path to saved data which will be loaded", command, 1)) {
                    engine.load(command.getFilename());
                }
                break;

            case "save":
                if (CliUtils.argumentsCountNeeded("path to file where data will be saved", command, 1)) {
                    engine.save(command.getFilename());
                }
                break;

            case "list":
                if (CliUtils.argumentsCountNeeded("engine indices which will be listed", command, 1)) {
                    engine.listData(command.getEngineType());
                }
                break;

            case "find":
                if (CliUtils.argumentsCountNeeded("engine indices in which will be search, search query", command, 1)) {
                    engine.find(command.getEngineType(), command.getArgumentsLine());
                }
                break;

            case "serializers":
                if (CliUtils.argumentsCountNeeded("serializers engine which will used, variants: " + engine.listSerializers(), command, 1)) {
                    engine.setSerializerEngine(command.getCommandArgument(0));
                }
                break;

            case "serializer":
                System.out.println(engine.listSerializers());
                break;

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
