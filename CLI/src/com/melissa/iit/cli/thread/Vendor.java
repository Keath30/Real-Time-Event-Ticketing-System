package com.melissa.iit.cli.thread;

import com.melissa.iit.cli.Ticket;
import com.melissa.iit.cli.TicketPool;
import com.melissa.iit.cli.util.LoggerUtil;

import java.util.UUID;


/**
 * Represents a vendor responsible for adding tickets to the ticket pool.
 * Vendors operate on a separate thread, adding tickets in batches at specific intervals.
 */
public class Vendor implements Runnable {

    private final String vendorId; // Unique identifier for the vendor
    private final String vendorName; // Name of the vendor
    private final int ticketsPerRelease; //  Number of tickets a vendor adds in one release
    private final int ticketReleaseRate; // Time interval (in seconds) between releases
    private final String eventName; // Name of the event associated with the tickets
    private final double price; // Price of each ticket
    private final TicketPool ticketPool; // Shared resource for ticket storage
    private final int totalTickets; // Total number of tickets the vendor can add


    /**
     * Constructs a new Vendor instance with the specified details.
     *
     * @param vendorName        Name of the vendor.
     * @param eventName         Event name for which tickets are being sold.
     * @param ticketPool        Reference to the shared ticket pool.
     * @param ticketsPerRelease Number of tickets added per batch.
     * @param ticketReleaseRate Interval (in seconds) between ticket additions.
     * @param totalTickets      Total number of tickets the vendor can add.
     * @param price             Price of each ticket.
     */
    public Vendor(String vendorName, String eventName, TicketPool ticketPool, int ticketsPerRelease, int ticketReleaseRate, int totalTickets, double price) {
        this.vendorName = vendorName;
        this.eventName = eventName;
        this.price = price;
        this.totalTickets = totalTickets;
        this.vendorId = UUID.randomUUID().toString();
        this.ticketPool = ticketPool;
        this.ticketsPerRelease = ticketsPerRelease;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    /**
     * The core execution logic for the vendor thread.
     * The vendor adds tickets to the shared pool in batches, waiting between batches.
     */
    @Override
    public void run() {
        try {
            int ticketsAdded = 0;
            // Add tickets until the total ticket limit is reached
            while (ticketsAdded < totalTickets && !ticketPool.isStopRequested()) {
                for (int i = 0; i < ticketsPerRelease && !ticketPool.isStopRequested() && ticketsAdded<totalTickets; i++) {
                    if (Thread.interrupted()) {
                        System.out.println(vendorName + " has been interrupted and is stopping.");
                        LoggerUtil.log("INFO", vendorName + " has been interrupted and is stopping.");
                        return;
                    }

                    // Create a new ticket with the current index and event details
                    Ticket ticket = new Ticket(ticketsAdded + 1, eventName, price);

                    // Add ticket to the shared pool
                    ticketPool.addTickets(ticket);

                    // Log the addition of the ticket
                    String logMessage = "Ticket added by " + vendorName + " (" + vendorId+ ") "  + ticket;
                    System.out.println(logMessage);
                    LoggerUtil.log("INFO", logMessage);

                    ticketsAdded++; // Increment the count of tickets added

                    // Log the updated count of tickets added
                    String message = vendorName + " added " + ticketsAdded + " tickets";
                    System.out.println(message);
                    LoggerUtil.log("INFO", message);

                }
                Thread.sleep(ticketReleaseRate * 1000L);

            }
            // Log the completion of the ticket addition process
            System.out.println("Vendor " + vendorName + " has finished adding tickets");
            LoggerUtil.log("INFO", "Vendor " + vendorName + " has finished adding tickets");

        } catch (InterruptedException e) {
            System.out.println("Vendor " + vendorName + " was interrupted while adding tickets ");
            LoggerUtil.log("WARNING", "Vendor " + vendorName + " was interrupted while adding tickets ");
        }catch (Exception e){
            System.err.println("Unexpected Error" + e.getMessage());
            LoggerUtil.log("ERROR", "Unexpected Error" + e.getMessage() );
        }


    }
}

