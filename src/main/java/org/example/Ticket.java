package org.example;

public class Ticket {
    private final String ticketId;

    public Ticket(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketId() {
        return ticketId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ticket ticket = (Ticket) obj;
        return ticketId.equals(ticket.ticketId);
    }

    @Override
    public int hashCode() {
        return ticketId.hashCode();
    }
}

