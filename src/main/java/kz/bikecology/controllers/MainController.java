package kz.bikecology.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainController {

    int clickCounter = 0;

    @FXML
    private Button btn;

    @FXML
    private void click(ActionEvent event) {
        btn.setText(String.valueOf(clickCounter));
        clickCounter++;
    }
}