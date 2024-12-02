package org.example;


public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketsPerBatch;
    private final int releaseInterval;
    private final String vendorId;
    private volatile boolean isRunning = true; // Flag to stop the thread gracefully

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
                Thread.sleep(releaseInterval);
            }
        } catch (InterruptedException e) {
            // Handle thread interruption gracefully
            System.out.println("Vendor thread interrupted.");
        }
    }

    // Method to stop the vendor thread gracefully
    public void stopThread() {
        isRunning = false;
    }
}
