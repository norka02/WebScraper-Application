package pl.hubertmaka.project.webscraper;

// otodom.pl

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.hubertmaka.project.enums.*;

import java.io.IOException;


public class Scraper {
    private static final Logger logger = LogManager.getLogger(Scraper.class);

    public static void main(String[] args) {
        PropertyType propertyType = PropertyType.APARTMENTS;
        PurchaseType purchaseType= PurchaseType.FOR_SALE;
        CityType cityType = CityType.KRAKOW;
        VoivodeshipType voivodeshipType = VoivodeshipType.LESSER_POLAND;
        Limit limit = Limit.LIMIT_32;

        StringBuilder url = new StringBuilder();
        url.append("https://www.otodom.pl/pl/wyniki/")
                .append(purchaseType.getPolishName()).append("/")
                .append(propertyType.getPolishName()).append("/")
                .append(voivodeshipType.getPolishName()).append("/")
                .append(cityType.getPolishName())
                .append("?limit=72");
        try {
            Connection connect = Jsoup.connect(url.toString());
            Document document = connect.get();

            Elements listItems = document.select("[data-cy=search.listing.organic] ul.css-rqwdxd.e1tno8ef0 li.css-o9b79t.e1dfeild0"); // lub data-cy="listing-item"
            int counter = 0;
            for (Element listItem: listItems) {
                Element aElement = listItem.selectFirst("a");
                if (aElement != null) {
                    System.out.println(aElement.attr("href"));

                }
                counter++;

            }
            System.out.println(counter);
//            Elements images = document.select("img");
//
//            for (Element img: images) {
//                System.out.println(img.absUrl("src"));
//                counter++;
//            }
//            System.out.println(counter);
        } catch (IOException e) {
            logger.warn(e.toString());
        }

    }
}
