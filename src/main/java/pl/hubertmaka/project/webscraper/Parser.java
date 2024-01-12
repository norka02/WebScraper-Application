package pl.hubertmaka.project.webscraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.hubertmaka.project.enums.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser extends Scraper {
    private static final Logger logger = LogManager.getLogger(Parser.class);
    public Parser(PropertyType propertyType, PurchaseType purchaseType, CityType cityType, VoivodeshipType voivodeshipType, Limit limit) {
        super(propertyType, purchaseType, cityType, voivodeshipType, limit);
    }

    public ApartmentInfo fillApartmentInfo(ApartmentInfo apartmentInfoInstance, Element listItem) {
        logger.info("Filling ApartmentInfo instance.");

        apartmentInfoInstance.setTitle(getTitleText(listItem));
        apartmentInfoInstance.setLocation(getLocationText(listItem));
        apartmentInfoInstance.setPrice(getApartmentInfos(listItem).get("price"));
        apartmentInfoInstance.setPricePerMeter(getApartmentInfos(listItem).get("pricePerMeter"));
        apartmentInfoInstance.setRooms(getApartmentInfos(listItem).get("rooms"));
        apartmentInfoInstance.setSize(getApartmentInfos(listItem).get("size"));
        apartmentInfoInstance.setImgSrc(getImgElementText(listItem));
        apartmentInfoInstance.setLinkToAnnouncement(getAElementHref(listItem));
        apartmentInfoInstance.setBoosted(getIsBoostedStatus(listItem));
        apartmentInfoInstance.setAdditionalInfo(getAdditionalInfo(listItem));

        return apartmentInfoInstance;
    }


    public static void main(String[] args) {
        try {
            // TODO: create builder design pattern
            Parser parser = new Parser(
                    PropertyType.APARTMENTS,
                    PurchaseType.FOR_SALE,
                    CityType.KRAKOW,
                    VoivodeshipType.LESSER_POLAND,
                    Limit.LIMIT_72
            );

            ArrayList<Elements> elementsArrayList = parser.getAllElementsFromSite(1);

            ArrayList<ApartmentInfo> apartmentInfoArrayList = new ArrayList<>();
            int counter = 0;

            for (Elements listItems : elementsArrayList) {
                for (Element listItem : listItems) {
                    ApartmentInfo apartmentInfo = parser.fillApartmentInfo(
                            new ApartmentInfo(), listItem
                    );
                    System.out.println("---------------------------------------------------------\n");
                    System.out.println(apartmentInfo.getAllInfo());
                    System.out.println("---------------------------------------------------------\n");
                    apartmentInfoArrayList.add(apartmentInfo);

                    counter++;
                }
            }
            System.out.println(counter);
        } catch (IOException e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
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
            return aElement.attr("href");
        }
        logger.warn("Empty element.");
        return "";
    }

    private Element getAllArticleElement(Element listItem) {
        return listItem.selectFirst("article");
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

    private HashMap<String, String> getApartmentInfos(Element listItem) {
        Elements apartmentInfos = listItem.select("span.css-1cyxwvy.ei6hyam2");

        HashMap<String, String> apartmentInfosHashMap = new HashMap<>();
        String[] labels = {"price", "pricePerMeter", "rooms", "size"};

        int counter = 0;
        for (Element apartmentInfo : apartmentInfos) {
            if (counter < labels.length) {
                apartmentInfosHashMap.put(labels[counter], apartmentInfo.text());
            }
            counter++;
        }
        return apartmentInfosHashMap;
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
