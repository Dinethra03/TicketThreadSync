package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLIApp {
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            Configuration config = promptForConfiguration(scanner);

            System.out.println("Do you want to start the program? 'start' / 'exit': ");
            String command = scanner.nextLine().toLowerCase();

            if (command.equals("start")) {
                TicketPool ticketPool = new TicketPool(config.getMaxCapacity());

                // Start Vendor Threads
                List<Thread> vendorThreads = new ArrayList<>();
                for (int i = 1; i <= 6; i++) {
                    Vendor vendor = new Vendor(ticketPool, config.getTotalTickets(), config.getTicketReleaseRate(), "Vendor-" + i);
                    Thread vendorThread = new Thread(vendor);
                    vendorThread.start();
                    vendorThreads.add(vendorThread);
                }

                // Start Customer Threads
                List<Thread> customerThreads = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate());
                    Thread customerThread = new Thread(customer, "Customer-" + i);
                    customerThread.start();
                    customerThreads.add(customerThread);
                }

                System.out.println("Enter 'stop' to terminate the program.");
                while (true) {
                    String stopCommand = scanner.nextLine().toLowerCase();
                    if (stopCommand.equals("stop")) {
                        stopThreads(vendorThreads, customerThreads);
                        break;
                    } else {
                        System.out.println("Invalid command. Enter 'stop' to terminate.");
                    }
                }
            } else if (command.equals("exit")) {
                System.out.println("Exiting program...");
                break;
            } else {
                System.out.println("Invalid command. Please enter 'start' or 'exit'.");
            }
        }
    }

    private Configuration promptForConfiguration(Scanner scanner) {
        int totalTickets = promptForInt(scanner, "Enter total tickets: ");
        int maxCapacity = promptForInt(scanner, "Enter max capacity: ");
        int ticketReleaseRate = promptForInt(scanner, "Enter ticket release rate: ");
        int customerRetrievalRate = promptForInt(scanner, "Enter customer retrieval rate: ");

        return new Configuration(totalTickets, maxCapacity, ticketReleaseRate, customerRetrievalRate);
    }

    private int promptForInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
                if (value > 0) {
                    return value;
                }
            } else {
                scanner.next(); // Consume invalid input
            }
            System.out.println("Please enter a valid positive integer.");
        }
    }

    private void stopThreads(List<Thread>... threadGroups) {
        for (List<Thread> threads : threadGroups) {
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }
    }
}
