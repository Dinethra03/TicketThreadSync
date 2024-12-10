package org.example;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.application.Platform; //Import JavaFx Platform for thread-safe UI updates

//The vendor class simulates a vendor who adds tickets to the ticket pool at a specific rate
public class Vendor implements Runnable {
    private static int ticketCounter = 1; //Shared counter for assigning unique IDs to tickets, shared among all vendors
    private final TicketPool ticketPool; //Reference to the TicketPool,where tickets will be added
    private final int totalTickets; //Total number of tickets this vendor needs to release
    private final int ticketReleaseRate; // The period (in second) between releasing each ticket
    private final String vendorId; //Unique identifier for this vendor

    private final Label ticketCountLabel;
    private final Label ticketPoolStatusLabel;
    private final TextArea outputArea; //TextArea for logging vendor actions


//Constructor to initialize the Vendor with required details
    public Vendor(TicketPool ticketPool, int totalTickets, int ticketReleaseRate, String vendorId,
                  Label ticketCountLabel, Label ticketPoolStatusLabel, TextArea outputArea) {
        this.ticketPool = ticketPool;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorId;
        this.ticketCountLabel = ticketCountLabel;
        this.ticketPoolStatusLabel = ticketPoolStatusLabel;
        this.outputArea = outputArea;
    }

    //The run() method defines the behavior of the Vendor thread
    @Override
    public void run() {
        for (int i = 1; i <= totalTickets; i++) { //Loop through the number of tickets to release
            if (Thread.currentThread().isInterrupted()) { //Check if the thread is interrupted
                break; //Exit the loop if interrupted
            }
            try {
                synchronized (Vendor.class) { // Synchronize one the Vendor class to ensure thread-safe ticket creation
                    Ticket ticket = new Ticket("Ticket-" + (ticketCounter++)); //Create a new ticket with a unique ID
                    ticketPool.addTickets(ticket);

                //Log the action to the console
                    System.out.println("[ " + vendorId + "] Added ticket: " + ticket.getTicketId());

                 // Update the UI elements safely using JavaFx Platform.runlater
                    if (ticketCountLabel != null && ticketPoolStatusLabel != null && outputArea != null) {
                        Platform.runLater(() -> {
                            ticketCountLabel.setText("Tickets in Pool: " + ticketPool.getTicketCount());
                            ticketPoolStatusLabel.setText("Ticket Pool Status: " + ticketPool.getTicketCount() + " tickets available.");
                            outputArea.appendText("[ " + vendorId + "] Added ticket: " + ticket.getTicketId() + "\n");
                        });
                    }
                }
                Thread.sleep(ticketReleaseRate * 1000); //Wait for the release rate interval before adding the next ticket
            } catch (InterruptedException e) { //Handle interrupts such as thread stopping
                Thread.currentThread().interrupt(); //Set the interrupt flag to ensure correct termination

            }

            }
        }
    // Override the toString() method to provide a readable description of Vendor
    @Override
    public String toString() {
        return "Vendor {ID = " + vendorId + ", Ticket to Release = " + totalTickets +
                ", Release Rate = " + ticketReleaseRate + "}";
    }
}



