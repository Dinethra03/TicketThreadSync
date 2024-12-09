package org.example;

import javafx.scene.control.Label; // Import Label
import javafx.scene.control.TextArea; // Import TextArea
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Customer implements Runnable {
    private static final Logger logger = LogManager.getLogger(Customer.class);
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;

    private final Label ticketCountLabel;  // UI Label for ticket count
    private final Label ticketPoolStatusLabel;  // UI Label for ticket pool status
    private final TextArea outputArea;  // Output area for logging

    public Customer(TicketPool ticketPool, int customerRetrievalRate) {
        this(ticketPool, customerRetrievalRate, null, null, null);
    }

    public Customer(TicketPool ticketPool, int customerRetrievalRate,
                    Label ticketCountLabel, Label ticketPoolStatusLabel, TextArea outputArea) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketCountLabel = ticketCountLabel;
        this.ticketPoolStatusLabel = ticketPoolStatusLabel;
        this.outputArea = outputArea;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Ticket ticket = ticketPool.removeTicket();

                // Log to console (CLI mode)
                String customerID = Thread.currentThread().getName();  // Get customer ID (thread name)
                System.out.println("[ " + customerID + "] Bought ticket: " + ticket.getTicketId());


                // Update UI (GUI mode)
                if (ticketCountLabel != null && ticketPoolStatusLabel != null && outputArea != null) {
                    Platform.runLater(() -> {
                        ticketCountLabel.setText("Tickets in Pool: " + ticketPool.getTicketCount());
                        ticketPoolStatusLabel.setText("Ticket Pool Status: " + ticketPool.getTicketCount() + " tickets available.");
                        outputArea.appendText("[ " + customerID + "] Bought ticket: " + ticket.getTicketId() + "\n");  // Include customer ID
                    });

                }

                Thread.sleep(customerRetrievalRate * 1000); // Sleep before retrieving the next ticket
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Overriding toString method
    @Override
    public String toString() {
        return "Customer {Retrieval Rate=" + customerRetrievalRate + "s}";
    }
}
