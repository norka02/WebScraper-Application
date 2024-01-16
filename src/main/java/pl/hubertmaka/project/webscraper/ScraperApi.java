package pl.hubertmaka.project.webscraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.hubertmaka.project.enums.*;

import java.io.IOException;
import java.util.ArrayList;

public class ScraperApi {
    private static final Logger logger = LogManager.getLogger(ScraperApi.class);
    private ArrayList<ApartmentInfo> apartmentInfoArrayList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try {
            ScraperApi scraperApi = new ScraperApi();

            scraperApi.createApartmentInfoArrayList();

            System.out.println(scraperApi.apartmentInfoArrayList.toString());
            for (ApartmentInfo apartmentInfo: scraperApi.apartmentInfoArrayList) {
                System.out.println(apartmentInfo.getAllInfo());
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void createApartmentInfoArrayList() throws IOException, InterruptedException {
        ParserOlx parserOlx = this.createOlxParser();
        Parser parser = this.createOtodomParser();

        ArrayList<Elements> elementsArrayListOtodom = this.getElementsFromSite(parser);
        ArrayList<Elements> elementsArrayListOlx = this.getElementsFromSite(parserOlx);
        ArrayList<ApartmentInfo> apartmentInfoArrayListOtodom = this.createApartmentInfoArrayList(elementsArrayListOtodom, parser);
        ArrayList<ApartmentInfo> apartmentInfoArrayListOlx = this.createApartmentInfoArrayList(elementsArrayListOlx, parserOlx);
        this.apartmentInfoArrayList.addAll(apartmentInfoArrayListOtodom);
        this.apartmentInfoArrayList.addAll(apartmentInfoArrayListOlx);

    }

    private ParserOlx createOlxParser() {
        // TODO: create builder design pattern
        return new ParserOlx(
            PropertyType.APARTMENTS_OLX,
            PurchaseType.FOR_SALE,
            CityType.KRAKOW,
            VoivodeshipType.LESSER_POLAND
        );
    }

    private Parser createOtodomParser() {
        // TODO: create builder design pattern
        return new Parser(
                PropertyType.APARTMENTS,
                PurchaseType.FOR_SALE,
                CityType.KRAKOW,
                VoivodeshipType.LESSER_POLAND,
                Limit.LIMIT_24
        );
    }

    private ArrayList<Elements> getElementsFromSite(Parser parser) throws IOException, InterruptedException {
        ArrayList<Elements> elementsArrayList = new ArrayList<>();
        elementsArrayList.addAll(parser.getAllElementsFromSite(1));
        elementsArrayList.addAll(parser.getAllElementsFromSite(1));
        return  elementsArrayList;
    }

    private ArrayList<Elements> getElementsFromSite(ParserOlx parserOlx) throws IOException, InterruptedException {
        ArrayList<Elements> elementsArrayList = new ArrayList<>();
        elementsArrayList.addAll(parserOlx.getAllElementsFromSite(1));
        elementsArrayList.addAll(parserOlx.getAllElementsFromSite(1));
        return  elementsArrayList;
    }

    private ArrayList<ApartmentInfo> createApartmentInfoArrayList(ArrayList<Elements> elementsArrayList, ParserOlx parserOlx) {
        ArrayList<ApartmentInfo> apartmentInfoArrayList = new ArrayList<>();
        for (Elements listItems : elementsArrayList) {
            for (Element listItem : listItems) {
                apartmentInfoArrayList.add(parserOlx.fillApartmentInfo(
                        new ApartmentInfo(), listItem
                ));
            }
        }
        return apartmentInfoArrayList;
    }

    private ArrayList<ApartmentInfo> createApartmentInfoArrayList(ArrayList<Elements> elementsArrayList, Parser parser) {
        ArrayList<ApartmentInfo> apartmentInfoArrayList = new ArrayList<>();
        for (Elements listItems : elementsArrayList) {
            for (Element listItem : listItems) {
                apartmentInfoArrayList.add(parser.fillApartmentInfo(
                        new ApartmentInfo(), listItem
                ));
            }
        }
        return apartmentInfoArrayList;
    }

}
