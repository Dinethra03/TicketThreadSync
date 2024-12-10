package org.example;

import com.google.gson.Gson; //Import Gson library for converting objects to JSON
import java.io.FileWriter; //Import FileWriter for writing data to files
import java.io.IOException; //Import IOException for handling file input or output exceptions

public class Configuration {
    private final int totalTickets;
    private final int maxTicketCapacity;
    private final int ticketReleaseRate;
    private final int customerRetrievalRate;


    //Constructor for initializing the configuration with the specified values
    public Configuration(int totalTickets, int maxTicketCapacity, int ticketReleaseRate, int customerRetrievalRate) {
        this.totalTickets = totalTickets;
        this.maxTicketCapacity = maxTicketCapacity;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getMaxCapacity() {
        return maxTicketCapacity;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    // Method for saving configuration data to a JSON file
    public void saveToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) { //Create a FileWriter to write to the specified file
            Gson gson = new Gson(); //Create a Gson instance for object to JSON conversion
            gson.toJson(this, writer); //Convert the Configuration object to JSON and write it to the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for saving configuration data to a plain text file
    public void saveToTextFile(String filename){
        try (FileWriter writer = new FileWriter(filename)) { //Create a FileWriter to write to the specified text file
            writer.write("Total Tickets: " + totalTickets + "\n");
            writer.write("Max Capacity: " + maxTicketCapacity + "\n");
            writer.write("Ticket Release Rate: " + ticketReleaseRate + "\n");
            writer.write("Customer Retrieval Rate: " + customerRetrievalRate + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

