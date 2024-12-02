package org.example;


public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalInterval;
    private volatile boolean isRunning = true;

    public Customer(TicketPool ticketPool, int retrievalInterval) {
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                Ticket ticket = ticketPool.removeTicket();
                logAction("Purchased ticket: " + ticket.getTicketId());
                Thread.sleep(retrievalInterval);
            }
        } catch (InterruptedException e) {
            logAction("Customer thread interrupted.");
            Thread.currentThread().interrupt();
        }
    }

    public void stopThread() {
        isRunning = false;
    }

    private void logAction(String message) {
        System.out.println("[Customer] " + message);
    }
}
