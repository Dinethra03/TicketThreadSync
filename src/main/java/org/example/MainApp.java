package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApp extends Application {
    private TicketPool ticketPool;
    private Thread[] vendorThreads;
    private Thread[] customerThreads;
    private Label ticketCountLabel;  // UI Label for ticket count
    private TextArea outputArea;  // Output Area to simulate console output

    public static void main(String[] args) {
        launch(args); // Start the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Real-Time Ticketing System");

        // Ticket Pool Status Section
        VBox statusBox = new VBox(10);
        Label ticketStatusLabel = new Label("Ticket Pool Status:");
        ticketCountLabel = new Label("Tickets in Pool: 0"); // Real-time updates
        statusBox.getChildren().addAll(ticketStatusLabel, ticketCountLabel);

        // Output Area (simulating CLI output)
        outputArea = new TextArea();
        outputArea.setEditable(false);  // Make the output area read-only
        outputArea.setPrefHeight(150);

        // Configuration Inputs Section
        GridPane configGrid = new GridPane();
        configGrid.setHgap(10);
        configGrid.setVgap(10);
        Label totalTicketsLabel = new Label("Total Tickets:");
        TextField totalTicketsField = new TextField();
        Label maxCapacityLabel = new Label("Max Capacity:");
        TextField maxCapacityField = new TextField();
        Label releaseRateLabel = new Label("Release Rate:");
        TextField releaseRateField = new TextField();
        Label retrievalRateLabel = new Label("Retrieval Rate:");
        TextField retrievalRateField = new TextField();

        configGrid.addRow(0, totalTicketsLabel, totalTicketsField);
        configGrid.addRow(1, maxCapacityLabel, maxCapacityField);
        configGrid.addRow(2, releaseRateLabel, releaseRateField);
        configGrid.addRow(3, retrievalRateLabel, retrievalRateField);

        // Control Panel Section
        HBox controlBox = new HBox(10);
        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        Button exitButton = new Button("Exit");
        controlBox.getChildren().addAll(startButton, stopButton, exitButton);

        // Root Layout
        VBox root = new VBox(20, statusBox, configGrid, controlBox, outputArea);
        Scene scene = new Scene(root, 600, 600);

        primaryStage.setScene(scene);
        primaryStage.show();

        // Button Actions
        startButton.setOnAction(e -> {
            try {
                // Get configuration values from the fields
                int totalTickets = Integer.parseInt(totalTicketsField.getText());
                int maxCapacity = Integer.parseInt(maxCapacityField.getText());
                int releaseRate = Integer.parseInt(releaseRateField.getText());
                int retrievalRate = Integer.parseInt(retrievalRateField.getText());

                // Initialize TicketPool
                ticketPool = new TicketPool(maxCapacity);

                // Start Vendor Threads
                vendorThreads = new Thread[6];
                for (int i = 0; i < 6; i++) {
                    Vendor vendor = new Vendor(ticketPool, totalTickets, releaseRate, "Vendor-" + (i + 1), ticketCountLabel, outputArea);
                    vendorThreads[i] = new Thread(vendor);
                    vendorThreads[i].start();
                }

                // Start Customer Threads
                customerThreads = new Thread[10];
                for (int i = 0; i < 10; i++) {
                    Customer customer = new Customer(ticketPool, retrievalRate, ticketCountLabel, outputArea);
                    customerThreads[i] = new Thread(customer, "Customer-" + (i + 1));
                    customerThreads[i].start();
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers for all fields.");
            }
        });

        stopButton.setOnAction(e -> stopThreads(vendorThreads, customerThreads));
        exitButton.setOnAction(e -> Platform.exit());
    }

    // Helper to stop threads
    private void stopThreads(Thread[]... threadGroups) {
        for (Thread[] threads : threadGroups) {
            if (threads != null) {
                for (Thread thread : threads) {
                    if (thread != null) {
                        thread.interrupt();
                    }
                }
            }
        }
    }

    // Helper for alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to access ticketCountLabel from MainApp
    public Label getTicketCountLabel() {
        return ticketCountLabel;
    }

    // Method to access outputArea from MainApp
    public TextArea getOutputArea() {
        return outputArea;
    }
}
