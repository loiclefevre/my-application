package com.mycompany.sample;

import java.util.Random;
import java.util.ResourceBundle;

import com.mycompany.sample.javafx.View;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class MainPresenter {

    private final String javafxVersion = System.getProperty("javafx.version");

    @FXML
    private View main;

    @FXML
    private Label color;

    @FXML
    private Label label;

    @FXML
    private ResourceBundle resources;

    public void initialize() {
        main.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
            }
        });
        label.setText(String.format(resources.getString("label.text"), "JavaFX", javafxVersion));
    }

    @FXML
    private void updateColor() {
        Random random = new Random();
        this.color.setTextFill(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
    }
}
