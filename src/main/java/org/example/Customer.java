package org.example;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalInterval;
    private volatile boolean isRunning = true; // Flag to stop the thread gracefully

    public Customer(TicketPool ticketPool, int retrievalInterval) {
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                ticketPool.removeTicket();
                Thread.sleep(retrievalInterval);
            }
        } catch (InterruptedException e) {
            // Handle thread interruption gracefully
            System.out.println("Customer thread interrupted.");
        }
    }

    // Method to stop the customer thread gracefully
    public void stopThread() {
        isRunning = false;
    }
}
