package pl.hubertmaka.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.hubertmaka.project.ui.MainScreenController;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainPage.fxml"));
            Parent root = loader.load();


            MainScreenController controller = loader.getController();

            primaryStage.setTitle("Apartment Web Scraper");
            primaryStage.setScene(new Scene(root, 1200, 800));

            primaryStage.setOnCloseRequest(event -> {
                logger.info("Exiting application, stopping all threads...");
                controller.stopRunningTasks();
            });

            primaryStage.show();

        } catch (Exception e) {
            logger.error("Error during booting application: ", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
