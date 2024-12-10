package org.example;

public class Ticket {
    private final String ticketId; //A unique identification for the ticket, declared final because it should not change

    //The constructor will initialize the ticket with its unique identifier
    public Ticket(String ticketId) {
        this.ticketId = ticketId; //Assign the given ticketId to the ticket
    }

    //Getter method for retrieving the ticket's unique identification
    public String getTicketId() {
        return ticketId; //Return the ticketID connected with this ticket
    }
}
