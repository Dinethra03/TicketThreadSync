package org.example;

import javafx.scene.control.Label; //Import label to display ticket count and status
import javafx.scene.control.TextArea; //Import TextArea to display output messages
import javafx.application.Platform; //Import Platform to update the GUI from a different thread

//Customer class implements Runnable to simulate a customer buying tickets in a multithreaded environment
public class Customer implements Runnable {
    private final TicketPool ticketPool; //Refer to the TicketPool from which customers will retrieve tickts
    private final int customerRetrievalRate; //The speed at which the customer retrieves tickets(in seconds)

    //Lables to display the current count of tickets and number of tickets available in the pool
    private final Label ticketCountLabel;
    private final Label ticketPoolStatusLabel;
    private final TextArea outputArea; //TextArea to show detailed output of the customer's actions
    private String name; // Customer name

    //Constructor will initialize the customer with the relevant details
    public Customer(TicketPool ticketPool, int customerRetrievalRate,
                    Label ticketCountLabel, Label ticketPoolStatusLabel, TextArea outputArea, String name) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketCountLabel = ticketCountLabel;
        this.ticketPoolStatusLabel = ticketPoolStatusLabel;
        this.outputArea = outputArea;
        this.name = name != null ? name : "Unknown"; //Set the customer's name (defult to "Unknown" if null)
    }

    //Override the run method in Runnable to define the customer's behavior on a separate thread
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) { //Continue running untill the thread is interrupted
            try {
                Ticket ticket = ticketPool.removeTicket(); //Attempt to get a ticket from the ticket pool

                String customerID = Thread.currentThread().getName(); //Get the current thread's name(used as a customer ID)
                System.out.println("[ " + customerID + "] Bought ticket: " + ticket.getTicketId());

                //Check that the UI components are not null before updating them from the GUI thread
                if (ticketCountLabel != null && ticketPoolStatusLabel != null && outputArea != null) {
                    //Use Platform.runLater to update the GUI from the customer's thread
                    Platform.runLater(() -> {
                        ticketCountLabel.setText("Tickets in Pool: " + ticketPool.getTicketCount()); //Update the ticket count label
                        ticketPoolStatusLabel.setText("Ticket Pool Status: " + ticketPool.getTicketCount() + " tickets available.");
                        outputArea.appendText("[ " + customerID + "] Bought ticket: " + ticket.getTicketId() + "\n"); //Apply the action to the output area
                    });
                }

                //Simulate the customer waiting before retrieving another ticket(in seconds)
                Thread.sleep(customerRetrievalRate * 1000);
            } catch (InterruptedException e) { //Handle interruptions and reset the interrupt flag
                Thread.currentThread().interrupt();
            }
        }
    }

    //Getter method for the Customer's Name
    public String getName() {
        return this.name;
    }

    //Override the toString method and provide a string representation of the customer
    @Override
    public String toString() {
        return "Customer {Retrieval Rate=" + customerRetrievalRate + "s, Name=" + name + "}";
    }
}
