package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.scene.control.Label; // Import Label class
import javafx.scene.control.TextArea; // Import TextArea class

public class Main {
    public static void main(String[] args) {
        // Start JavaFX application in a separate thread
        Thread javafxThread = new Thread(() -> {
            MainApp.main(args); // Launch the JavaFX application
        });
        javafxThread.setDaemon(true);
        javafxThread.start();

        // Continue running the CLI program
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Prompt for configuration
            Configuration config = promptForConfiguration(scanner);

            System.out.println("Do you want to start the program? 'start' / 'exit': ");
            String startcmd = scanner.nextLine().toLowerCase();

            if (startcmd.equals("start")) {
                // Initialize TicketPool and threads
                TicketPool ticketPool = new TicketPool(config.getMaxCapacity());

                // Retrieve UI elements from MainApp
                MainApp mainApp = new MainApp();
                Label ticketCountLabel = mainApp.getTicketCountLabel();
                Label ticketPoolStatusLabel = mainApp.getTicketPoolStatusLabel();
                TextArea outputArea = mainApp.getOutputArea();

                // Start Vendor threads
                List<Thread> vendorThreads = new ArrayList<>();
                for (int i = 1; i <= 6; i++) {
                    Vendor vendor = new Vendor(ticketPool, config.getTotalTickets(), config.getTicketReleaseRate(),
                            "Vendor-" + i, ticketCountLabel, ticketPoolStatusLabel, outputArea);
                    Thread vendorThread = new Thread(vendor);
                    vendorThread.start();
                    vendorThreads.add(vendorThread);
                }

                // Start Customer threads
                List<Thread> customerThreads = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate(),
                            ticketCountLabel, ticketPoolStatusLabel, outputArea);
                    Thread customerThread = new Thread(customer, "Customer-" + i);
                    customerThread.start();
                    customerThreads.add(customerThread);
                }

                System.out.println("Enter 'stop' to terminate the program");
                while (true) {
                    String stopcmd = scanner.nextLine().toLowerCase();
                    if (stopcmd.equals("stop")) {
                        for (Thread thread : vendorThreads) thread.interrupt();
                        for (Thread thread : customerThreads) thread.interrupt();
                        break;
                    }
                }
            } else if (startcmd.equals("exit")) {
                System.out.println("Exiting program...");
                break;
            }
        }
    }

    // Prompt for configuration input
    private static Configuration promptForConfiguration(Scanner scanner) {
        int totalTickets = promptForInt(scanner, "Enter total tickets: ");
        int maxCapacity = promptForInt(scanner, "Enter max capacity: ");
        int ticketReleaseRate = promptForInt(scanner, "Enter ticket release rate: ");
        int customerRetrievalRate = promptForInt(scanner, "Enter customer retrieval rate: ");

        Configuration config = new Configuration(totalTickets, maxCapacity, ticketReleaseRate, customerRetrievalRate);

        // Asking user if they want to save configurations
        String saveChoice;
        System.out.print("Do you want to save this configuration? (yes/no): ");
        saveChoice = scanner.nextLine().toLowerCase();

        while (!saveChoice.equals("yes") && !saveChoice.equals("no")) {
            System.out.println("Please enter 'yes' or 'no': ");
            saveChoice = scanner.nextLine().toLowerCase();
        }
        if (saveChoice.equals("yes")) {
            System.out.print("Enter filename to save configuration: ");
            String filename = scanner.nextLine();
            config.saveToFile(filename);
            System.out.println("Configuration saved to " + filename);
            config.loadFromFile(filename);
        }

        return config;
    }

    // Helper method to prompt for valid integer input
    private static int promptForInt(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Please enter a positive integer again: ");
                }
            } else {
                System.out.println("Please enter an integer again: ");
                scanner.next(); // Consume invalid input
            }
        }
    }
}
