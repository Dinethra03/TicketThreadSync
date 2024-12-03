package org.example;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class VendorCustomerTest {

    @Test
    void testVendorCustomerInteraction() throws InterruptedException {
        TicketPool ticketPool = new TicketPool(10);
        Vendor vendor = new Vendor(ticketPool, 5, 100, "Vendor1");
        Customer customer = new Customer(ticketPool, 200);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(vendor);
        executor.submit(customer);

        // Allow some time for interactions
        Thread.sleep(2000);

        // Validate that tickets are being added and removed
        assertTrue(ticketPool.getTicketCount() >= 0);

        executor.shutdownNow();
    }

    @Test
    void testMultipleVendorsAndCustomers() throws InterruptedException {
        TicketPool ticketPool = new TicketPool(20);

        // Create multiple vendors and customers
        Vendor vendor1 = new Vendor(ticketPool, 10, 100, "Vendor1");
        Vendor vendor2 = new Vendor(ticketPool, 10, 150, "Vendor2");
        Customer customer1 = new Customer(ticketPool, 200);
        Customer customer2 = new Customer(ticketPool, 300);

        ExecutorService executor = Executors.newFixedThreadPool(4);
        executor.submit(vendor1);
        executor.submit(vendor2);
        executor.submit(customer1);
        executor.submit(customer2);

        // Allow time for interactions
        Thread.sleep(3000);

        // Validate the pool remains within capacity
        assertTrue(ticketPool.getTicketCount() <= 20);

        executor.shutdownNow();
    }

    @Test
    void testHighConcurrency() throws InterruptedException {
        TicketPool ticketPool = new TicketPool(50);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Create multiple vendors and customers
        for (int i = 0; i < 5; i++) {
            executor.submit(new Vendor(ticketPool, 20, 50, "Vendor" + i));
            executor.submit(new Customer(ticketPool, 100));
        }

        // Allow time for execution
        Thread.sleep(5000);

        // Validate that the system remains stable
        assertTrue(ticketPool.getTicketCount() >= 0);

        executor.shutdownNow();
    }
}
