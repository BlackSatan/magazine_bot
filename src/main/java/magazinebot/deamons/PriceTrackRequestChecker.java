package magazinebot.deamons;

import magazinebot.bot.BotResponseFormatter;
import magazinebot.bot.MagazineBotHandler;
import magazinebot.models.PriceTrackRequest;
import magazinebot.services.PriceFinderService;
import java.util.List;

public class PriceTrackRequestChecker {

    private static int CHECK_LIMIT = 20;

    private List<PriceTrackRequest> getUncheckedRequest() {
        return PriceTrackRequest.getUnchecked(CHECK_LIMIT);
    }

    public void main() {
        PriceFinderService finder = new PriceFinderService();
        List<PriceTrackRequest> requestList = getUncheckedRequest();
        for (PriceTrackRequest request:
             requestList) {
            try {
                float price = finder.getPrice(request.getUrl());
                if(price != request.getPrice()) {
                    request.set("price", price);
                    MagazineBotHandler handler = new MagazineBotHandler();
                    handler.sendTextMessage(request.getChatId().toString(), BotResponseFormatter.showPriceUpdatedMessage(price, request.getUrl()));
                }
                request.saveIt();
            }
            catch (Exception e) {
                e.getStackTrace();
            }
        }
    }
}
