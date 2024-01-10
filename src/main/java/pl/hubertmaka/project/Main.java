package pl.hubertmaka.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Ładowanie pliku FXML
            Parent root = FXMLLoader.load(getClass().getResource("/MainPage.fxml"));

            // Ustawianie sceny
            primaryStage.setTitle("My Web Scraper Application");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch(Exception e) {
           e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}