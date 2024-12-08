package org.example;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Customer implements Runnable {
    private static final Logger logger = LogManager.getLogger(Customer.class);
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;
    private final Label ticketCountLabel;  // UI Label for ticket count
    private final Label ticketPoolStatusLabel;  // UI Label for ticket pool status
    private final TextArea outputArea;  // Output area for logging

    public Customer(TicketPool ticketPool, int customerRetrievalRate, Label ticketCountLabel, Label ticketPoolStatusLabel, TextArea outputArea) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketCountLabel = ticketCountLabel;
        this.ticketPoolStatusLabel = ticketPoolStatusLabel;
        this.outputArea = outputArea;
    }

    @Override
    public void run() {
        // Run ticket-buying logic in this thread
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Ticket ticket = ticketPool.removeTicket(); // Retrieve a ticket from the pool

                // Update UI using Platform.runLater()
                Platform.runLater(() -> {
                    ticketCountLabel.setText("Tickets in Pool: " + ticketPool.getTicketCount());
                    ticketPoolStatusLabel.setText("Ticket Pool Status: " + ticketPool.getTicketCount() + " tickets available.");
                    outputArea.appendText("[ " + Thread.currentThread().getName() + "] Bought ticket: " + ticket.getTicketId() + "\n");
                });

                Thread.sleep(customerRetrievalRate * 1000); // Sleep before retrieving the next ticket
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle thread interruption
            }
        }
    }
}
