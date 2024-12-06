package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private int ticketsAvailable;

    private Label ticketsAvailableLabel;

    private Timeline ticketReleaseTimeline;
    private Timeline ticketRetrievalTimeline;

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

        // New buttons for Sign Up, Login, View Customers, View Vendors
        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        signUpButton.setOnAction(e -> showSignUpDialog());

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: orange; -fx-text-fill: white;");
        loginButton.setOnAction(e -> showLoginDialog());

        Button viewCustomersButton = new Button("View Customers");
        viewCustomersButton.setStyle("-fx-background-color: purple; -fx-text-fill: white;");
        viewCustomersButton.setOnAction(e -> showCustomersList());

        Button viewVendorsButton = new Button("View Vendors");
        viewVendorsButton.setStyle("-fx-background-color: teal; -fx-text-fill: white;");
        viewVendorsButton.setOnAction(e -> showVendorsList());

        // Label to display available tickets
        ticketsAvailableLabel = new Label("Tickets Available: 0");
        ticketsAvailableLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");

        // Layout (VBox) setup for the sliders and system controls
        VBox controlLayout = new VBox(10);
        controlLayout.setAlignment(Pos.CENTER);
        controlLayout.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20;");

        controlLayout.getChildren().addAll(
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

        // Layout (HBox) setup for the buttons
        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.TOP_LEFT);
        buttonLayout.setStyle("-fx-padding: 10;");

        buttonLayout.getChildren().addAll(
                signUpButton,
                loginButton,
                viewCustomersButton,
                viewVendorsButton
        );

        // Combining both layouts
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.getChildren().addAll(controlLayout, buttonLayout);

        // Scene and Stage setup
        Scene scene = new Scene(mainLayout, 400, 600);
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
        ticketsAvailable = totalTickets;

        System.out.println("System started with parameters: ");
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Release Rate: " + ticketReleaseRate);
        System.out.println("Retrieval Rate: " + customerRetrievalRate);
        System.out.println("Max Capacity: " + maxTicketCapacity);

        // Update tickets available label initially
        ticketsAvailableLabel.setText("Tickets Available: " + ticketsAvailable);

        // Set up the Timeline for ticket release
        ticketReleaseTimeline = new Timeline(
                new KeyFrame(Duration.millis(ticketReleaseRate), e -> releaseTicket())
        );
        ticketReleaseTimeline.setCycleCount(Timeline.INDEFINITE);

        // Set up the Timeline for customer retrieval
        ticketRetrievalTimeline = new Timeline(
                new KeyFrame(Duration.millis(customerRetrievalRate), e -> retrieveTicket())
        );
        ticketRetrievalTimeline.setCycleCount(Timeline.INDEFINITE);

        // Start the Timelines
        ticketReleaseTimeline.play();
        ticketRetrievalTimeline.play();
    }

    private void stopSystem() {
        // Logic to stop the system
        System.out.println("System stopped.");

        // Stop the Timelines
        if (ticketReleaseTimeline != null) ticketReleaseTimeline.stop();
        if (ticketRetrievalTimeline != null) ticketRetrievalTimeline.stop();
    }

    private void releaseTicket() {
        // Logic to release a ticket, ensuring we don't exceed the max capacity
        if (ticketsAvailable < maxTicketCapacity) {
            ticketsAvailable++;
            ticketsAvailableLabel.setText("Tickets Available: " + ticketsAvailable);
        }
    }

    private void retrieveTicket() {
        // Logic to simulate customer retrieval of a ticket
        if (ticketsAvailable > 0) {
            ticketsAvailable--;
            ticketsAvailableLabel.setText("Tickets Available: " + ticketsAvailable);
        }
    }

    private void showSignUpDialog() {
        // Create a dialog for user to input sign-up details
        TextInputDialog signUpDialog = new TextInputDialog();
        signUpDialog.setTitle("Sign Up");
        signUpDialog.setHeaderText("Please enter your details");
        signUpDialog.setContentText("Enter your username:");

        signUpDialog.showAndWait().ifPresent(username -> {
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Sign Up");
            passwordDialog.setHeaderText("Enter your password");
            passwordDialog.setContentText("Password:");
            passwordDialog.showAndWait().ifPresent(password -> {
                TextInputDialog confirmPasswordDialog = new TextInputDialog();
                confirmPasswordDialog.setTitle("Sign Up");
                confirmPasswordDialog.setHeaderText("Confirm your password");
                confirmPasswordDialog.setContentText("Confirm password:");
                confirmPasswordDialog.showAndWait().ifPresent(confirmPassword -> {
                    // Here, you can add validation to check if passwords match
                    if (password.equals(confirmPassword)) {
                        System.out.println("Sign Up successful!");
                        System.out.println("Username: " + username + ", Password: " + password);
                    } else {
                        System.out.println("Passwords do not match!");
                    }
                });
            });
        });
    }

    private void showLoginDialog() {
        // Create a dialog for user to input login details
        TextInputDialog loginDialog = new TextInputDialog();
        loginDialog.setTitle("Login");
        loginDialog.setHeaderText("Please enter your login details");
        loginDialog.setContentText("Enter your username:");

        loginDialog.showAndWait().ifPresent(username -> {
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Password");

            // You can add a custom dialog for password entry here

            System.out.println("Login successful!");
        });
    }

    private void showCustomersList() {
        // Create a simple list to show customers
        ListView<String> customersList = new ListView<>();
        customersList.getItems().addAll("Customer 1", "Customer 2", "Customer 3");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Customers List");
        alert.getDialogPane().setContent(customersList);
        alert.showAndWait();
    }

    private void showVendorsList() {
        // Create a simple list to show vendors
        ListView<String> vendorsList = new ListView<>();
        vendorsList.getItems().addAll("Vendor 1", "Vendor 2", "Vendor 3");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vendors List");
        alert.getDialogPane().setContent(vendorsList);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
