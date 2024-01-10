module pl.hubertmaka.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    // Eksportowanie pakietów
    exports pl.hubertmaka.project to javafx.graphics;
    exports pl.hubertmaka.project.ui to javafx.fxml;

    // Otwieranie pakietu dla dostępu przez FXMLLoader
    opens pl.hubertmaka.project.ui to javafx.fxml;
    // Otwieranie pakietu dla dostępu do log4j2
    opens pl.hubertmaka.project to org.apache.logging.log4j;
}