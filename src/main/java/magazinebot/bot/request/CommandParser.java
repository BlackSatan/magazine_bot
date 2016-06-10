package magazinebot.bot.request;

import magazinebot.exceptions.WrongCommandFormatException;

import java.util.Arrays;

public class CommandParser {

    private String command;

    public CommandParser(String message) {
        command = message;
    }

    public CommandRequest getCommandRequest() throws WrongCommandFormatException {
        if(command.length() == 0)
            throw new WrongCommandFormatException("Command cant be empty");
        if(!command.substring(0, 1).equals("/"))
            throw new WrongCommandFormatException("Command must start with slash");
        command = command.substring(1);
        String[] array = command.split(" ");
        if(array.length == 0)
            throw new WrongCommandFormatException("Command must contain at least command name");
        String[] arguments = Arrays.copyOfRange(array, 1, array.length);
        return  new CommandRequest(array[0], arguments);
    }
}
