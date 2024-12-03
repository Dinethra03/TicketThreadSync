package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TicketPool {
    private final BlockingQueue<Ticket> tickets;

    public TicketPool(int maxCapacity) {
        this.tickets = new ArrayBlockingQueue<>(maxCapacity);
    }

    public void addTickets(Ticket ticket) throws InterruptedException {
        tickets.put(ticket); // Blocks if the queue is full
        System.out.println("[TicketPool] Added: " + ticket.getTicketId());
    }

    public Ticket removeTicket() throws InterruptedException {
        Ticket ticket = tickets.take(); // Blocks if the queue is empty
        System.out.println("[TicketPool] Removed: " + ticket.getTicketId());
        return ticket;
    }

    public int getTicketCount() {
        return tickets.size();
    }
}
