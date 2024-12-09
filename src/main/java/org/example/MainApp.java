package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {
    private TicketPool ticketPool;
    private Thread[] vendorThreads;
    private Thread[] customerThreads;
    private Label ticketCountLabel;
    private Label ticketPoolStatusLabel;
    private TextArea outputArea;

    private Map<String, User> usersDatabase = new HashMap<>();  // Updated to store user with type
    private List<Vendor> vendorList = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();

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

        // Authentication Buttons (Sign Up and Login)
        Button signUpButton = new Button("Sign Up");
        Button loginButton = new Button("Login");
        HBox authButtons = new HBox(10, signUpButton, loginButton);

        // View Customers and View Vendors Buttons
        Button viewCustomersButton = new Button("View Customers");
        Button viewVendorsButton = new Button("View Vendors");
        HBox viewButtons = new HBox(10, viewCustomersButton, viewVendorsButton);

        VBox root = new VBox(20, statusBox, configGrid, controlBox, outputArea, authButtons, viewButtons);
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
                    Customer customer = new Customer(ticketPool, retrievalRate, ticketCountLabel, ticketPoolStatusLabel, outputArea);
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
                loginStage.close();
            } else {
                showAlert("Login Failed", "Invalid username, password, or user type.");
            }
        });

        Scene loginScene = new Scene(loginLayout, 300, 250);
        loginStage.setScene(loginScene);
        loginStage.initOwner(primaryStage);
        loginStage.show();
    }

    private void showCustomerDetails() {
        StringBuilder customerDetails = new StringBuilder("Customer Details:\n");
        for (Customer customer : customerList) {
            customerDetails.append(customer.toString()).append("\n");
        }
        outputArea.setText(customerDetails.toString());
    }

    private void showVendorDetails() {
        StringBuilder vendorDetails = new StringBuilder("Vendor Details:\n");
        for (Vendor vendor : vendorList) {
            vendorDetails.append(vendor.toString()).append("\n");
        }
        outputArea.setText(vendorDetails.toString());
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

    // User class to store user data with type
    public static class User {
        private String username;
        private String password;
        private String userType;

        public User(String username, String password, String userType) {
            this.username = username;
            this.password = password;
            this.userType = userType;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getUserType() {
            return userType;
        }
    }
}
