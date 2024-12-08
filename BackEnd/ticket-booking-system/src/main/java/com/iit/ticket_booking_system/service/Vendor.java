package com.iit.ticket_booking_system.service;

import com.iit.ticket_booking_system.model.Ticket;

import java.util.UUID;


public class Vendor implements Runnable {

    private final String vendorId;
    private final String vendorName;
    private final int ticketsPerRelease; //  Number of tickets a vendor adds in one release
    private final int releaseInterval; // Time interval between releases
    private final String eventName;
    private final double price;
    private final TicketPool ticketPool;
    private final int totalTickets;
    private int ticketsAdded = 0;

    public Vendor(String vendorId, String vendorName, String eventName, TicketPool ticketPool, int ticketsPerRelease, int releaseInterval, int totalTickets, double price) {
        this.vendorName = vendorName;
        this.eventName = eventName;
        this.price = price;
        this.totalTickets = totalTickets;
        this.vendorId = UUID.randomUUID().toString();
        this.ticketPool = ticketPool;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
    }

    public String getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    @Override
    public void run() {
        try {
            while (ticketsAdded < totalTickets) {
                for (int i = 0; i < ticketsPerRelease; i++) {

                    Ticket ticket = new Ticket(ticketsAdded + 1, eventName, price);
                    ticketPool.addTickets(ticket);
                    System.out.println("Ticket added by " + vendorName);
                    ticketsAdded++;
                    System.out.println(ticket);
                    System.out.println(vendorName + " added " + ticketsAdded + " tickets\n");
                }
                Thread.sleep(releaseInterval * 1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Vendor " + vendorName + " has finished adding tickets\n");
    }
}

