package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    // Declare the sliders as instance variables
    private Slider totalTicketsSlider;
    private Slider releaseRateSlider;
    private Slider retrievalRateSlider;
    private Slider capacitySlider;

    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    @Override
    public void start(Stage primaryStage) {
        // Initialize sliders
        totalTicketsSlider = new Slider(0, 1000, 100); // Range for Total Number of Tickets
        releaseRateSlider = new Slider(100, 1000, 200); // Range for Ticket Release Rate (in ms)
        retrievalRateSlider = new Slider(100, 1000, 200); // Range for Customer Retrieval Rate (in ms)
        capacitySlider = new Slider(10, 500, 50); // Range for Maximum Ticket Capacity

        // TextFields to display the current value of sliders
        TextField totalTicketsField = new TextField("100");
        totalTicketsField.setEditable(false);
        TextField releaseRateField = new TextField("200");
        releaseRateField.setEditable(false);
        TextField retrievalRateField = new TextField("200");
        retrievalRateField.setEditable(false);
        TextField capacityField = new TextField("50");
        capacityField.setEditable(false);

        // Bind text fields to sliders
        totalTicketsSlider.valueProperty().addListener((obs, oldValue, newValue) -> totalTicketsField.setText(String.valueOf(newValue.intValue())));
        releaseRateSlider.valueProperty().addListener((obs, oldValue, newValue) -> releaseRateField.setText(String.valueOf(newValue.intValue())));
        retrievalRateSlider.valueProperty().addListener((obs, oldValue, newValue) -> retrievalRateField.setText(String.valueOf(newValue.intValue())));
        capacitySlider.valueProperty().addListener((obs, oldValue, newValue) -> capacityField.setText(String.valueOf(newValue.intValue())));

        // Buttons for starting/stopping the system
        Button startButton = new Button("Start System");
        startButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        startButton.setOnAction(e -> startSystem());

        Button stopButton = new Button("Stop System");
        stopButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        stopButton.setOnAction(e -> stopSystem());

        // Label to display available tickets
        Label ticketsAvailableLabel = new Label("Tickets Available: 0");
        ticketsAvailableLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");

        // Layout (VBox) setup
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20;");

        layout.getChildren().addAll(
                new Label("Total Number of Tickets"),
                totalTicketsSlider, totalTicketsField,
                new Label("Ticket Release Rate (ms)"),
                releaseRateSlider, releaseRateField,
                new Label("Customer Retrieval Rate (ms)"),
                retrievalRateSlider, retrievalRateField,
                new Label("Maximum Ticket Capacity"),
                capacitySlider, capacityField,
                ticketsAvailableLabel,
                startButton, stopButton
        );

        // Scene and Stage setup
        Scene scene = new Scene(layout, 400, 600);
        primaryStage.setTitle("Event Ticketing System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startSystem() {
        // Logic to start the system with the selected parameters
        totalTickets = (int) totalTicketsSlider.getValue();
        ticketReleaseRate = (int) releaseRateSlider.getValue();
        customerRetrievalRate = (int) retrievalRateSlider.getValue();
        maxTicketCapacity = (int) capacitySlider.getValue();

        System.out.println("System started with parameters: ");
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Release Rate: " + ticketReleaseRate);
        System.out.println("Retrieval Rate: " + customerRetrievalRate);
        System.out.println("Max Capacity: " + maxTicketCapacity);
    }

    private void stopSystem() {
        // Logic to stop the system
        System.out.println("System stopped.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

