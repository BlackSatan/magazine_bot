package magazinebot.models;

import org.javalite.activejdbc.Model;

public class PriceTrackRequest extends Model {

    public String getUrl() {
        return getString("url");
    }

    public Float getPrice() {
        return getFloat("price");
    }

    public Long getCharId() {
        return getLong("chatId");
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
