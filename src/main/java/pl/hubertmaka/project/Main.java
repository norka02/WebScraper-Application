package pl.hubertmaka.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;


public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);
    @Override
    public void start(Stage primaryStage) {
        try {
            logger.debug("Debug message log.");
            // Ładowanie pliku FXML
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainPage.fxml")));

            // Ustawianie sceny
            primaryStage.setTitle("My Web Scraper Application");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch(Exception e) {
            logger.error(e.toString(), new RuntimeException("App Exception"));
        }
    }

    public static void main(String[] args) {
        runApp(args);
    }

    public static void runApp(String[] args) {
        logger.info("Launching app.");
        launch(args);

    }
}