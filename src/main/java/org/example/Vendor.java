package org.example;

import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Vendor implements Runnable {
    private static final Logger logger = LogManager.getLogger(Vendor.class);
    private static int ticketCounter = 1; // Shared ticket counter
    private final TicketPool ticketPool;
    private final int totalTickets;
    private final int ticketReleaseRate;
    private final String vendorId;
    private final Label ticketCountLabel; // UI Label

    public Vendor(TicketPool ticketPool, int totalTickets, int ticketReleaseRate, String vendorId, Label ticketCountLabel) {
        this.ticketPool = ticketPool;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorId;
        this.ticketCountLabel = ticketCountLabel;
    }

    @Override
    public void run() {
        for (int i = 1; i <= totalTickets; i++) {
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
            try {
                synchronized (Vendor.class) {
                    Ticket ticket = new Ticket("Ticket-" + (ticketCounter++));
                    ticketPool.addTickets(ticket);

                    // Update UI
                    Platform.runLater(() -> ticketCountLabel.setText("Tickets in Pool: " + ticketPool.getTicketCount()));

                    logger.info("[ " + vendorId + "] Added ticket: " + ticket.getTicketId());
                }
                Thread.sleep(ticketReleaseRate * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
