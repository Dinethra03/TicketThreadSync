package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainGUI extends Application {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private TicketPool ticketPool;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Real-Time Event Ticketing System");

        // Set up UI elements
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(20));

        // Configuration fields
        TextField totalTicketsField = new TextField();
        totalTicketsField.setPromptText("Enter total tickets");

        TextField maxCapacityField = new TextField();
        maxCapacityField.setPromptText("Enter max capacity");

        TextField ticketReleaseRateField = new TextField();
        ticketReleaseRateField.setPromptText("Enter ticket release rate");

        TextField customerRetrievalRateField = new TextField();
        customerRetrievalRateField.setPromptText("Enter customer retrieval rate");

        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        TextArea logArea = new TextArea();
        logArea.setEditable(false);

        // Add fields to the layout
        root.getChildren().addAll(
                new Label("Total Tickets:"), totalTicketsField,
                new Label("Max Capacity:"), maxCapacityField,
                new Label("Ticket Release Rate:"), ticketReleaseRateField,
                new Label("Customer Retrieval Rate:"), customerRetrievalRateField,
                startButton, stopButton, logArea
        );

        // Start button action
        startButton.setOnAction(event -> {
            int totalTickets = Integer.parseInt(totalTicketsField.getText());
            int maxCapacity = Integer.parseInt(maxCapacityField.getText());
            int ticketReleaseRate = Integer.parseInt(ticketReleaseRateField.getText());
            int customerRetrievalRate = Integer.parseInt(customerRetrievalRateField.getText());

            Configuration config = new Configuration(totalTickets, maxCapacity, ticketReleaseRate, customerRetrievalRate);
            ticketPool = new TicketPool(config.getMaxCapacity());

            // Start Vendor and Customer Threads
            new Thread(() -> runSimulation(config)).start();
        });

        // Stop button action
        stopButton.setOnAction(event -> {
            Platform.exit();
        });

        // Set up the stage
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    private void runSimulation(Configuration config) {
        List<Thread> vendorThreads = new ArrayList<>();
        List<Thread> customerThreads = new ArrayList<>();

        // Start Vendor threads
        for (int i = 1; i <= 6; i++) {
            Vendor vendor = new Vendor(ticketPool, config.getTotalTickets(), config.getTicketReleaseRate(), "Vendor-" + i);
            Thread vendorThread = new Thread(vendor);
            vendorThread.start();
            vendorThreads.add(vendorThread);
        }

        // Start Customer threads
        for (int i = 1; i <= 10; i++) {
            Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate());
            Thread customerThread = new Thread(customer);
            customerThread.setName("Customer-" + i);
            customerThread.start();
            customerThreads.add(customerThread);
        }
    }
}
