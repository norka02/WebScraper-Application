package pl.hubertmaka.project.webscraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.hubertmaka.project.enums.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import java.io.IOException;
import java.util.ArrayList;

public class ScraperApi {
    private static final Logger logger = LogManager.getLogger(ScraperApi.class);
    public ArrayList<ApartmentInfo> apartmentInfoArrayList = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public void shutdownExecutorService() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }

    private ArrayList<ApartmentInfo> getApartmentInfoFromSite(Parser parser) {
        ArrayList<ApartmentInfo> apartmentInfos = new ArrayList<>();
        try {
            ArrayList<Elements> elementsArrayList = new ArrayList<>(parser.getAllElementsFromSite(5));
            apartmentInfos = createApartmentInfoArrayList(elementsArrayList, parser);
        } catch (IOException e) {
            logger.error("IOException in retrieving apartment info: ", e);
        } catch (InterruptedException e) {
            logger.info("Scraping interrupted, exiting getApartmentInfoFromSite. " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        return apartmentInfos;
    }

    private ArrayList<ApartmentInfo> getApartmentInfoFromSite(ParserOlx parserOlx) {
        ArrayList<ApartmentInfo> apartmentInfos = new ArrayList<>();
        try {
            ArrayList<Elements> elementsArrayList = new ArrayList<>(parserOlx.getAllElementsFromSite(5));
            apartmentInfos = createApartmentInfoArrayList(elementsArrayList, parserOlx);
        } catch (IOException e) {
            logger.error("IOException in retrieving apartment info: ", e);
        } catch (InterruptedException e) {
            logger.info("Scraping stopped, exiting getApartmentInfoFromSite. " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        return apartmentInfos;
    }

    public void createApartmentInfoArrayList(List<CityType> selectedCities, List<VoivodeshipType> selectedVoivodeships, Map<String, List<String>> normalizedVoivodeshipToCities, PurchaseType purchaseType) {
        apartmentInfoArrayList.clear();

        for (VoivodeshipType voivodeship : selectedVoivodeships) {
            List<String> citiesInVoivodeship = normalizedVoivodeshipToCities.get(voivodeship.getPolishName());
            for (CityType city : selectedCities) {
                if (city != null && citiesInVoivodeship != null && citiesInVoivodeship.contains(city.getPolishName())) {
                    logger.info("Creating parsers for city: " + city + " and voivodeship: " + voivodeship);
                    Parser parser = createOtodomParser(city, voivodeship, purchaseType);
                    ParserOlx parserOlx = createOlxParser(city, voivodeship, purchaseType);

                        apartmentInfoArrayList.addAll(getApartmentInfoFromSite(parser));
                        apartmentInfoArrayList.addAll(getApartmentInfoFromSite(parserOlx));

                } else {
                    logger.warn("City: " + city + " is not in the voivodeship: " + voivodeship + ", skipping...");
                }
            }
        }

        logger.info("Scraping completed. Total apartments scraped: " + apartmentInfoArrayList.size());
    }


    private Parser createOtodomParser(CityType city, VoivodeshipType voivodeship, PurchaseType purchaseType) {
        return new Parser(
                PropertyType.APARTMENTS,
                purchaseType,
                city,
                voivodeship,
                Limit.LIMIT_24
        );
    }

    private ParserOlx createOlxParser(CityType city, VoivodeshipType voivodeship, PurchaseType purchaseType) {
        return new ParserOlx(
                PropertyType.APARTMENTS_OLX,
                purchaseType,
                city,
                voivodeship
        );
    }


    private ArrayList<Elements> getElementsFromSite(Parser parser) throws IOException, InterruptedException {
        return new ArrayList<>(parser.getAllElementsFromSite(1));
    }

    private ArrayList<Elements> getElementsFromSite(ParserOlx parserOlx) throws IOException, InterruptedException {
        return new ArrayList<>(parserOlx.getAllElementsFromSite(1));
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
