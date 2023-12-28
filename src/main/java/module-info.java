module pl.hubertmaka.webscraper {
    requires javafx.controls;
    requires javafx.fxml;


    opens pl.hubertmaka.webscraper to javafx.fxml;
    exports pl.hubertmaka.webscraper;
}