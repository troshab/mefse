package com.fido.tro.cli;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CliCommand {
    private final String DEFAULT_COMMAND_ENGINE_TYPE = "dictionary";

    private String command;
    private String engineType = DEFAULT_COMMAND_ENGINE_TYPE;
    private String filename = "";
    private int commandArgumentsCount = 0;
    private List<String> commandArguments;

    public CliCommand(String commandLine) {
        parseCommandLine(commandLine);
        defineEngineTypeAndFilename();
    }

    public String getCommand() {
        return command;
    }

    public String getEngineType() {
        return engineType;
    }

    public String getFilename() {
        return filename;
    }

    Integer getCommandArgumentsCount() {
        return commandArgumentsCount;
    }

    private void defineEngineTypeAndFilename() {
        if (command.equals("list") || command.equals("find")) {
            commandArguments.remove(0);
            if (getCommandArgumentsCount() > 0) {
                engineType = commandArguments.get(0).toLowerCase();
            }
            commandArguments.remove(engineType);
        } else {
            if (getCommandArgumentsCount() > 0) {
                filename = commandArguments.get(1);
            }
        }
        commandArgumentsCount = commandArguments.size();
    }

    private void parseCommandLine(String commandLine) {
        String[] commandLineSplit = commandLine.split(" ");

        parseCommand(commandLineSplit);
        parseCommandArguments(commandLineSplit);
    }

    private void parseCommandArguments(String[] commandLineSplit) {
        commandArguments = new LinkedList<>();
        commandArguments.addAll(Arrays.asList(commandLineSplit));
        commandArgumentsCount = commandArguments.size() - 1;
    }

    private void parseCommand(String[] commandLineSplit) {
        command = commandLineSplit[0].toLowerCase();
    }

    public String getCommandArgument(Integer commandArgumentNumber) {
        if (getCommandArgumentsCount() < commandArgumentNumber) {
            System.err.println("Called argument with number less than arguments count");
        }
        return commandArguments.get(commandArgumentNumber);
    }

    public String getArgumentsLine() {
        StringBuilder line = new StringBuilder();
        for (String argument : commandArguments) {
            line.append(" ").append(argument);
        }
        return line.substring(1);
    }
}
