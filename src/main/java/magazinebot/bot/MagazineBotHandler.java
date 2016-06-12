package magazinebot.bot;

import magazinebot.bot.request.CommandParser;
import magazinebot.bot.request.CommandRequest;
import magazinebot.exceptions.PriceFinderServiceException;
import magazinebot.exceptions.WrongCommandFormatException;
import magazinebot.models.PriceTrackRequest;
import magazinebot.services.*;
import magazinebot.services.PriceFinderService;
import org.apache.commons.logging.Log;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.InvalidObjectException;
import java.util.List;


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
            System.err.print("Exception while handling incoming update: " + e.toString());
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

        BotCommands currentCommand = BotCommands.valueOf(request.getCommandName().toUpperCase());
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
        PriceFinderService finder = new PriceFinderService();
        Float price;
        try {
            price = finder.getPrice(url);
            PriceTrackRequest.addRequest(url, price, message.getChatId());
            sendTextMessage(message.getChatId().toString(), BotResponseFormatter.showSuccessAddMessage(price, url));
        } catch (PriceFinderServiceException e) {
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
            System.err.print(LOGTAG);
        }
    }
}
