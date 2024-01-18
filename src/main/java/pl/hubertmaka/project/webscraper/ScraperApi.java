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

    private ArrayList<ApartmentInfo> getApartmentInfoFromSite(Parser parser) throws IOException, InterruptedException {
        logger.info("Getting apartment info from site using Parser: " + parser.getClass().getSimpleName());
        ArrayList<Elements> elementsArrayList = new ArrayList<>(parser.getAllElementsFromSite(5));
        return createApartmentInfoArrayList(elementsArrayList, parser);
    }

    private ArrayList<ApartmentInfo> getApartmentInfoFromSite(ParserOlx parserOlx) throws IOException, InterruptedException {
        logger.info("Getting apartment info from site using ParserOlx: " + parserOlx.getClass().getSimpleName());
        ArrayList<Elements> elementsArrayList = new ArrayList<>(parserOlx.getAllElementsFromSite(5));
        return createApartmentInfoArrayList(elementsArrayList, parserOlx);
    }

    public void createApartmentInfoArrayList(List<CityType> selectedCities, List<VoivodeshipType> selectedVoivodeships, Map<String, List<String>> voivodeshipToCities) throws IOException, InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        ArrayList<Future<ArrayList<ApartmentInfo>>> futures = new ArrayList<>();

        for (VoivodeshipType voivodeship : selectedVoivodeships) {
            List<String> citiesInVoivodeship = voivodeshipToCities.get(voivodeship.getPolishName());
            for (CityType city : selectedCities) {
                if (citiesInVoivodeship != null && citiesInVoivodeship.contains(city.getPolishName())) {
                    logger.info("Creating parsers for city: " + city + " and voivodeship: " + voivodeship);
                    Parser parser = createOtodomParser(city, voivodeship);
                    ParserOlx parserOlx = createOlxParser(city, voivodeship);

                    futures.add(executor.submit(() -> getApartmentInfoFromSite(parser)));
                    futures.add(executor.submit(() -> getApartmentInfoFromSite(parserOlx)));
                } else {
                    logger.warn("City: " + city + " is not in the voivodeship: " + voivodeship + ", skipping...");
                }
            }
        }

        for (Future<ArrayList<ApartmentInfo>> future : futures) {
            try {
                apartmentInfoArrayList.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Error in retrieving apartment info: ", e);
            }
        }

        executor.shutdown();
        logger.info("Scraping completed. Total apartments scraped: " + apartmentInfoArrayList.size());
    }

    private Parser createOtodomParser(CityType city, VoivodeshipType voivodeship) {
        return new Parser(
                PropertyType.APARTMENTS,
                PurchaseType.FOR_SALE,
                city,
                voivodeship,
                Limit.LIMIT_24
        );
    }

    private ParserOlx createOlxParser(CityType city, VoivodeshipType voivodeship) {
        return new ParserOlx(
                PropertyType.APARTMENTS_OLX,
                PurchaseType.FOR_SALE,
                city,
                voivodeship
        );
    }


    private ArrayList<Elements> getElementsFromSite(Parser parser) throws IOException, InterruptedException {
        return new ArrayList<>(parser.getAllElementsFromSite(5));
    }

    private ArrayList<Elements> getElementsFromSite(ParserOlx parserOlx) throws IOException, InterruptedException {
        return new ArrayList<>(parserOlx.getAllElementsFromSite(5));
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
