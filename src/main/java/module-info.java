module pl.hubertmaka.webscraper {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Eksportowanie pakietów
    exports pl.hubertmaka.project to javafx.graphics;
    exports pl.hubertmaka.project.ui to javafx.fxml;

    // Otwieranie pakietu dla dostępu przez FXMLLoader
    opens pl.hubertmaka.project.ui to javafx.fxml;
}