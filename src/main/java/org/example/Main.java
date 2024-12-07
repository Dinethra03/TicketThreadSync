package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);



        while (true) {
            // Prompt the user for configuration and validate input
            Configuration config = promptForConfiguration(scanner);

            String startcmd;
            while (true) {
                System.out.println("Do you want to start the program? 'start' / 'exit': ");
                startcmd = scanner.nextLine().toLowerCase();

                if (startcmd.equals("start")) {
                    // TicketPool to be shared between all vendors and customers
                    TicketPool ticketPool = new TicketPool(config.getMaxCapacity());

                    // Creating lists to hold the threads
                    List<Thread> vendorThreads = new ArrayList<>();
                    List<Thread> customerThreads = new ArrayList<>();

                    // Start 6 Vendor threads
                    for (int i = 1; i <= 6; i++) {
                        Vendor vendor = new Vendor(ticketPool, config.getTotalTickets(), config.getTicketReleaseRate(), "Vendor-" + i);
                        Thread vendorThread = new Thread(vendor);
                        vendorThread.start();
                        vendorThreads.add(vendorThread); // Add to the vendor threads list
                    }

                    // Start 10 Customer threads
                    for (int i = 1; i <= 10; i++) {
                        Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate());
                        Thread customerThread = new Thread(customer);
                        customerThread.setName("Customer-" + i); // Set the thread name as Customer-1, Customer-2, etc.
                        customerThread.start();
                        customerThreads.add(customerThread);
                    }

                    System.out.println("Enter 'stop' to terminate the program");
                    while (true) {
                        String stopcmd = scanner.nextLine().toLowerCase();
                        if (stopcmd.equals("stop")) {
                            for (Thread customerThread : customerThreads) {
                                customerThread.interrupt();
                            }
                            for (Thread vendorThread : vendorThreads) {
                                vendorThread.interrupt();
                            }

                            System.exit(0); // Stopping the threads
                        } else if (stopcmd.equals("exit")) {
                            System.out.println("Terminating the program...");
                            System.exit(0);
                        } else {
                            System.out.println("Invalid command. Please enter 'stop' or 'exit'.");
                        }
                    }
                } else if (startcmd.equals("exit")) {
                    System.out.println("Terminating the program...");
                    System.exit(0);
                } else {
                    System.out.println("System terminated...");
                }
            }
        }
    }





    // CLI to prompt for configuration input
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
                scanner.next(); // consume invalid input
            }
        }
    }
}