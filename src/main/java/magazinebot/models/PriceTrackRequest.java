package magazinebot.models;

import org.javalite.activejdbc.Model;

import java.util.List;

public class PriceTrackRequest extends Model {

    private static int SIX_HOURS = 60*60*6;

    public String getUrl() {
        return getString("url");
    }

    public Float getPrice() {
        return getFloat("price");
    }

    public Long getChatId() {
        return getLong("chatId");
    }

    public static List<PriceTrackRequest> getUnchecked(int limit) {
        long unixTime = System.currentTimeMillis() / 1000L - SIX_HOURS;

        return PriceTrackRequest.where("limit > " + unixTime).limit(limit);
    }

    public static PriceTrackRequest addRequest(String url, float price, Long chatId) {
        PriceTrackRequest p = new PriceTrackRequest();
        p.set("url", url);
        p.set("price", price);
        p.set("chatId", chatId);
        p.saveIt();

        return p;
    }

}
