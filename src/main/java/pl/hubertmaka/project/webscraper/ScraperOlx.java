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
    private final String coreUrl = "https://www.olx.pl/nieruchomosci/";
    private final PropertyType propertyType;
    private final PurchaseType purchaseType;

    protected volatile boolean isScrapingCancelled = false;
    public void cancelScraping() {
        this.isScrapingCancelled = true;
    }
    public void startScraping() {
        this.isScrapingCancelled = false;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public PurchaseType getPurchaseType() {
        return purchaseType;
    }

    public CityType getCityType() {
        return cityType;
    }

    public VoivodeshipType getVoivodeshipType() {
        return voivodeshipType;
    }

    private final CityType cityType;
    private final VoivodeshipType voivodeshipType;

    // Do scrapwoania poszczególnych miast
    public ScraperOlx(PropertyType propertyType, PurchaseType purchaseType, CityType cityType, VoivodeshipType voivodeshipType) {
        this.propertyType = propertyType;
        this.purchaseType = purchaseType;
        this.cityType = cityType;
        this.voivodeshipType = voivodeshipType;
    }

    protected ArrayList<Elements> getAllElementsFromSite(int max_pages) throws IOException, InterruptedException {
        int page = 1;
        ArrayList<Elements> elementsArrayList = new ArrayList<>();

        while (true) {
            if (isScrapingCancelled) {
                break;
            }
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

    protected ArrayList<Elements> getAllElementsFromSite() throws IOException, InterruptedException {
        int page = 1;
        ArrayList<Elements> elementsArrayList = new ArrayList<>();

        while (true) {
            if (isScrapingCancelled) {
                break;
            }
            Elements itemsList = scrapSite(page, true);

            if (itemsList.isEmpty()) {
                logger.info("No more elements.");
                break;
            }

            elementsArrayList.add(itemsList);

            logger.info("Adding elements to ArrayList nr:" + page);

            page++;
        }
        return elementsArrayList;
    };

    private Connection connectToSite(String url) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            logger.info("Scraping interrupted, exiting connectToSite");
            throw new InterruptedException("Scraping interrupted during connectToSite");
        }
        return Jsoup.connect(url);
    }

    private Document getDocument(Connection connection) throws IOException {
        return connection.get();
    }

    private Elements getItemsList(Document document) {
        return document.select("div.css-oukcj3 div.css-1sw7q4x");
    }

    private String buildUrlOnlyCity(int page) {
        StringBuilder url = new StringBuilder();
        url.append(coreUrl)
                .append(this.propertyType.getPolishName()).append("/")
                .append(this.purchaseType.getPolishName()).append("/")
                .append(this.cityType.getPolishName()).append("/")
                .append("?page=").append(page);
        return url.toString();
    }

    private String buildUrlAllVoivodeship(int page) {
        StringBuilder url = new StringBuilder();
        url.append(coreUrl)
                .append(this.propertyType.getPolishName()).append("/")
                .append(this.purchaseType.getPolishName()).append("/")
                .append(this.voivodeshipType.getPolishName()).append("/")
                .append("?page=").append(page);
        return url.toString();
    }


    private Elements scrapSite(int page, boolean scrapOnlyCity) throws IOException, InterruptedException {
        String url;
        if (scrapOnlyCity) {
            url = buildUrlOnlyCity(page);
            logger.info("Choosing scraping only cities");
        } else {
            url = buildUrlAllVoivodeship(page);
            logger.info("Choosing scraping voivodeships");
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
