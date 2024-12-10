package org.example;

import javafx.application.Application; //Importing the JavaFx Application basic class
import javafx.scene.Scene; //Importing Scene for the JavaFx GUI
import javafx.scene.control.*; //Importing JavaFx controls like TextField,TextArea, etc.
import javafx.scene.layout.*; //Importing layouts like VBox, HBox, GridPane
import javafx.stage.Stage; //Importing Stage for window management
import javafx.scene.control.TextArea; //To allows multi-line text editing and display
import javafx.scene.layout.VBox; //Importing VBox layout which organizes child notes vertically

import java.util.*;
import java.util.ArrayList;

public class MainApp extends Application { //Main JavaFx application class extendind Application
    private TicketPool ticketPool;

    //Threads for vendor and customer for operations
    private Thread[] vendorThreads;
    private Thread[] customerThreads;

    //Lable for display ticket count , ticket pool status
    private Label ticketCountLabel;
    private Label ticketPoolStatusLabel;

    //Area to display logs or messages
    private TextArea outputArea;

    private Map<String, User> usersDatabase = new HashMap<>();  // Stores user data with their type
    //List of vendor and customer objects
    private List<Vendor> vendorList = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();

    private Button bookTicketButton;
    private Button showTicketPricesButton;
    private Label ticketPriceLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) { //Entry point for JavaFx application
        primaryStage.setTitle("Real-Time Ticketing System");

        // Ticket Pool Status Section
        VBox statusBox = new VBox(10); //Vertical layout for ticket status labels
        ticketCountLabel = new Label("Tickets in Pool: 0");
        ticketPoolStatusLabel = new Label("Ticket Pool Status: 0 tickets available.");
        statusBox.getChildren().addAll(ticketCountLabel, ticketPoolStatusLabel); //Adding labels to layout

        // Output Area
        outputArea = new TextArea(); //Text area for displaying logs
        outputArea.setEditable(false); //Making it read-only
        outputArea.setPrefHeight(150); //Setting height

        // Configuration Inputs
        GridPane configGrid = new GridPane(); //Grid layout for input fields
        Label totalTicketsLabel = new Label("Total Tickets:");
        TextField totalTicketsField = new TextField(); //Input field for total tickets
        Label maxCapacityLabel = new Label("Max Capacity:");
        TextField maxCapacityField = new TextField(); //Input field for max capacity
        Label releaseRateLabel = new Label("Release Rate:");
        TextField releaseRateField = new TextField(); //Input field for release rate
        Label retrievalRateLabel = new Label("Retrieval Rate:");
        TextField retrievalRateField = new TextField(); //Input field for retrieval rate


        //Adding total tickets , max capacity , release rate , retrieval rate rowa
        configGrid.addRow(0, totalTicketsLabel, totalTicketsField);
        configGrid.addRow(1, maxCapacityLabel, maxCapacityField);
        configGrid.addRow(2, releaseRateLabel, releaseRateField);
        configGrid.addRow(3, retrievalRateLabel, retrievalRateField);

        // Control Panel
        HBox controlBox = new HBox(10); //Horizontal layout for control buttons
        Button startButton = new Button("Start"); // The start button to boot up the system
        startButton.setStyle("-fx-background-color:  #4CAF50; -fx-text-fill: white; ");
        Button stopButton = new Button("Stop"); //The stop button to terminate activities
        stopButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        controlBox.getChildren().addAll(startButton, stopButton); //Adding buttons to layout

        // Authentication Buttons (Sign Up and Login)
        Button signUpButton = new Button("Sign Up"); //Sign-up button for new users
        signUpButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
        Button loginButton = new Button("Login"); //Loging button for existing users
        loginButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white;");
        HBox authButtons = new HBox(10, signUpButton, loginButton); //Layout for authentication buttons

        // View Customers and View Vendors Buttons
        Button viewCustomersButton = new Button("View Customers"); //Button to view customer list
        viewCustomersButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;");
        Button viewVendorsButton = new Button("View Vendors"); //Button to view vendor list
        viewVendorsButton.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white;");
        HBox viewButtons = new HBox(10, viewCustomersButton, viewVendorsButton); //Layout for viewing buttons

        // Customer actions
        bookTicketButton = new Button("Book Ticket");
        bookTicketButton.setStyle("-fx-background-color: #3F51B5; -fx-text-fill: white;");
        showTicketPricesButton = new Button("Show Ticket Prices");
        showTicketPricesButton.setStyle("-fx-background-color: #3F51B5; -fx-text-fill: white;");
        bookTicketButton.setDisable(true); //Initially disabled till log in
        showTicketPricesButton.setDisable(true); //Intially disabled till log in
        ticketPriceLabel = new Label("Ticket Prices: N/A"); //Placeholder for ticket prices

        // Customer options - organizes all sections vertically
        HBox customerOptionsBox = new HBox(10, bookTicketButton, showTicketPricesButton, ticketPriceLabel);

        VBox root = new VBox(20, statusBox, configGrid, controlBox, outputArea, authButtons, viewButtons, customerOptionsBox);
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();

        // Button Actions - Define the behavior when button are clicked
        startButton.setOnAction(e -> {
            try {

                //Parse the input fields to set up the ticket system
                int totalTickets = Integer.parseInt(totalTicketsField.getText());
                int maxCapacity = Integer.parseInt(maxCapacityField.getText());
                int releaseRate = Integer.parseInt(releaseRateField.getText());
                int retrievalRate = Integer.parseInt(retrievalRateField.getText());

                ticketPool = new TicketPool(maxCapacity); //Initialize the ticket pool with the maximum capacity

                vendorThreads = new Thread[6]; //Create threads for 6 vendors
                for (int i = 0; i < 6; i++) {
                    Vendor vendor = new Vendor(ticketPool, totalTickets, releaseRate, "Vendor-" + (i + 1), ticketCountLabel, ticketPoolStatusLabel, outputArea);
                    vendorList.add(vendor); //Add vendor to the list
                    vendorThreads[i] = new Thread(vendor); //Craete a thread for the vendor
                    vendorThreads[i].start(); //Start the vendor thread
                }

                customerThreads = new Thread[10]; //Create threads for 10 customers
                for (int i = 0; i < 10; i++) {
                    Customer customer = new Customer(ticketPool, retrievalRate, ticketCountLabel, ticketPoolStatusLabel, outputArea, "Customer-" + (i + 1));
                    customerList.add(customer); //Add customer to the list
                    customerThreads[i] = new Thread(customer, "Customer-" + (i + 1));
                    customerThreads[i].start(); //Start the customer thread
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numbers.");
            }
        });

        stopButton.setOnAction(e -> stopThreads(vendorThreads, customerThreads)); //Stop the threads when stop button is clicked

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

    private void stopThreads(Thread[]... threadGroups) { //Method for stopping all threads (vendors and customers)
        for (Thread[] threads : threadGroups) {
            if (threads != null) {
                for (Thread thread : threads) {
                    if (thread != null) {
                        thread.interrupt(); //Interrupt each thread to stop them
                    }
                }
            }
        }
    }

    private void showAlert(String title, String message) { //Method to display an alert dialog
        Alert alert = new Alert(Alert.AlertType.ERROR); //Create an error alert
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait(); //Show the alert and wait for the user to close it
    }

    private void showSignUpDialog(Stage primaryStage) {
        Stage signUpStage = new Stage(); //Create a new window for sign-up
        signUpStage.setTitle("Sign Up"); //Set the title for the sign-up window

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

        //Add fields to the layout
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

    // Set scene for the sign-up stage
        Scene signUpScene = new Scene(signUpLayout, 300, 250);
        signUpStage.setScene(signUpScene);
        signUpStage.initOwner(primaryStage);
        signUpStage.show();
    }

    private void showLoginDialog(Stage primaryStage) { //Method to show the login dialog
        Stage loginStage = new Stage(); //Create a new window for login
        loginStage.setTitle("Login"); //Set the title for the login window

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

        //Add fields to the layout
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

            // Retrieve the user from the database and check if username and password match
            if (usersDatabase.containsKey(username) && usersDatabase.get(username).getPassword().equals(password) && usersDatabase.get(username).getUserType().equals(userType)) {
                outputArea.appendText("User Logged In: " + username + " (" + userType + ")\n");
                if ("Customer".equals(userType)) {
                    bookTicketButton.setDisable(false);
                    showTicketPricesButton.setDisable(false);
                }
                loginStage.close(); //close the login window
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }
        });

        Scene loginScene = new Scene(loginLayout, 300, 250);
        loginStage.setScene(loginScene);
        loginStage.initOwner(primaryStage);
        loginStage.show(); //show the login stage
    }

    private void showCustomerDetails() { //Method to display customer details
        outputArea.appendText("Customers: " + customerList.size() + "\n");
        for (Customer customer : customerList) { //Iterate through the customers list.
            outputArea.appendText(customer.toString() + "\n");
        }
    }

    private void showVendorDetails() { //Method to display vendor details

        for (Vendor vendor : vendorList) { //Iterate through the vendors list
            outputArea.appendText(vendor.toString() + "\n");
        }
    }

    private void bookTicket() { //Method to book ticket

        outputArea.appendText("Ticket booked!\n"); //Display a message in the output area
    }

    private void showTicketPrices() { //Method to show ticket prices
        double ticketPrice = 50.0; // Set the ticket price label with price

        // Update the label with the actual price
        ticketPriceLabel.setText("Ticket Prices: $" + ticketPrice);
        outputArea.appendText("Ticket prices displayed!\n");
    }

}
