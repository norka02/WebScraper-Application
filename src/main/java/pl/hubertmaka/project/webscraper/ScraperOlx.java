package pl.hubertmaka.project.webscraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import pl.hubertmaka.project.enums.*;
import pl.hubertmaka.project.interfaces.Scraper;

import java.io.IOException;
import java.util.ArrayList;

public class ScraperOlx {
    private static final Logger logger = LogManager.getLogger(ScraperOlx.class);
    private final String coreUrl = "https://www.olx.pl/nieruchomosci/mieszkania/";
    private final PropertyType propertyType;
    private final PurchaseType purchaseType;
    private CityType cityType = CityType.valueOf("");
    private VoivodeshipType voivodeshipType = VoivodeshipType.valueOf("");

    // Do scrapwoania poszczególnych miast
    public ScraperOlx(PropertyType propertyType, PurchaseType purchaseType, CityType cityType, VoivodeshipType voivodeshipType) {
        this.propertyType = propertyType;
        this.purchaseType = purchaseType;
        this.cityType = cityType;
        this.voivodeshipType = voivodeshipType;
    }

    public ArrayList<Elements> getAllElementsFromSite(int max_pages) throws IOException {
        int page = 1;
        ArrayList<Elements> elementsArrayList = new ArrayList<>();

        while (true) {
            Elements itemsList = scrapSite(page, true);

            if (itemsList.isEmpty()) {
                logger.info("No more elements.");
                break;
            }

            elementsArrayList.add(itemsList);

            if (page == max_pages) {
                break;
            }

            logger.info("Adding elements to ArrayList nr:" + page);

            page++;
        }
        return elementsArrayList;
    };

    private Connection connectToSite(String url) {
        return Jsoup.connect(url);
    }

    private Document getDocument(Connection connection) throws IOException {
        return connection.get();
    }

    private Elements getItemsList(Document document) {
        return document.select("");
    }

    private String buildUrlOnlyCity(int page) {
        StringBuilder url = new StringBuilder();
        url.append(coreUrl)
                .append(this.purchaseType.getPolishName()).append("/")
                .append(this.propertyType.getPolishName()).append("/")
                .append(this.cityType.getPolishName())
                .append("&page=").append(page);
        return url.toString();
    }

    private String buildUrlAllVoivodeship(int page) {
        StringBuilder url = new StringBuilder();
        url.append(coreUrl)
                .append(this.purchaseType.getPolishName()).append("/")
                .append(this.propertyType.getPolishName()).append("/")
                .append(this.voivodeshipType.getPolishName()).append("/")
                .append("&page=").append(page);
        return url.toString();
    }

    private Elements scrapSite(int page, boolean scrapOnlyCity) throws IOException {
        String url;
        if (scrapOnlyCity) {
            url = buildUrlOnlyCity(page);
        } else {
            url = buildUrlAllVoivodeship(page);
        }
        logger.info("Building url nr: " + page);
        Connection connection = this.connectToSite(url);
        logger.info("Connecting to site nr: " + page);
        Document document = this.getDocument(connection);
        logger.info("Getting document nr:" + page);
        Elements itemsList = this.getItemsList(document);
        logger.info("Getting elements nr: " + page);

        return itemsList;
    }
}
