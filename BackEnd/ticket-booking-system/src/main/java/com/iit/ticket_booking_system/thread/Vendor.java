package com.iit.ticket_booking_system.thread;

import com.iit.ticket_booking_system.model.Ticket;
import com.iit.ticket_booking_system.TicketPool;
import com.iit.ticket_booking_system.util.LoggerUtil;

import java.util.UUID;

/**
 * Represents a vendor responsible for adding tickets to the ticket pool.
 * Each vendor adds tickets in batches at specified intervals until a total ticket limit is reached.
 * Implements {@link Runnable} to allow execution in a separate thread.
 */
public class Vendor implements Runnable {

    private final String vendorId; // Unique identifier for the vendor
    private final String vendorName; // Name of the vendor
    private final int ticketsPerRelease; //  Number of tickets a vendor adds in one release
    private final int releaseInterval; // Time interval (in seconds) between releases
    private final String eventName; // Name of the event associated with the tickets
    private final double price; // Price of each ticket
    private final TicketPool ticketPool; // Shared resource for ticket storage
    private final int totalTickets; // Total number of tickets the vendor can add
    private int ticketsAdded = 0; // Tracks the number of tickets added so far


    /**
     * Constructs a Vendor with the specified details.
     *
     * @param vendorId          Unique identifier for the vendor (generated internally).
     * @param vendorName        Name of the vendor.
     * @param eventName         Name of the event for which tickets are sold.
     * @param ticketPool        Reference to the shared ticket pool.
     * @param ticketsPerRelease Number of tickets added per batch.
     * @param releaseInterval   Interval (in seconds) between ticket releases.
     * @param totalTickets      Total number of tickets to add.
     * @param price             Price of each ticket.
     */
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

    /**
     * The execution logic for the vendor thread.
     * The vendor adds tickets in batches to the shared ticket pool, with pauses between each batch.
     */
    @Override
    public void run() {
        try {

            // Continue adding tickets until the total specified limit is reached
            while (ticketsAdded < totalTickets) {
                for (int i = 0; i < ticketsPerRelease; i++) {

                    // Create a new ticket with the current index and event details
                    Ticket ticket = new Ticket(ticketsAdded + 1, eventName, price);
                    ticketPool.addTickets(ticket);

                    // Log the addition of the ticket
                    System.out.println("Ticket added by " + vendorName + " " + ticket);
                    LoggerUtil.log("INFO", "Ticket added by " + vendorName + " " + ticket);

                    ticketsAdded++; // Increment the count of tickets added

                    // Log the updated count of tickets added
                    System.out.println(vendorName + " added " + ticketsAdded + " tickets\n");
                    LoggerUtil.log("INFO", vendorName + " added " + ticketsAdded + " tickets");
                }
                Thread.sleep(releaseInterval * 1000L);
            }
        } catch (InterruptedException e) {
            System.out.println("Error Occurred");
            LoggerUtil.log("ERROR", "Error Occurred when vendor adds tickets");
        }

        // Log the completion of the ticket addition process
        System.out.println("Vendor " + vendorName + " has finished adding tickets\n");
        LoggerUtil.log("INFO", "Vendor " + vendorName + " has finished adding tickets");
    }
}

