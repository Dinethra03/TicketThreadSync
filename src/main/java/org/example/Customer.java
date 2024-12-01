package org.example;
public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalInterval; // in milliseconds

    public Customer(TicketPool ticketPool, int retrievalInterval) {
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ticketPool.removeTicket();
                Thread.sleep(retrievalInterval);
            }
        } catch (InterruptedException e) {
            System.out.println("Customer interrupted.");
        }
    }
}
