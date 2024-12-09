package org.example;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.application.Platform;

public class Vendor implements Runnable {
    private static int ticketCounter = 1;
    private final TicketPool ticketPool;
    private final int totalTickets;
    private final int ticketReleaseRate;
    private final String vendorId;

    private final Label ticketCountLabel;
    private final Label ticketPoolStatusLabel;
    private final TextArea outputArea;

    public Vendor(TicketPool ticketPool, int totalTickets, int ticketReleaseRate, String vendorId,
                  Label ticketCountLabel, Label ticketPoolStatusLabel, TextArea outputArea) {
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

                    System.out.println("[ " + vendorId + "] Added ticket: " + ticket.getTicketId());

                    if (ticketCountLabel != null && ticketPoolStatusLabel != null && outputArea != null) {
                        Platform.runLater(() -> {
                            ticketCountLabel.setText("Tickets in Pool: " + ticketPool.getTicketCount());
                            ticketPoolStatusLabel.setText("Ticket Pool Status: " + ticketPool.getTicketCount() + " tickets available.");
                            outputArea.appendText("[ " + vendorId + "] Added ticket: " + ticket.getTicketId() + "\n");
                        });
                    }
                }
                Thread.sleep(ticketReleaseRate * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

            }

            }
        }
    // Override the toString() method to return a readable format
    @Override
    public String toString() {
        return "Vendor {ID = " + vendorId + ", Ticket to Release = " + totalTickets +
                ", Release Rate = " + ticketReleaseRate + "}";
    }
}



