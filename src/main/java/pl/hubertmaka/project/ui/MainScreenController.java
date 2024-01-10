package pl.hubertmaka.project.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainScreenController {
    private static final Logger logger = LogManager.getLogger(MainScreenController.class);
    private int count = 0;
    @FXML
    private Label labelText;

    @FXML
    protected void onButtonClick() {
        System.out.println(count + ": " + "Java WebScraper Application");
        count++;
        logger.info("Button Clicked and counter increased");

    }
}
