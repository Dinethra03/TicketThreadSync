package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create Labels
        Label totalTicketsLabel = new Label("Total Tickets");
        Label releaseRateLabel = new Label("Release Rate (ms)");
        Label retrievalRateLabel = new Label("Retrieval Rate (ms)");
        Label maxCapacityLabel = new Label("Max Ticket Capacity");
        Label ticketsAvailableLabel = new Label("Tickets Available: 0");
        ticketsAvailableLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: green;");

        // Create TextFields
        TextField totalTicketsField = new TextField();
        totalTicketsField.setPromptText("Enter Total Tickets");

        TextField releaseRateField = new TextField();
        releaseRateField.setPromptText("Enter Release Rate");

        TextField retrievalRateField = new TextField();
        retrievalRateField.setPromptText("Enter Retrieval Rate");

        TextField maxCapacityField = new TextField();
        maxCapacityField.setPromptText("Enter Max Capacity");

        // Create Buttons
        Button startSystemButton = new Button("Start System");
        startSystemButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");

        Button stopSystemButton = new Button("Stop System");
        stopSystemButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

        Button viewCustomersButton = new Button("View Customers");
        viewCustomersButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");

        Button viewVendorsButton = new Button("View Vendors");
        viewVendorsButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");

        // Arrange layout in a GridPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        // Add components to GridPane
        gridPane.add(totalTicketsLabel, 0, 0);
        gridPane.add(totalTicketsField, 1, 0);
        gridPane.add(releaseRateLabel, 0, 1);
        gridPane.add(releaseRateField, 1, 1);
        gridPane.add(retrievalRateLabel, 0, 2);
        gridPane.add(retrievalRateField, 1, 2);
        gridPane.add(maxCapacityLabel, 0, 3);
        gridPane.add(maxCapacityField, 1, 3);

        // Buttons in a vertical layout
        VBox buttonBox = new VBox(10, startSystemButton, stopSystemButton, viewCustomersButton, viewVendorsButton, loginButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(20, gridPane, ticketsAvailableLabel, buttonBox);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        // Set the Scene and Stage
        Scene scene = new Scene(mainLayout, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Event Ticketing System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
