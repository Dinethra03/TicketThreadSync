package org.example;
import java.util.LinkedList;


public class TicketPool {
    private final LinkedList<Ticket> tickets = new LinkedList<>();
    private final int maxCapacity;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public synchronized void addTickets(Ticket ticket) throws InterruptedException {
        while (tickets.size() >= maxCapacity) {
            wait();
        }
        tickets.add(ticket);
        System.out.println("Added: " + ticket.getTicketId());
        notifyAll();
    }

    public synchronized Ticket removeTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            wait();
        }
        Ticket ticket = tickets.removeFirst();
        System.out.println("Removed: " + ticket.getTicketId());
        notifyAll();
        return ticket;
    }

    public synchronized int getTicketCount() {
        return tickets.size();
    }
}

