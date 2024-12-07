package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Customer implements Runnable {
    private static final Logger logger = LogManager.getLogger(Customer.class);
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;
    private final String customerId;

    public Customer(TicketPool ticketPool, int customerRetrievalRate, String customerId) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Ticket ticket = ticketPool.removeTicket();
                System.out.println("[Customer " +  Thread.currentThread().getName() +  "] Bought ticket: " + ticket.getTicketId());
                logger.info("[Customer " + customerId + "] Bought ticket: " + ticket.getTicketId());
                Thread.sleep(customerRetrievalRate * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("[Customer " + customerId + "] Interrupted while retrieving tickets.");
            }
        }
    }
}
