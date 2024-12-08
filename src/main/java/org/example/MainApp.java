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
    private Label ticketCountLabel;
    private Label ticketPoolStatusLabel;
    private TextArea outputArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Real-Time Ticketing System");

        // Ticket Pool Status Section
        VBox statusBox = new VBox(10);
        ticketCountLabel = new Label("Tickets in Pool: 0");
        ticketPoolStatusLabel = new Label("Ticket Pool Status: 0 tickets available.");
        statusBox.getChildren().addAll(ticketCountLabel, ticketPoolStatusLabel);

        // Output Area
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(150);

        // Configuration Inputs
        GridPane configGrid = new GridPane();
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

        // Control Panel
        HBox controlBox = new HBox(10);
        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        controlBox.getChildren().addAll(startButton, stopButton);

        VBox root = new VBox(20, statusBox, configGrid, controlBox, outputArea);
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();

        // Button Actions
        startButton.setOnAction(e -> {
            try {
                int totalTickets = Integer.parseInt(totalTicketsField.getText());
                int maxCapacity = Integer.parseInt(maxCapacityField.getText());
                int releaseRate = Integer.parseInt(releaseRateField.getText());
                int retrievalRate = Integer.parseInt(retrievalRateField.getText());

                ticketPool = new TicketPool(maxCapacity);

                vendorThreads = new Thread[6];
                for (int i = 0; i < 6; i++) {
                    Vendor vendor = new Vendor(ticketPool, totalTickets, releaseRate, "Vendor-" + (i + 1), ticketCountLabel, ticketPoolStatusLabel, outputArea);
                    vendorThreads[i] = new Thread(vendor);
                    vendorThreads[i].start();
                }

                customerThreads = new Thread[10];
                for (int i = 0; i < 10; i++) {
                    Customer customer = new Customer(ticketPool, retrievalRate, ticketCountLabel, ticketPoolStatusLabel, outputArea);
                    customerThreads[i] = new Thread(customer, "Customer-" + (i + 1));
                    customerThreads[i].start();
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers.");
            }
        });

        stopButton.setOnAction(e -> {
            stopThreads(vendorThreads, customerThreads);
        });
    }

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Label getTicketCountLabel() {
        return ticketCountLabel;
    }

    public Label getTicketPoolStatusLabel() {
        return ticketPoolStatusLabel;
    }

    public TextArea getOutputArea() {
        return outputArea;
    }
}
