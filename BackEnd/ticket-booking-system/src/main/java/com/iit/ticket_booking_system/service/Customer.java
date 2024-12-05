package com.iit.ticket_booking_system.service;

import com.iit.ticket_booking_system.model.Ticket;

import java.util.UUID;

public class Customer implements Runnable {

    private final String customerId;
    private final String customerName;
    private final int retrievalInterval;
    private final TicketPool ticketPool;
    private final int totalTickets;
    private int ticketsAdded = 0;

    public Customer(String customerName, int retrievalInterval, int totalTickets, TicketPool ticketPool) {
        this.customerName = customerName;
        this.customerId = UUID.randomUUID().toString();
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;
        this.totalTickets = totalTickets;
    }

    public String getCustomerId(){
        return customerId;
    }
    @Override
    public void run() {
        while (ticketsAdded < totalTickets) {
            Ticket ticket = ticketPool.buyTicket();
            System.out.println("Ticket bought by " + customerId + "\n");
            ticketsAdded++;
            try {
                Thread.sleep(retrievalInterval * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}


