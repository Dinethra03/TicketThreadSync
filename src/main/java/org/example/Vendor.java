package org.example;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Vendor implements Runnable {
    private static final Logger logger = LogManager.getLogger(Vendor.class);
    private static int ticketCounter = 1; // Shared ticket counter across all vendor threads
    private final TicketPool ticketPool;
    private final int totalTickets;
    private final int ticketReleaseRate;
    private final String vendorId;
    private final Label ticketCountLabel;  // UI Label for ticket count
    private final Label ticketPoolStatusLabel;  // UI Label for ticket pool status
    private final TextArea outputArea;  // Output area for logging

    public Vendor(TicketPool ticketPool, int totalTickets, int ticketReleaseRate, String vendorId, Label ticketCountLabel, Label ticketPoolStatusLabel, TextArea outputArea) {
        this.ticketPool = ticketPool;
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.vendorId = vendorId;
        this.ticketCountLabel = ticketCountLabel;
        this.ticketPoolStatusLabel = ticketPoolStatusLabel;
        this.outputArea = outputArea;
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

                    // Update UI using Platform.runLater()
                    Platform.runLater(() -> {
                        ticketCountLabel.setText("Tickets in Pool: " + ticketPool.getTicketCount());
                        ticketPoolStatusLabel.setText("Ticket Pool Status: " + ticketPool.getTicketCount() + " tickets available.");
                        outputArea.appendText("[ " + vendorId + "] Added ticket: " + ticket.getTicketId() + "\n");
                    });
                }
                Thread.sleep(ticketReleaseRate * 1000); // Sleep before adding the next ticket
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}