package org.example;


public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketsPerBatch;
    private final int releaseInterval;
    private final String vendorId;
    private volatile boolean isRunning = true;

    public Vendor(TicketPool ticketPool, int ticketsPerBatch, int releaseInterval, String vendorId) {
        this.ticketPool = ticketPool;
        this.ticketsPerBatch = ticketsPerBatch;
        this.releaseInterval = releaseInterval;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < ticketsPerBatch && isRunning; i++) {
                String ticketId = vendorId + "-T" + (i + 1);
                ticketPool.addTickets(new Ticket(ticketId));
                logAction("Added ticket: " + ticketId);
                Thread.sleep(releaseInterval);
            }
        } catch (InterruptedException e) {
            logAction("Vendor thread interrupted.");
            Thread.currentThread().interrupt();
        }
    }

    public void stopThread() {
        isRunning = false;
    }

    private void logAction(String message) {
        System.out.println("[Vendor " + vendorId + "] " + message);
    }
}
