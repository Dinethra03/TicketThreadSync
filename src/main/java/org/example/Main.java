package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {
    private ExecutorService vendorExecutor;
    private ExecutorService customerExecutor;

    @Override
    public void start(Stage primaryStage) {
        // UI Components
        TextField totalTicketsField = new TextField();
        totalTicketsField.setPromptText("Total Tickets");

        TextField maxCapacityField = new TextField();
        maxCapacityField.setPromptText("Max Capacity");

        TextField releaseRateField = new TextField();
        releaseRateField.setPromptText("Release Rate (ms)");

        TextField retrievalRateField = new TextField();
        retrievalRateField.setPromptText("Retrieval Rate (ms)");

        Button startButton = new Button("Start System");
        Button stopButton = new Button("Stop System");
        stopButton.setDisable(true);

        Label statusLabel = new Label("System Status: Stopped");
        Label ticketCountLabel = new Label("Tickets Available: 0");

        VBox layout = new VBox(10, totalTicketsField, maxCapacityField, releaseRateField, retrievalRateField, statusLabel, ticketCountLabel, startButton, stopButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Start button action
        startButton.setOnAction(event -> {
            try {
                int totalTickets = Integer.parseInt(totalTicketsField.getText());
                int maxCapacity = Integer.parseInt(maxCapacityField.getText());
                int releaseRate = Integer.parseInt(releaseRateField.getText());
                int retrievalRate = Integer.parseInt(retrievalRateField.getText());

                Configuration config = new Configuration(totalTickets, maxCapacity, releaseRate, retrievalRate);
                TicketPool ticketPool = new TicketPool(config.getMaxCapacity());

                vendorExecutor = Executors.newSingleThreadExecutor();
                customerExecutor = Executors.newSingleThreadExecutor();

                Vendor vendor = new Vendor(ticketPool, config.getTotalTickets(), config.getReleaseRate(), "Vendor1");
                Customer customer = new Customer(ticketPool, config.getRetrievalRate());

                vendorExecutor.submit(vendor);
                customerExecutor.submit(customer);

                vendorExecutor.submit(() -> {
                    while (!vendorExecutor.isShutdown()) {
                        Platform.runLater(() -> ticketCountLabel.setText("Tickets Available: " + ticketPool.getTicketCount()));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });

                statusLabel.setText("System Status: Running");
                startButton.setDisable(true);
                stopButton.setDisable(false);
            } catch (NumberFormatException e) {
                statusLabel.setText("Invalid input. Please check values.");
            }
        });

        // Stop button action
        stopButton.setOnAction(event -> {
            if (vendorExecutor != null) vendorExecutor.shutdownNow();
            if (customerExecutor != null) customerExecutor.shutdownNow();
            statusLabel.setText("System Status: Stopped");
            stopButton.setDisable(true);
            startButton.setDisable(false);
        });

        primaryStage.setTitle("Ticketing System");
        primaryStage.setScene(new Scene(layout, 400, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}