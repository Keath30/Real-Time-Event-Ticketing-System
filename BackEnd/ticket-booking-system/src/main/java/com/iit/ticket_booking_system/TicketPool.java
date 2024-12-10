package com.iit.ticket_booking_system;

import com.iit.ticket_booking_system.config.Configuration;
import com.iit.ticket_booking_system.config.ConfigurationManager;
import com.iit.ticket_booking_system.model.Ticket;
import com.iit.ticket_booking_system.util.LoggerUtil;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class TicketPool {

    private final List<Ticket> tickets = Collections.synchronizedList(new LinkedList<>()); // Store tickets in a thread-safe list
    private static int totalTicketsAdded = 0; //Total number of tickets added by vendors
    private static double totalSales = 0; // Total sales from tickets bought
    private final int maxCapacity; //Maximum tickets allowed in pool

    /**
     * Constructor for the TicketPool class.
     * Initializes the ticket pool with the maximum capacity from the configuration file.
     * <p>
     * Loads the configuration and sets the maximum capacity of tickets allowed in the pool.
     */
    public TicketPool() {
        ConfigurationManager configurationManager = new ConfigurationManager();
        Configuration config = configurationManager.loadConfiguration("config.json");
        this.maxCapacity = config.getMaxTicketCapacity(); // Set max capacity from config file
    }

    /**
     * Clears the sales data and the ticket pool.
     * This method resets the total sales, total tickets added, and clears all tickets in the pool.
     */
    public void clearSales() {
        totalSales = 0;
        totalTicketsAdded = 0;
        tickets.clear();
    }

    /**
     * Gets the current list of tickets in the pool.
     *
     * @return The list of tickets in the pool.
     */
    public List<Ticket> getTickets() {
        return tickets;
    }

    /**
     * Sets the total number of tickets added to the pool by vendors.
     *
     * @param totalTicketsAdded The total number of tickets added to the pool.
     */
    public static void setTotalTicketsAdded(int totalTicketsAdded) {
        TicketPool.totalTicketsAdded = totalTicketsAdded;
    }


    /**
     * Sets the total sales amount from ticket purchases.
     *
     * @param totalSales The total sales amount.
     */
    public static void setTotalSales(int totalSales) {
        TicketPool.totalSales = totalSales;
    }

    /**
     * Adds a ticket to the pool. If the pool is full, it waits until space is available.
     * <p>
     * This method is synchronized to ensure thread safety when adding tickets to the pool.
     * If the pool is at maximum capacity, it waits for space to become available.
     *
     * @param ticket The ticket to be added to the pool.
     */
    public synchronized void addTickets(Ticket ticket) {
        // Wait if the ticket pool is at full capacity
        while (tickets.size() >= maxCapacity) {
            try {
                System.out.println("Ticket pool is full\n");
                LoggerUtil.log("INFO", "Ticket pool is full"); // Log the full pool message
                wait(); // Wait for space to become available in the pool
            } catch (InterruptedException e) {
                System.out.println("Error occurred");
                LoggerUtil.log("ERROR", "Error occurred while waiting to add ticket");
            }
        }
        tickets.add(ticket); // Add the ticket to the pool
        totalTicketsAdded++; // Increment the total number of tickets added
        notifyAll(); // Notify all threads waiting for tickets that one has been added
    }

    /**
     * Buys a ticket from the pool. If no tickets are available, it waits until tickets are added.
     * <p>
     * This method is synchronized to ensure thread safety when removing tickets from the pool.
     * If the pool is empty, it waits until tickets are available.
     *
     * @return The ticket that was bought.
     */
    public synchronized Ticket buyTicket() {
        // Wait if there are no tickets in the pool
        while (tickets.isEmpty()) {
            try {
                System.out.println("No tickets available\n");
                LoggerUtil.log("INFO", "No tickets available");
                wait(); // Wait until tickets are added to the pool
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Ticket ticket = tickets.remove(0);  // Remove and return the first ticket from the pool

        totalSales += ticket.getPrice(); // Add the price of the ticket to total sales
        notifyAll(); // Notify all threads waiting for tickets that one has been bought
        return ticket;
    }

    /**
     * Gets the current size of the ticket pool.
     *
     * @return The current number of tickets in the pool.
     */
    public int getCurrentSize() {
        return tickets.size();
    }

    /**
     * Gets the maximum capacity of the ticket pool.
     *
     * @return The maximum number of tickets allowed in the pool.
     */

    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Gets the total number of tickets added to the pool.
     *
     * @return The total number of tickets added to the pool.
     */
    public int getTotalTicketsAdded() {
        return totalTicketsAdded;
    }

    /**
     * Gets the total sales amount from ticket purchases.
     *
     * @return The total sales amount.
     */
    public double getTotalSales() {
        return totalSales;
    }


}
