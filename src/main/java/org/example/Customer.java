package org.example;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Customer implements Runnable {
    private static final Logger logger = LogManager.getLogger(Customer.class);
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;
    private final Label ticketCountLabel; // UI Label

    public Customer(TicketPool ticketPool, int customerRetrievalRate, Label ticketCountLabel) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketCountLabel = ticketCountLabel;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Ticket ticket = ticketPool.removeTicket();

                // Update UI
                Platform.runLater(() -> ticketCountLabel.setText("Tickets in Pool: " + ticketPool.getTicketCount()));

                logger.info("[ " + Thread.currentThread().getName() + "] Bought ticket: " + ticket.getTicketId());
                Thread.sleep(customerRetrievalRate * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
