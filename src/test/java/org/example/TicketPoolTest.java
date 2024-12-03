package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketPoolTest {

    @Test
    void testAddAndRemoveTickets() throws InterruptedException {
        TicketPool ticketPool = new TicketPool(5);
        Ticket ticket1 = new Ticket("Ticket1");
        Ticket ticket2 = new Ticket("Ticket2");

        // Add tickets
        ticketPool.addTickets(ticket1);
        ticketPool.addTickets(ticket2);

        assertEquals(2, ticketPool.getTicketCount());

        // Remove tickets
        assertEquals(ticket1, ticketPool.removeTicket());
        assertEquals(ticket2, ticketPool.removeTicket());
        assertEquals(0, ticketPool.getTicketCount());
    }

    @Test
    void testBlockingBehavior() throws InterruptedException {
        TicketPool ticketPool = new TicketPool(1);
        Ticket ticket1 = new Ticket("Ticket1");
        Ticket ticket2 = new Ticket("Ticket2");

        // Add first ticket
        ticketPool.addTickets(ticket1);

        // Add second ticket in a separate thread (should block until space is available)
        new Thread(() -> {
            try {
                ticketPool.addTickets(ticket2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        Thread.sleep(500); // Simulate some delay
        assertEquals(1, ticketPool.getTicketCount());

        // Remove the first ticket to free up space
        ticketPool.removeTicket();
        Thread.sleep(500); // Allow second thread to proceed
        assertEquals(1, ticketPool.getTicketCount());
    }
}
