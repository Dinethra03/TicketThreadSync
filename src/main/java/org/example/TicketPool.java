package org.example;



import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<Ticket> tickets = new LinkedList<>();
    private final int maxCapacity;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public synchronized void addTickets(Ticket ticket) throws InterruptedException {
        while (tickets.size() >= maxCapacity) {
            wait();
        }
        tickets.add(ticket);
        notifyAll();
    }

    public synchronized Ticket removeTicket() throws InterruptedException {
        while (tickets.isEmpty()) {
            wait();
        }
        Ticket ticket = tickets.poll();
        notifyAll();
        return ticket;
    }

    public synchronized int getTicketCount() {
        return tickets.size();
    }
}