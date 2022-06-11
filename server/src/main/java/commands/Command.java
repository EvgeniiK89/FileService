package commands;

public interface Command {
    String getCommandName();
    String executeCommand(String params);
}
