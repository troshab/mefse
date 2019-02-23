package com.fido.tro.cli;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CliCommand {
    private final static Logger LOGGER = Logger.getLogger(CliCommand.class);

    private final String DEFAULT_COMMAND_ENGINE_TYPE = "dictionary";

    private String command;
    public String getCommand() { return command; }

    private String engineType = DEFAULT_COMMAND_ENGINE_TYPE;
    public String getEngineType() { return engineType; }

    private String filename = "";
    public String getFilename() { return filename; }

    private List<String> commandArguments;
    public List<String> getCommandArguments() { return commandArguments; }
    Integer getCommandArgumentsCount() { return commandArguments.size(); }

    public CliCommand(String commandLine) {
        parseCommandLine(commandLine);
        defineEngineTypeAndFilename();
    }

    private void defineEngineTypeAndFilename() {
        if (getCommandArgumentsCount() != 0) {
            filename = commandArguments.get(0);
        }

        if (!command.equals("find")) {
            if (command.equals("list")) {
                if (getCommandArgumentsCount() > 0)
                    engineType = commandArguments.get(0).toLowerCase();
                    commandArguments.remove(engineType);
            } else {
                if (getCommandArgumentsCount() >= 2) {
                    engineType = commandArguments.get(0).toLowerCase();
                    filename = commandArguments.get(1);
                    commandArguments.remove(engineType);
                    commandArguments.remove(filename);
                }
            }
        }
    }

    private void parseCommandLine(String commandLine) {
        String[] commandLineSplit = commandLine.split(" ");

        parseCommand(commandLineSplit);
        parseCommandArguments(commandLineSplit);
    }

    private void parseCommandArguments(String[] commandLineSplit) {
        commandArguments = new LinkedList<>();
        commandArguments.addAll(Arrays.asList(commandLineSplit));
    }

    private void parseCommand(String[] commandLineSplit) {
        command = commandLineSplit[0].toLowerCase();
    }

    public String getCommandArgument(Integer commandArgumentNumber) {
        if (getCommandArgumentsCount() < commandArgumentNumber) {
            try {
                throw new CliCommandNotEnoughtArgumentsException();
            } catch (CliCommandNotEnoughtArgumentsException exception) {
                LOGGER.error("Called argument with number less than arguments count", exception);
            }
        }
        return commandArguments.get(commandArgumentNumber);
    }

    public String getArgumentsLine() {
        StringBuilder line = new StringBuilder();
        for(String argument : commandArguments) {
            line.append(" ")
                .append(argument);
        }
        return line.substring(1);
    }
}
