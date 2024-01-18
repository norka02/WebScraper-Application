package pl.hubertmaka.project.webscraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.hubertmaka.project.enums.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParserOlx extends ScraperOlx {
    private static final Logger logger = LogManager.getLogger(Parser.class);

    public ParserOlx(PropertyType propertyType, PurchaseType purchaseType, CityType cityType, VoivodeshipType voivodeshipType) {
        super(propertyType, purchaseType, cityType, voivodeshipType);
    }

    public ApartmentInfo fillApartmentInfo(ApartmentInfo apartmentInfoInstance, Element listItem) {
        logger.info("Filling ApartmentInfo instance.");
        apartmentInfoInstance.setFromSite("OLX");
        apartmentInfoInstance.setPurchaseType(this.getPurchaseType().getPolishName());
        apartmentInfoInstance.setTitle(getTitleText(listItem));
        apartmentInfoInstance.setVoivodeship(getVoivodeshipType().getPolishName());
        apartmentInfoInstance.setCity(extractLocationDetails(listItem).get("city"));
        apartmentInfoInstance.setDistrict(extractLocationDetails(listItem).get("district"));
        apartmentInfoInstance.setStreet(extractLocationDetails(listItem).get("street"));
        apartmentInfoInstance.setPrice(parsePrice(getApartmentInfos(listItem)));
        apartmentInfoInstance.setPricePerMeter(parsePricePerMeter(getApartmentInfos(listItem)));
        apartmentInfoInstance.setSize(parseSize(getApartmentInfos(listItem)));
        apartmentInfoInstance.setLinkToAnnouncement(getAElementHref(listItem));
        apartmentInfoInstance.setBoosted(getIsBoostedStatus(listItem));

        return apartmentInfoInstance;
    }


    private String getAElementHref(Element listItem) {
        Element aElement = listItem.selectFirst("a");
        if (aElement != null) {
            String href = aElement.attr("href");
            if (href.contains("https://www.otodom.pl")) {
                return href;
            } else {
                return "https://www.olx.pl" + href;
            }
        }
        return "";
    }

    private boolean getIsBoostedStatus(Element listItem) {
        Element spanElement = listItem.selectFirst("p.css-veheph");
        if (spanElement != null) {
            return spanElement.text().contains("Dzisiaj");
        }
        return false;
    }

    private String getTitleText(Element listItem) {
        Element title = listItem.selectFirst("h6.css-16v5mdi.er34gjf0");
        if (title != null) {
            return title.text();
        }
        logger.warn("Empty element.");
        return "";
    }

    private String getLocationText(Element listItem) {
        Element location = listItem.selectFirst("p.css-veheph");
        if (location != null) {
            return location.text();
        }
        logger.warn("Empty element.");
        return "";
    }

    private HashMap<String, String> extractLocationDetails(Element listItem) {
        String summaryLocation = getLocationText(listItem).toUpperCase().split(" - ")[0];
        String[] summaryLocationArray = summaryLocation.split(", ");
        String[] possibleLocations = {"city", "district"};
        HashMap<String, String> extractedLocations = new HashMap<>();
        extractedLocations.put(possibleLocations[0], summaryLocationArray[0]);
        if (summaryLocationArray.length < 2) {
            extractedLocations.put(possibleLocations[1], "None");
        } else {
            extractedLocations.put(possibleLocations[1], summaryLocationArray[1]);
        }

        return extractedLocations;
    }

    private HashMap<String, String> getApartmentInfos(Element listItem) {
        Element sizeAndPricePerMeterElement = listItem.selectFirst("span.css-643j0o");
        Element priceElement = listItem.selectFirst("p.css-10b0gli.er34gjf0");
        ArrayList<String> sizeAndPricePerMeterArray = new ArrayList<>();
        HashMap<String, String> apartmentInfosHashMap = new HashMap<>();
        String[] labels = {"size","pricePerMeter", "price"};

        if (sizeAndPricePerMeterElement != null) {
            sizeAndPricePerMeterArray.addAll(List.of(sizeAndPricePerMeterElement.text().split("-")));
        }
        if (priceElement != null) {
            sizeAndPricePerMeterArray.add(priceElement.text());
        }

        for (int i = 0; i < labels.length; i++) {
            String textValue = sizeAndPricePerMeterArray.get(i).replaceAll("[^\\d.]", "");
            apartmentInfosHashMap.put(labels[i], textValue);
        }
        return apartmentInfosHashMap;
    }

    private Integer parsePrice(HashMap<String, String> apartmentInfosHashMap) {
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

    private Double parseSize(HashMap<String, String> apartmentInfosHashMap) {
        try {
            String sizeString = apartmentInfosHashMap.get("size");
            return Double.parseDouble(sizeString);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

}
