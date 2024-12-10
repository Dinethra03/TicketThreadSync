package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//The CLIApp class provides a command-line interface for to configure and run the ticket vending simulation
public class CLIApp {
    //Main method to run the CLI application
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Configuration config = promptForConfiguration(scanner); //Prompt the user for configuration details

            // Save the configuration to a JSON file
            config.saveToFile("config.json");
            System.out.println("Configuration saved to 'config.json'.");

            //Save the configuration to a text file
            config.saveToTextFile("config.txt");
            System.out.println("Configuration saved to 'config.txt'.");

            System.out.println("Do you want to start the program? 'start' / 'exit': ");
            String command = scanner.nextLine().toLowerCase();

            if (command.equals("start")) {
                TicketPool ticketPool = new TicketPool(config.getMaxCapacity()); //Create a new ticket pool

                // Create and start vendor threads
                List<Thread> vendorThreads = new ArrayList<>();
                for (int i = 1; i <= 6; i++) { //Loop to create multiple vendors
                    Vendor vendor = new Vendor(ticketPool, config.getTotalTickets(), config.getTicketReleaseRate(), "Vendor-" + i, null, null, null);
                    Thread vendorThread = new Thread(vendor); //Wrap the vendor in thread
                    vendorThread.start(); //Start the thread
                    vendorThreads.add(vendorThread); //Add the thread to the list
                }

                // Start and create Customer Threads
                List<Thread> customerThreads = new ArrayList<>();
                for (int i = 1; i <= 10; i++) { //Loop to create multiple customers
                    // Pass customer name like "Customer-1", "Customer-2", etc.
                    Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate(), null, null, null, "Customer-" + i);
                    Thread customerThread = new Thread(customer, "Customer-" + i); //Wrap the customer in a thread
                    customerThread.start();
                    customerThreads.add(customerThread);
                }

                System.out.println("Enter 'stop' to terminate the program.");
                while (true) {
                    String stopCommand = scanner.nextLine().toLowerCase();
                    if (stopCommand.equals("stop")) {
                        stopThreads(vendorThreads, customerThreads); //Stop all threds
                        break;
                    } else {
                        System.out.println("Invalid command. Enter 'stop' to terminate.");
                    }
                }
            } else if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting program...");
                System.exit(0);
            } else {
                System.out.println("Invalid command. System Stopped");
            }

            break; //Exit the loop after processing the input

        }
    }

    //Prompt the user to enter configuration details
    private Configuration promptForConfiguration(Scanner scanner) {
        int totalTickets = promptForInt(scanner, "Enter total tickets: "); //Number of tickets
        int maxCapacity = promptForInt(scanner, "Enter max capacity: "); //Pool's maximum capacity
        int ticketReleaseRate = promptForInt(scanner, "Enter ticket release rate: "); //Rate of ticket release
        int customerRetrievalRate = promptForInt(scanner, "Enter customer retrieval rate: "); //Rate of ticket retrieval

        //Return a new Configuration object based on user input
        return new Configuration(totalTickets, maxCapacity, ticketReleaseRate, customerRetrievalRate);
    }

    // Helper function for prompting for positive integer inputs
    private int promptForInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt); //Display the prompt
            if (scanner.hasNextInt()) { //Check if input is an integer
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
                if (value > 0) { // Validate positive input
                    return value;
                }
            } else {
                scanner.next(); // Consume invalid input
            }
            System.out.println("Please enter a valid positive integer.");

        }


    }

    //Stop all threads
    private void stopThreads(List<Thread>... threadGroups) {
        for (List<Thread> threads : threadGroups) {
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }
    }

}
