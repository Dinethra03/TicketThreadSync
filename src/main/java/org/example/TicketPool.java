package org.example;

import java.util.LinkedList; //Import the LinkedList class and use it for the ticket queue.
import java.util.Queue; //Import the Queue interface to use a queue data structure

public class TicketPool {
    private final Queue<Ticket> tickets = new LinkedList<>(); //Declare a Queue to store the tickets,using LinkedList for its implemetation
    private final int maxCapacity;

    //Constructor to initialize the TickrtPool with a maximum capacity
    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    //Synchronized method for adding a ticket to the pool
    public synchronized void addTickets(Ticket ticket) throws InterruptedException {
        while (tickets.size() >= maxCapacity) { //If the pool is at maximum capacity, wait
            wait();
        }
        tickets.add(ticket); //Add the ticket to the queue
        notifyAll(); //Notify all pending threads that a ticket has been added
    }

    //Synchronized method to remove a ticket from the pool
    public synchronized Ticket removeTicket() throws InterruptedException {
        while (tickets.isEmpty()) { //If the pool is empty, wait
            wait(); //Make the thread wait till a ticket becomes available in queue
        }
        Ticket ticket = tickets.poll(); //Remove and retrieve the first ticket from the queue
        notifyAll(); //Notify all waiting threads that the ticket has been removed
        return ticket; //Return the removed ticket
    }

    //Synchronized method to get the current number of tickets in the pool
    public synchronized int getTicketCount() {
        return tickets.size(); //Return the number of tickets currently in the pool
    }
}

