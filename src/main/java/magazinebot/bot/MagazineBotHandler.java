package magazinebot.bot;

import com.sun.javafx.util.Logging;
import magazinebot.bot.request.CommandParser;
import magazinebot.bot.request.CommandRequest;
import magazinebot.exceptions.WrongCommandFormatException;
import magazinebot.models.PriceTrackRequest;
import org.apache.commons.logging.Log;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import pricefinder.exceptions.PriceNotFoundException;
import pricefinder.selenium.SeleniumPriceFinder;

import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MagazineBotHandler extends TelegramLongPollingBot {

    private static final String LOGTAG = "MAGAZINEHANDLERS";
    private static final String COMMAND_NOT_FOUND_ERROR = "Sorry, command not found.";

    @Override
    public String getBotToken() {
        return BotConig.TOKEN;
    }

    public void onUpdateReceived(Update update) {
        try {
            handleIncomingUpdate(update);

        } catch (Exception e) {
            Logger log = Logger.getLogger(Logging.class.getName());
            log.log(Level.WARNING, LOGTAG, e);
        }
    }

    public String getBotUsername() {
        return BotConig.USERNAME;
    }

    private void handleIncomingUpdate(Update update) throws InvalidObjectException {
        if(!update.hasMessage())
            return;
        Message message = update.getMessage();
        String command = message.getText();
        CommandRequest request;
        try {
            CommandParser parser = new CommandParser(command);
            request = parser.getCommandRequest();
        }
        catch (WrongCommandFormatException e) {
            showErrorMessage(message);
            return;
        }
        BotCommands currentCommand = BotCommands.valueOf(request.getCommandName());
        String[] arguments = request.getArguments();
        switch (currentCommand) {
            case ADD:
                if(request.getArgumentsCount() == 0) {
                    showMessagesList(message);
                    return;
                }

                addPriceTrackRequest(message, arguments[0]);
                break;
            case LIST:
                showMessagesList(message);
                break;
            default:
                showErrorMessage(message);
                break;
        }
    }

    private void addPriceTrackRequest(Message message, String url) {
        SeleniumPriceFinder finder = new SeleniumPriceFinder();
        String price = null;
        try {
            price = finder.get(url);
            PriceTrackRequest.addRequest(url, Float.parseFloat(price), message.getChatId());
        } catch (PriceNotFoundException e) {
            e.printStackTrace();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void showMessagesList(Message message) {
        List<PriceTrackRequest> requests = PriceTrackRequest.where("userId = ?", message.getChatId());
        String response = BotResponseFormatter.showRequestsList(requests);
        sendTextMessage(message.getChatId().toString(), response);
    }

    private void showErrorMessage(Message message) {
        sendTextMessage(message.getChatId().toString(), COMMAND_NOT_FOUND_ERROR);
    }

    private void sendTextMessage(String chatId, String text) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setText(text);
        sendMessageRequest.setChatId(chatId);
        try {
            sendMessage(sendMessageRequest);
        } catch (TelegramApiException e) {
            Logger log = Logger.getLogger(Logging.class.getName());
            log.log(Level.WARNING, LOGTAG, e);
        }
    }
}
