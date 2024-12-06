package org.example;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Vendor implements Runnable {
    private static final Logger logger = LogManager.getLogger(Vendor.class);
    private final TicketPool ticketPool;
    private final int totalTickets;
    private final int ticketReleaseRate;
    private final String vendorId;

    public Vendor(TicketPool ticketPool, int totalTickets, int ticketReleaseRate, String vendorId) {
        this.ticketPool = ticketPool;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorId;
    }

    @Override
    public void run() {
        for (int i = 1; i <= totalTickets; i++) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            try {
                Ticket ticket = new Ticket("Ticket-" + i);
                ticketPool.addTickets(ticket);
                System.out.println("[ " + vendorId + "] Added ticket: " + ticket.getTicketId());
                logger.info("[Vendor " + vendorId + "] Added ticket: " + ticket.getTicketId());
                Thread.sleep(ticketReleaseRate * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("[ " + vendorId + "] Interrupted while adding tickets.");
            }
        }
    }
}
