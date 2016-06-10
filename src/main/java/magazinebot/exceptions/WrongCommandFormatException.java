package magazinebot.exceptions;

public class WrongCommandFormatException extends Exception {

    public WrongCommandFormatException() {}

    public WrongCommandFormatException(String message)
    {
        super(message);
    }
}
