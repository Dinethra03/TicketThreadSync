package org.example;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TicketPool {
    private final BlockingQueue<Ticket> tickets;
    private final int maxCapacity;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tickets = new ArrayBlockingQueue<>(maxCapacity);
    }

    public void addTickets(Ticket ticket) throws InterruptedException {
        tickets.put(ticket); // This is a blocking call that will wait until there is space in the queue
        System.out.println("Added: " + ticket.getTicketId());
    }

    public Ticket removeTicket() throws InterruptedException {
        Ticket ticket = tickets.take(); // This is a blocking call that will wait until a ticket is available
        System.out.println("Removed: " + ticket.getTicketId());
        return ticket;
    }

    public int getTicketCount() {
        return tickets.size();
    }
}
