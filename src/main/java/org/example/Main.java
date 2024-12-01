package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        TicketPool ticketPool = new TicketPool(10); // Max capacity of 10 tickets

        Label statusLabel = new Label("System Status: Stopped");
        Button startButton = new Button("Start System");
        Button stopButton = new Button("Stop System");
        stopButton.setDisable(true);

        VBox layout = new VBox(10, statusLabel, startButton, stopButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Thread vendorThread = new Thread(new Vendor(ticketPool, 10, 1000, "Vendor1"));
        Thread customerThread = new Thread(new Customer(ticketPool, 2000));

        startButton.setOnAction(event -> {
            vendorThread.start();
            customerThread.start();
            statusLabel.setText("System Status: Running");
            startButton.setDisable(true);
            stopButton.setDisable(false);
        });

        stopButton.setOnAction(event -> {
            vendorThread.interrupt();
            customerThread.interrupt();
            statusLabel.setText("System Status: Stopped");
            stopButton.setDisable(true);
        });

        primaryStage.setTitle("Ticketing System");
        primaryStage.setScene(new Scene(layout, 300, 200));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
