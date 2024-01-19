package pl.hubertmaka.project.webscraper;

/**
 OTODOM.PL Parser Class
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.hubertmaka.project.enums.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Parser extends Scraper {
    private static final Logger logger = LogManager.getLogger(Parser.class);
    public Parser(PropertyType propertyType, PurchaseType purchaseType, CityType cityType, VoivodeshipType voivodeshipType, Limit limit) {
        super(propertyType, purchaseType, cityType, voivodeshipType, limit);
    }

    public ApartmentInfo fillApartmentInfo(ApartmentInfo apartmentInfoInstance, Element listItem) {
        logger.info("Filling ApartmentInfo instance.");

        apartmentInfoInstance.setFromSite("OTODOM");
        apartmentInfoInstance.setTitle(getTitleText(listItem));
        apartmentInfoInstance.setPurchaseType(this.getPurchaseType().getPolishName());
        apartmentInfoInstance.setVoivodeship(extractLocationDetails(listItem).get("voivodeship"));
        apartmentInfoInstance.setCity(extractLocationDetails(listItem).get("city"));
        apartmentInfoInstance.setDistrict(extractLocationDetails(listItem).get("district"));
        apartmentInfoInstance.setStreet(extractLocationDetails(listItem).get("street"));
        apartmentInfoInstance.setPrice(parsePrice(getApartmentInfos(listItem)));
        apartmentInfoInstance.setPricePerMeter(parsePricePerMeter(getApartmentInfos(listItem)));
        apartmentInfoInstance.setRooms(parseRooms(getApartmentInfos(listItem)));
        apartmentInfoInstance.setSize(parseSize(getApartmentInfos(listItem)));
        apartmentInfoInstance.setImgSrc(getImgElementText(listItem));
        apartmentInfoInstance.setLinkToAnnouncement(getAElementHref(listItem));
        apartmentInfoInstance.setBoosted(getIsBoostedStatus(listItem));
        apartmentInfoInstance.setAdditionalInfo(getAdditionalInfo(listItem));

        return apartmentInfoInstance;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Parser parser = new Parser(PropertyType.APARTMENTS,
                PurchaseType.FOR_SALE,
                CityType.KRAKOW,
                VoivodeshipType.LESSER_POLAND,
                Limit.LIMIT_24);
        parser.getAllElementsFromSite();
    }

    private String getImgElementText(Element listItem) {
        Element imgElement = listItem.selectFirst("img");
        if (imgElement != null) {
            return imgElement.attr("src");
        }
        logger.warn("Empty element.");
        return "";
    }

    private String getAElementHref(Element listItem) {
        Element aElement = listItem.selectFirst("a");
        if (aElement != null) {
            return "https://www.otodom.pl" + aElement.attr("href");
        }
        logger.warn("Empty element.");
        return "";
    }

    private boolean getIsBoostedStatus(Element listItem) {
        Element isBoostedPTag = listItem.selectFirst("p.css-1vd92mz.ewcwyit0");
        return isBoostedPTag != null;
    }

    private String getTitleText(Element listItem) {
        Element title = listItem.selectFirst("[data-cy=listing-item-title]");
        if (title != null) {
            return title.text();
        }
        logger.warn("Empty element.");
        return "";
    }

    private String getLocationText(Element listItem) {
        Element location = listItem.selectFirst("article p.css-19dkezj.e1n06ry53");
        if (location != null) {
            return location.text();
        }
        logger.warn("Empty element.");
        return "";
    }

    private HashMap<String, String> extractLocationDetails(Element listItem) {
        String summaryLocation = getLocationText(listItem).toUpperCase();
        String[] summaryLocationArray = summaryLocation.split(", ");
        return mapLocations(summaryLocationArray);
    }

    private HashMap<String, String> mapLocations(String[] summaryLocationArray) {
        String[] possibleLocations = {"voivodeship", "city", "district", "street"};
        HashMap<String, String> extractedLocations = new HashMap<>();
        for (int i = 0; i < possibleLocations.length - 1; i++) {
            try {
                extractedLocations.put(possibleLocations[i], summaryLocationArray[summaryLocationArray.length - (i + 1)]);
            } catch (ArrayIndexOutOfBoundsException e) {
                extractedLocations.put(possibleLocations[i], "Not defined");
            }
        }

        // This loop can be used length - 2 times beacuse the street parameter from the scraped site is the fires arg
        try {
            extractedLocations.put(possibleLocations[possibleLocations.length - 1], summaryLocationArray[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            extractedLocations.put(possibleLocations[possibleLocations.length - 1], "Not defined");
        }

        return extractedLocations;
    }

    private HashMap<String, String> getApartmentInfos(Element listItem) {
        Elements apartmentInfos = listItem.select("span.css-1cyxwvy.ei6hyam2");
        HashMap<String, String> apartmentInfosHashMap = new HashMap<>();
        String[] labels = {"price", "pricePerMeter", "rooms", "size"};

        for (int i = 0; i < labels.length
                ; i++) {
            String textValue = apartmentInfos.get(i).text().replaceAll("[^\\d,]", "").replace(",", ".");
            apartmentInfosHashMap.put(labels[i], textValue);
        }
        return apartmentInfosHashMap;
    }

    private Integer  parsePrice(HashMap<String, String> apartmentInfosHashMap) {
        try {
            String priceString = apartmentInfosHashMap.get("price");
            return Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Double parsePricePerMeter(HashMap<String, String> apartmentInfosHashMap) {
        try {
            String pricePerMeter = apartmentInfosHashMap.get("pricePerMeter");
            return Double.parseDouble(pricePerMeter);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    private Integer parseRooms(HashMap<String, String> apartmentInfosHashMap) {
        try {
            String roomsString = apartmentInfosHashMap.get("rooms");
            return Integer.parseInt(roomsString);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Double parseSize(HashMap<String, String> apartmentInfosHashMap) {
        try {
            String sizeString = apartmentInfosHashMap.get("size");
            return Double.parseDouble(sizeString);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    private String getAdditionalInfo(Element listItem) {
        Element additionalInfo = listItem.selectFirst("[data-cy=listing-ad-description-text]");
        if (additionalInfo != null) {
            return additionalInfo.text();
        }
        logger.warn("Empty element.");
        return "";
    }

}
