package org.example;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConfigUI {

    // This method creates and returns the layout for configuration
    public GridPane createConfigLayout(Stage primaryStage) {
        // Create labels and text fields for configuration
        Label totalTicketsLabel = new Label("Total Tickets:");
        Label maxCapacityLabel = new Label("Max Capacity:");
        Label ticketReleaseRateLabel = new Label("Ticket Release Rate:");
        Label customerRetrievalRateLabel = new Label("Customer Retrieval Rate:");

        TextField totalTicketsField = new TextField();
        TextField maxCapacityField = new TextField();
        TextField ticketReleaseRateField = new TextField();
        TextField customerRetrievalRateField = new TextField();

        // Button to submit the configuration
        Button submitButton = new Button("Submit Configuration");

        // Validate input when the submit button is pressed
        submitButton.setOnAction(e -> {
            try {
                int totalTickets = Integer.parseInt(totalTicketsField.getText());
                int maxCapacity = Integer.parseInt(maxCapacityField.getText());
                int ticketReleaseRate = Integer.parseInt(ticketReleaseRateField.getText());
                int customerRetrievalRate = Integer.parseInt(customerRetrievalRateField.getText());

                // Validation: Check if values are positive
                if (totalTickets <= 0 || maxCapacity <= 0 || ticketReleaseRate <= 0 || customerRetrievalRate <= 0) {
                    showErrorAlert("Invalid input", "All values must be positive integers.");
                } else {
                    // If validation passes, create Configuration object
                    Configuration config = new Configuration(totalTickets, maxCapacity, ticketReleaseRate, customerRetrievalRate);
                    System.out.println("Configuration saved: " + config);

                    // Proceed to the next step or save the configuration
                    // Optionally save to a file or continue with the program
                    // config.saveToFile("config.json");

                    // You can now switch to the next scene (main ticketing system)
                    startTicketingSystem(primaryStage, config); // Call this method to start the main system
                }
            } catch (NumberFormatException ex) {
                showErrorAlert("Invalid input", "Please enter valid integer values for all fields.");
            }
        });

        // Set up the layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(totalTicketsLabel, 0, 0);
        gridPane.add(totalTicketsField, 1, 0);
        gridPane.add(maxCapacityLabel, 0, 1);
        gridPane.add(maxCapacityField, 1, 1);
        gridPane.add(ticketReleaseRateLabel, 0, 2);
        gridPane.add(ticketReleaseRateField, 1, 2);
        gridPane.add(customerRetrievalRateLabel, 0, 3);
        gridPane.add(customerRetrievalRateField, 1, 3);
        gridPane.add(submitButton, 0, 4, 2, 1);

        return gridPane;
    }

    // Show error alert
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // This method will be called after valid configuration is entered.
    private void startTicketingSystem(Stage primaryStage, Configuration config) {
        // You can replace this with the logic to start the actual ticketing system.
        // This could be the next UI for vendors, customers, and ticket pool.
        System.out.println("Starting the ticketing system with the following configuration:");
        System.out.println("Total Tickets: " + config.getTotalTickets());
        System.out.println("Max Capacity: " + config.getMaxCapacity());
        System.out.println("Ticket Release Rate: " + config.getTicketReleaseRate());
        System.out.println("Customer Retrieval Rate: " + config.getCustomerRetrievalRate());

        // Optionally, create a new scene here for the ticketing system
        GridPane ticketingSystemLayout = new GridPane(); // Replace with actual layout of the system
        Scene scene = new Scene(ticketingSystemLayout, 600, 400);
        primaryStage.setScene(scene);
    }
}
