package pl.hubertmaka.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.hubertmaka.project.ui.MainScreenController;
import pl.hubertmaka.project.webscraper.ScraperApi;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private ScraperApi scraperApi;
    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainPage.fxml"));
            Parent root = loader.load();
            MainScreenController controller = loader.getController();
//            scraperApi = controller.getScraperApi();

            primaryStage.setTitle("Moja Aplikacja Web Scraper");
            primaryStage.setScene(new Scene(root, 1400, 1000));

            primaryStage.setOnCloseRequest(this::handleCloseRequest);
            primaryStage.show();

        } catch (Exception e) {
            logger.error("Wystąpił błąd podczas uruchamiania aplikacji: ", e);
        }
    }
    private void handleCloseRequest(WindowEvent event) {
        // Zamknij wątki w ScraperApi
        if (scraperApi != null) {
            scraperApi.shutdownExecutorService();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
