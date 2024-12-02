package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private ExecutorService vendorExecutor;
    private ExecutorService customerExecutor;

    @Override
    public void start(Stage primaryStage) {
        // Logging the start of the system
        logger.info("Ticketing system initialized.");

        // Create input fields for dynamic configuration
        TextField totalTicketsField = new TextField();
        totalTicketsField.setPromptText("Total Tickets");

        TextField maxCapacityField = new TextField();
        maxCapacityField.setPromptText("Max Capacity");

        TextField releaseRateField = new TextField();
        releaseRateField.setPromptText("Release Rate (ms)");

        TextField retrievalRateField = new TextField();
        retrievalRateField.setPromptText("Retrieval Rate (ms)");

        // Create buttons
        Button startButton = new Button("Start System");
        Button stopButton = new Button("Stop System");
        stopButton.setDisable(true);

        Label statusLabel = new Label("System Status: Stopped");
        Label ticketCountLabel = new Label("Tickets Available: 0");

        // Layout setup
        VBox layout = new VBox(10, totalTicketsField, maxCapacityField, releaseRateField, retrievalRateField, statusLabel, ticketCountLabel, startButton, stopButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Button action for starting the system
        startButton.setOnAction(event -> {
            try {
                // Parse input values
                int totalTickets = Integer.parseInt(totalTicketsField.getText());
                int maxCapacity = Integer.parseInt(maxCapacityField.getText());
                int releaseRate = Integer.parseInt(releaseRateField.getText());
                int retrievalRate = Integer.parseInt(retrievalRateField.getText());

                // Create Configuration object
                Configuration config = new Configuration(totalTickets, maxCapacity, releaseRate, retrievalRate);
                TicketPool ticketPool = new TicketPool(config.getMaxCapacity());

                // Create ExecutorService for Vendor and Customer threads
                vendorExecutor = Executors.newFixedThreadPool(2); // Adjust based on the number of vendors
                customerExecutor = Executors.newFixedThreadPool(2); // Adjust based on the number of customers

                // Start Vendor and Customer threads
                Vendor vendor = new Vendor(ticketPool, config.getTotalTickets(), config.getReleaseRate(), "Vendor1");
                Customer customer = new Customer(ticketPool, config.getRetrievalRate());
                vendorExecutor.submit(vendor);
                customerExecutor.submit(customer);

                // Update ticket count dynamically
                vendorExecutor.submit(() -> {
                    while (!vendorExecutor.isShutdown()) {
                        Platform.runLater(() -> ticketCountLabel.setText("Tickets Available: " + ticketPool.getTicketCount()));
                        try {
                            Thread.sleep(1000); // Update every second
                        } catch (InterruptedException e) {
                            logger.warn("Ticket count update thread interrupted.");
                            Thread.currentThread().interrupt();
                        }
                    }
                });

                // Update UI status
                statusLabel.setText("System Status: Running");
                startButton.setDisable(true);
                stopButton.setDisable(false);
                logger.info("System started successfully.");
            } catch (NumberFormatException e) {
                statusLabel.setText("Invalid input, please check values.");
                logger.error("Invalid input for configuration values.", e);
            }
        });

        // Button action for stopping the system
        stopButton.setOnAction(event -> {
            if (vendorExecutor != null) vendorExecutor.shutdownNow();
            if (customerExecutor != null) customerExecutor.shutdownNow();
            statusLabel.setText("System Status: Stopped");
            stopButton.setDisable(true);
            startButton.setDisable(false);
            logger.info("System stopped.");
        });

        // Set up the primary stage
        primaryStage.setTitle("Ticketing System");
        primaryStage.setScene(new Scene(layout, 400, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
