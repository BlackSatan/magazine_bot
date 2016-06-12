package magazinebot.services;

import magazinebot.exceptions.PriceFinderServiceException;
import pricefinder.exceptions.PriceNotFoundException;
import pricefinder.selenium.SeleniumPriceFinder;

import java.net.MalformedURLException;

public class PriceFinderService {

    SeleniumPriceFinder finder = new SeleniumPriceFinder();

    public float getPrice(String url) throws PriceFinderServiceException {
        try {
            return Float.parseFloat(finder.get(url));
        }
        catch (MalformedURLException e) {
            throw new PriceFinderServiceException();
        }
        catch (PriceNotFoundException e) {
            throw new PriceFinderServiceException();
        }
    }
}

