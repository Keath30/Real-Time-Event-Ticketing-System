package com.melissa.iit.cli;

import com.melissa.iit.cli.config.Configuration;
import com.melissa.iit.cli.config.ConfigurationManager;
import com.melissa.iit.cli.util.LoggerUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a thread-safe pool of tickets for a ticket booking system.
 * <p>
 * The pool maintains a synchronized list of tickets with a maximum capacity
 * defined in a configuration file. It supports concurrent addition and
 * retrieval of tickets, ensuring thread safety.
 */
public class TicketPool {

    private final List<Ticket> tickets = Collections.synchronizedList(new LinkedList<>()); // Store tickets in a thread-safe list
    private static int totalTicketsAdded = 0; //Total number of tickets added by vendors
    private static double totalSales = 0; // Total sales from tickets bought
    private int maxCapacity; //Maximum tickets allowed in pool
    private volatile boolean stopRequested = false; //A flag indicating whether a stop has been requested for the ticket pool.

    /**
     * Constructor for the TicketPool class.
     * Initializes the ticket pool with the maximum capacity from the configuration file.
     */
    public TicketPool() {
        loadMaxCapacity(); // Load the maximum capacity from the configuration file
    }

    /**
     * Checks if a stop has been requested for the ticket pool.
     *
     * @return {@code true} if a stop has been requested, {@code false} otherwise.
     */
    public synchronized boolean isStopRequested(){
        return stopRequested;
    }

    /**
     * Requests to stop the ticket pool, notifying all waiting threads.
     */
    public synchronized void requestStop(){
        stopRequested = true;
        notifyAll();
    }


    /**
     * Loads the maximum ticket capacity from the configuration file.
     * <p>
     * Logs an error if the configuration cannot be loaded.
     */
    public void loadMaxCapacity(){
        Configuration config = ConfigurationManager.loadConfiguration("config.txt");

        if (config!=null) {
            this.maxCapacity = config.getMaxTicketCapacity(); // Set max capacity from config file
            LoggerUtil.log("INFO","Max capacity set to " + maxCapacity);
        }
        else{
            LoggerUtil.log("ERROR","Failed to load configuration");
            System.err.println("Failed to load configuration");
        }
    }


    /**
     * Adds a ticket to the pool. If the pool is full, it waits until space is available.
     * <p>
     * This method is synchronized to ensure thread safety when modifying the ticket list.
     *
     * @param ticket The ticket to be added to the pool.
     * @throws IllegalArgumentException if the ticket is null.
     */
    public synchronized void addTickets(Ticket ticket) {

        // Wait if the ticket pool is at full capacity
        while (tickets.size() >= maxCapacity && !isStopRequested()) {
            try {
                System.out.println("Ticket pool is full");
                LoggerUtil.log("INFO", "Ticket pool is full");
                wait(); // Wait for space to become available in the pool
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LoggerUtil.log("ERROR", "Thread interrupted while waiting to add ticket");
            }
        }
        if (stopRequested) return;
        tickets.add(ticket); // Add the ticket to the pool
        totalTicketsAdded++; // Increment the total number of tickets added
        System.out.println("Ticket added: " + ticket);
        notifyAll(); // Notify all threads waiting for tickets that one has been added
    }

    /**
     * Buys a ticket from the pool. If no tickets are available, it waits until tickets are added.
     * <p>
     * This method is synchronized to ensure thread safety when modifying the ticket list.
     *
     * @return The ticket that was bought.
     */
    public synchronized Ticket buyTicket() {
        // Wait if there are no tickets in the pool
        while (tickets.isEmpty() && !isStopRequested()) {
            try {
                System.out.println("No tickets available\n");
                LoggerUtil.log("INFO", "No tickets available");
                wait(); // Wait until tickets are added to the pool
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
               LoggerUtil.log("ERROR","Thread interrupted while waiting to buy ticket");
            }
        }

        if(stopRequested) return null;
        // Remove and return the first ticket from the pool
        Ticket ticket = tickets.remove(0);

        totalSales += ticket.getPrice(); // Add the price of the ticket to total sales
        System.out.println("Ticket removed: " + ticket);
        notifyAll(); // Notify all threads waiting for tickets that one has been bought
        return ticket;
    }


}
