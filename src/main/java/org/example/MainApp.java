package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.ArrayList;

public class MainApp extends Application {
    private TicketPool ticketPool;
    private Thread[] vendorThreads;
    private Thread[] customerThreads;
    private Label ticketCountLabel;
    private Label ticketPoolStatusLabel;
    private TextArea outputArea;

    private Map<String, User> usersDatabase = new HashMap<>();  // Stores users with type
    private List<Vendor> vendorList = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();

    private Button bookTicketButton;
    private Button showTicketPricesButton;
    private Label ticketPriceLabel;

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
        startButton.setStyle("-fx-background-color:  #4CAF50; -fx-text-fill: white; ");
        Button stopButton = new Button("Stop");
        stopButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        controlBox.getChildren().addAll(startButton, stopButton);

        // Authentication Buttons (Sign Up and Login)
        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
        HBox authButtons = new HBox(10, signUpButton, loginButton);

        // View Customers and View Vendors Buttons
        Button viewCustomersButton = new Button("View Customers");
        viewCustomersButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;");
        Button viewVendorsButton = new Button("View Vendors");
        viewVendorsButton.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white;");
        HBox viewButtons = new HBox(10, viewCustomersButton, viewVendorsButton);

        // Customer actions
        bookTicketButton = new Button("Book Ticket");
        bookTicketButton.setStyle("-fx-background-color: #3F51B5; -fx-text-fill: white;");
        showTicketPricesButton = new Button("Show Ticket Prices");
        showTicketPricesButton.setStyle("-fx-background-color: #3F51B5; -fx-text-fill: white;");
        bookTicketButton.setDisable(true);
        showTicketPricesButton.setDisable(true);
        ticketPriceLabel = new Label("Ticket Prices: N/A");

        // Customer options
        HBox customerOptionsBox = new HBox(10, bookTicketButton, showTicketPricesButton, ticketPriceLabel);

        VBox root = new VBox(20, statusBox, configGrid, controlBox, outputArea, authButtons, viewButtons, customerOptionsBox);
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
                    vendorList.add(vendor);
                    vendorThreads[i] = new Thread(vendor);
                    vendorThreads[i].start();
                }

                customerThreads = new Thread[10];
                for (int i = 0; i < 10; i++) {
                    Customer customer = new Customer(ticketPool, retrievalRate, ticketCountLabel, ticketPoolStatusLabel, outputArea, "Customer-" + (i + 1));
                    customerList.add(customer);
                    customerThreads[i] = new Thread(customer, "Customer-" + (i + 1));
                    customerThreads[i].start();
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers.");
            }
        });

        stopButton.setOnAction(e -> stopThreads(vendorThreads, customerThreads));

        // Sign Up and Login Button Handlers
        signUpButton.setOnAction(e -> showSignUpDialog(primaryStage));
        loginButton.setOnAction(e -> showLoginDialog(primaryStage));

        // View Customers and View Vendors Button Handlers
        viewCustomersButton.setOnAction(e -> showCustomerDetails());
        viewVendorsButton.setOnAction(e -> showVendorDetails());

        // Customer actions (Book Ticket and Show Ticket Prices)
        bookTicketButton.setOnAction(e -> bookTicket());
        showTicketPricesButton.setOnAction(e -> showTicketPrices());
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

    private void showSignUpDialog(Stage primaryStage) {
        Stage signUpStage = new Stage();
        signUpStage.setTitle("Sign Up");

        VBox signUpLayout = new VBox(10);
        signUpLayout.setStyle("-fx-padding: 15; -fx-alignment: center;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("Customer", "Vendor");
        userTypeComboBox.setPromptText("Select User Type");

        Button submitButton = new Button("Sign Up");

        signUpLayout.getChildren().addAll(
                new Label("Sign Up"),
                usernameField,
                passwordField,
                userTypeComboBox,
                submitButton
        );

        submitButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String userType = userTypeComboBox.getValue();

            if (!username.isEmpty() && !password.isEmpty() && userType != null) {
                // Save user data with type
                usersDatabase.put(username, new User(username, password, userType));
                outputArea.appendText("New User Registered: " + username + " (" + userType + ")\n");
                signUpStage.close();
            } else {
                showAlert("Invalid Input", "Please fill in all fields.");
            }
        });

        Scene signUpScene = new Scene(signUpLayout, 300, 250);
        signUpStage.setScene(signUpScene);
        signUpStage.initOwner(primaryStage);
        signUpStage.show();
    }

    private void showLoginDialog(Stage primaryStage) {
        Stage loginStage = new Stage();
        loginStage.setTitle("Login");

        VBox loginLayout = new VBox(10);
        loginLayout.setStyle("-fx-padding: 15; -fx-alignment: center;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("Customer", "Vendor");
        userTypeComboBox.setPromptText("Select User Type");

        Button loginButton = new Button("Login");

        loginLayout.getChildren().addAll(
                new Label("Login"),
                usernameField,
                passwordField,
                userTypeComboBox,
                loginButton
        );

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String userType = userTypeComboBox.getValue();

            if (usersDatabase.containsKey(username) && usersDatabase.get(username).getPassword().equals(password) && usersDatabase.get(username).getUserType().equals(userType)) {
                outputArea.appendText("User Logged In: " + username + " (" + userType + ")\n");
                if ("Customer".equals(userType)) {
                    bookTicketButton.setDisable(false);
                    showTicketPricesButton.setDisable(false);
                }
                loginStage.close();
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }
        });

        Scene loginScene = new Scene(loginLayout, 300, 250);
        loginStage.setScene(loginScene);
        loginStage.initOwner(primaryStage);
        loginStage.show();
    }

    private void showCustomerDetails() {
        outputArea.appendText("Customers: " + customerList.size() + "\n");
        for (Customer customer : customerList) {
            outputArea.appendText(customer.toString() + "\n");
        }
    }

    private void showVendorDetails() {

        for (Vendor vendor : vendorList) {
            outputArea.appendText(vendor.toString() + "\n");
        }
    }

    private void bookTicket() {
        // Implement your ticket booking logic here
        outputArea.appendText("Ticket booked!\n");
    }

    private void showTicketPrices() {
        // Assume you have a ticket price variable or method to calculate the price
        double ticketPrice = 50.0; // or calculate this dynamically if needed

        // Update the label with the actual price
        ticketPriceLabel.setText("Ticket Prices: $" + ticketPrice);
        outputArea.appendText("Ticket prices displayed!\n");
    }

}
