package org.example;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketsPerBatch;
    private final int releaseInterval; // in milliseconds
    private final String vendorId;

    public Vendor(TicketPool ticketPool, int ticketsPerBatch, int releaseInterval, String vendorId) {
        this.ticketPool = ticketPool;
        this.ticketsPerBatch = ticketsPerBatch;
        this.releaseInterval = releaseInterval;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < ticketsPerBatch; i++) {
                String ticketId = vendorId + "-T" + (i + 1);
                ticketPool.addTickets(new Ticket(ticketId));
                Thread.sleep(releaseInterval);
            }
        } catch (InterruptedException e) {
            System.out.println("Vendor interrupted.");
        }
    }
}





