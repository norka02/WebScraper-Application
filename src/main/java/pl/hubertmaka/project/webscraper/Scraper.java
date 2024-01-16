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
import java.util.ArrayList;


public class Scraper {
    private static final Logger logger = LogManager.getLogger(Scraper.class);

    private final String coreUrl = "https://www.otodom.pl/pl/wyniki/";
    private final PropertyType propertyType;
    private final PurchaseType purchaseType;
    private final CityType cityType;
    private final VoivodeshipType voivodeshipType;
    private final Limit limit;

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

    public Limit getLimit() {
        return limit;
    }

    public Scraper(PropertyType propertyType, PurchaseType purchaseType, CityType cityType, VoivodeshipType voivodeshipType, Limit limit) {
        this.propertyType = propertyType;
        this.purchaseType = purchaseType;
        this.cityType = cityType;
        this.voivodeshipType = voivodeshipType;
        this.limit = limit;
    }

    protected ArrayList<Elements> getAllElementsFromSite() throws IOException, InterruptedException {
        int page = 1;
        ArrayList<Elements> elementsArrayList = new ArrayList<>();
        while (true) {
            Elements itemsList = scrapSite(page);

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


    protected ArrayList<Elements> getAllElementsFromSite(int max_pages) throws IOException, InterruptedException {
        int page = 1;
        ArrayList<Elements> elementsArrayList = new ArrayList<>();

        while (true) {
            Elements itemsList = scrapSite(page);

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

    private Elements scrapSite(int page) throws IOException {
        String url = buildUrl(page);
        logger.info("Building url nr: " + page);
        Connection connection = this.connectToSite(url);
        logger.info("Connecting to site nr: " + page);
        Document document = this.getDocument(connection);
        logger.info("Getting document nr:" + page);
        Elements itemsList = this.getItemsList(document);
        logger.info("Getting elements nr: " + page);

        return itemsList;
    }

    private String buildUrl(int page) {
        StringBuilder url = new StringBuilder();
        url.append(coreUrl)
                .append(this.purchaseType.getPolishName()).append("/")
                .append(this.propertyType.getPolishName()).append("/")
                .append(this.voivodeshipType.getPolishName()).append("/")
                .append(this.cityType.getPolishName())
                .append("?limit=").append(this.limit.getLimit())
                .append("&page=").append(page);
        return url.toString();
    }

    private Connection connectToSite(String url) {
        return Jsoup.connect(url);
    }

    private Document getDocument(Connection connection) throws IOException {
        return connection.get();
    }

    private Elements getItemsList(Document document) {
        return document.select(
                "[data-cy=search.listing.organic] " +
                        "ul.css-rqwdxd.e1tno8ef0 " +
                        "li.css-o9b79t.e1dfeild0 " +
                        "[data-cy=listing-item-link]"
        ); // lub data-cy="listing-item"
    }




}
