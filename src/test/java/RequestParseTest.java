import junit.framework.TestCase;
import magazinebot.bot.request.CommandParser;
import magazinebot.bot.request.CommandRequest;
import magazinebot.exceptions.WrongCommandFormatException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RequestParseTest extends TestCase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testBaseTestCommandTest() throws WrongCommandFormatException {
        CommandParser parser = new CommandParser("/test arg1 arg2");
        CommandRequest request = parser.getCommandRequest();
        assertEquals("test", request.getCommandName());
        assertEquals(2, request.getArgumentsCount());
        String[] arguments = request.getArguments();
        assertEquals(arguments[0],"arg1");
        assertEquals(arguments[1],"arg2");
    }

    @Test(expected = WrongCommandFormatException.class)
    public void baseWrongArgumentTestException() throws WrongCommandFormatException {
        CommandParser parser = new CommandParser("test arg1 arg2");
        exception.expect(WrongCommandFormatException.class);
        parser.getCommandRequest();
    }

    @Test(expected = WrongCommandFormatException.class)
    public void baseWrongArgumentTestException2() throws WrongCommandFormatException {
        CommandParser parser = new CommandParser("");
        exception.expect(WrongCommandFormatException.class);
        CommandRequest request = parser.getCommandRequest();
    }

    @Test(expected = WrongCommandFormatException.class)
    public void baseWrongArgumentTestException3() throws WrongCommandFormatException {
        CommandParser parser = new CommandParser("");
        exception.expect(WrongCommandFormatException.class);
        CommandRequest request = parser.getCommandRequest();
    }

}
