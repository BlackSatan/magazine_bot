package magazinebot.bot.request;


public class CommandRequest {

    private String commandName;
    private String[] arguments;

    CommandRequest(String commandNameNew, String[] argumentsNew) {
        commandName = commandNameNew;
        arguments = argumentsNew;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArguments() {
        return arguments;
    }

    public int getArgumentsCount() {
        return arguments.length;
    }
}
