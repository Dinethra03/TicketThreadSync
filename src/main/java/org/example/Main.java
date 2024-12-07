package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ticketing System");

        // Create an instance of ConfigUI to get the configuration layout
        ConfigUI configUI = new ConfigUI();  // Make sure ConfigUI is in the same package or import it if it's in a different package

        // Create the configuration scene
        Scene configScene = new Scene(configUI.createConfigLayout(primaryStage), 400, 300);

        // Set the scene for the primaryStage to show the configuration form
        primaryStage.setScene(configScene);
        primaryStage.show();
    }
}
