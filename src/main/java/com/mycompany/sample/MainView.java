package com.mycompany.sample;

import java.util.ResourceBundle;
import java.io.IOException;

import com.mycompany.sample.javafx.View;
import javafx.fxml.FXMLLoader;

public class MainView {

    public View getView() {
        try {
            View view = FXMLLoader.load(MainView.class.getResource("main.fxml"), ResourceBundle.getBundle("com.mycompany.sample.main"));
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            return new View();
        }
    }

}
