package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user for configuration and validate input
        Configuration config = promptForConfiguration(scanner);
        //TicketPool  to be shared between Vendor and Customer
        TicketPool ticketPool = new TicketPool(config.getMaxCapacity());


        //Starting the Vendor  thread
        Vendor vendor = new Vendor(ticketPool,config.getTotalTickets(), config.getTicketReleaseRate(), "Vendor-1");
        Thread vendorThread = new Thread(vendor);
        vendorThread.start();

        //Starting the Customer thread
        Customer customer = new Customer(ticketPool, config.getCustomerRetrievalRate(), "Customer-1");
        Thread customerThread = new Thread(customer);
        customerThread.setName("Customer-1");
        customerThread.start();

        System.out.println("Enter 'stop' to terminate the program   ");
        while   (true){
            String stopcmd = scanner.nextLine();
            if(stopcmd.equals("stop")){
                vendorThread.interrupt();
                customerThread.interrupt();
                break;
            }
        }


    }


    // CLI to prompt for configuration input
    private static Configuration promptForConfiguration(Scanner scanner) {
        int totalTickets = promptForInt(scanner, "Enter total tickets: ");
        int maxCapacity = promptForInt(scanner, "Enter max capacity: ");
        int ticketReleaseRate = promptForInt(scanner, "Enter ticket release rate (in seconds): ");
        int customerRetrievalRate = promptForInt(scanner, "Enter customer retrieval rate (in seconds) : ");

        Configuration config = new Configuration(totalTickets, maxCapacity, ticketReleaseRate, customerRetrievalRate);

        //Asking user if they want to save configurations
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
                }else {
                    System.out.println("Please enter a positive integer again: ");

                }
            }else{
                System.out.println("Please enter a integer again: ");
                scanner.next();
            }

        }
    }


}
