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
}
