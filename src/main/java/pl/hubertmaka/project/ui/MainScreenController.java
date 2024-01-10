package pl.hubertmaka.project.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainScreenController {
    private int count = 0;
    @FXML
    private Label labelText;

    @FXML
    protected void onButtonClick() {
        System.out.println(count + ": " + "Java WebScraper Application");
        count++;
    }
}
