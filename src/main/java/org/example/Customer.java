package org.example;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.application.Platform;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerRetrievalRate;

    private final Label ticketCountLabel;
    private final Label ticketPoolStatusLabel;
    private final TextArea outputArea;
    private String name; // Customer name

    public Customer(TicketPool ticketPool, int customerRetrievalRate,
                    Label ticketCountLabel, Label ticketPoolStatusLabel, TextArea outputArea, String name) {
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketCountLabel = ticketCountLabel;
        this.ticketPoolStatusLabel = ticketPoolStatusLabel;
        this.outputArea = outputArea;
        this.name = name != null ? name : "Unknown";
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Ticket ticket = ticketPool.removeTicket();

                String customerID = Thread.currentThread().getName();
                System.out.println("[ " + customerID + "] Bought ticket: " + ticket.getTicketId());

                if (ticketCountLabel != null && ticketPoolStatusLabel != null && outputArea != null) {
                    Platform.runLater(() -> {
                        ticketCountLabel.setText("Tickets in Pool: " + ticketPool.getTicketCount());
                        ticketPoolStatusLabel.setText("Ticket Pool Status: " + ticketPool.getTicketCount() + " tickets available.");
                        outputArea.appendText("[ " + customerID + "] Bought ticket: " + ticket.getTicketId() + "\n");
                    });
                }

                Thread.sleep(customerRetrievalRate * 1000); // Sleep before retrieving the next ticket
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Customer {Retrieval Rate=" + customerRetrievalRate + "s, Name=" + name + "}";
    }
}
