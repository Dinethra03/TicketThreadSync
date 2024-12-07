package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ticketing System");

        // Show the Configuration UI first
        ConfigUI configUI = new ConfigUI();
        configUI.start(primaryStage);  // Pass the primaryStage to ConfigUI

        // If the configuration is saved, you can move on to start the system
        // Add more code to start the vendor/customer threads and ticket pool
    }

}
