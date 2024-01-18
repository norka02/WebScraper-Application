package pl.hubertmaka.project.webscraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.hubertmaka.project.enums.*;
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
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }



    public void createApartmentInfoArrayList() throws IOException, InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(2); // Utwórz pulę wątków z dwoma wątkami

        ParserOlx parserOlx = this.createOlxParser();
        Parser parser = this.createOtodomParser();

        // Utwórz zadania
        Future<ArrayList<Elements>> futureOtodom = executor.submit(() -> getElementsFromSite(parser));
        Future<ArrayList<Elements>> futureOlx = executor.submit(() -> getElementsFromSite(parserOlx));

        // Pobierz wyniki
        ArrayList<Elements> elementsArrayListOtodom = futureOtodom.get();
        ArrayList<Elements> elementsArrayListOlx = futureOlx.get();

        // Zamknij ExecutorService
        executor.shutdown();

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
