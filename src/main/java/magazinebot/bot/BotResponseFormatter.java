package magazinebot.bot;


import magazinebot.models.PriceTrackRequest;

import java.util.List;

public class BotResponseFormatter {

    public static String showRequestsList(List<PriceTrackRequest> requests) {
        String response = "";
        for(PriceTrackRequest request : requests) {
            response += request.getUrl()
                    + ":" +  request.getPrice().toString() + "\n";
        }

        return response;
    }

    public static String showSuccessAddMessage(Float price, String URL) {
        return "Product " + URL + " was successfully added to tracklist, current price " + price.toString();
    }

    public static String showPriceUpdatedMessage(Float price, String URL) {
        return "Price on your product " + URL + " was updated, new price is" + price.toString();
    }


}
