package pl.hubertmaka.project.webscraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.hubertmaka.project.enums.*;

import java.io.IOException;
import java.util.ArrayList;

public class ParserOlx extends ScraperOlx {
    private static final Logger logger = LogManager.getLogger(Parser.class);

    public ParserOlx(PropertyType propertyType, PurchaseType purchaseType, CityType cityType, VoivodeshipType voivodeshipType) {
        super(propertyType, purchaseType, cityType, voivodeshipType);
    }

    public static void main(String[] args) {
        try {
            // TODO: create builder design pattern
            ParserOlx parser = new ParserOlx(
                    PropertyType.APARTMENTS_OLX,
                    PurchaseType.FOR_SALE,
                    CityType.KRAKOW,
                    VoivodeshipType.LESSER_POLAND
            );

            ArrayList<Elements> elementsArrayList = parser.getAllElementsFromSite(1);
            int counter = 0;

            for (Elements listItems : elementsArrayList) {
                for (Element listItem : listItems) {
                    Element title = listItem.selectFirst("h6.css-16v5mdi.er34gjf0");
                    if (title != null) {
                        System.out.println(title.text());
                    }
                    Element price = listItem.selectFirst("p.css-10b0gli.er34gjf0");
                    if (price != null) {
                        System.out.println(price.text());
                    }
                    Element location = listItem.selectFirst("p.css-veheph");
                    if (location != null) {
                        System.out.println(location.text());
                    }
                    System.out.println("\n-------------------------------------------");
                    counter++;
                }
            }
            System.out.println(counter);

        } catch (IOException e) {
            logger.error(e.toString());
        }
    }
}
