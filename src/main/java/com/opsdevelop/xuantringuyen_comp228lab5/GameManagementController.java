package com.opsdevelop.xuantringuyen_comp228lab5;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameManagementController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}