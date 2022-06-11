package commands;

import java.util.List;

public class CommandResolver {
    private static List<Command> allCommands = List.of(
            new LoginCommand()
    );

    public static Command resolveCommand(String commandName) {
        for (Command command : allCommands) {
            if (commandName.equals(command.getCommandName())) {
                return command;
            }
        }
        throw new RuntimeException("Invalid command!");
    }
}
