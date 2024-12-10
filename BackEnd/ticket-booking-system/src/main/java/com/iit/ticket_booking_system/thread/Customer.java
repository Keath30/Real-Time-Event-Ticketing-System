package com.iit.ticket_booking_system.thread;

import com.iit.ticket_booking_system.model.Ticket;
import com.iit.ticket_booking_system.TicketPool;
import com.iit.ticket_booking_system.util.LoggerUtil;

import java.util.UUID;
public class Customer implements Runnable {

    private final String customerId; // Unique ID for the customer
    private final String customerName; // Name of the customer
    private final int retrievalInterval; // Time interval for each ticket purchase
    private final TicketPool ticketPool; // The ticket pool from which tickets are bought
    private final int totalTickets; // Total number of tickets the customer wants to buy
    private int ticketsAdded = 0; // Number of tickets the customer has bought


    /**
     * Constructor for the Customer class.
     * Initializes customer ID, name, retrieval interval, total tickets to buy, and the ticket pool.
     *
     * @param customerId      The unique identifier for the customer.
     * @param customerName    The name of the customer.
     * @param retrievalInterval The time interval (in seconds) between each ticket purchase.
     * @param totalTickets    The total number of tickets the customer wants to buy.
     * @param ticketPool      The pool of tickets to buy from.
     */
    public Customer(String customerId, String customerName, int retrievalInterval, int totalTickets, TicketPool ticketPool) {
        this.customerName = customerName;
        this.customerId = UUID.randomUUID().toString();
        this.retrievalInterval = retrievalInterval;
        this.ticketPool = ticketPool;
        this.totalTickets = totalTickets;
    }

    /**
     * Gets the customer ID.
     *
     * @return The unique identifier for the customer.
     */
    public String getCustomerId(){
        return customerId;
    }

    /**
     * Gets the customer name.
     *
     * @return The name of the customer.
     */
    public String getCustomerName(){
        return customerName;
    }

    /**
     * The run method for the customer thread. It simulates buying tickets from the ticket pool until
     * the specified total number of tickets is bought. After each purchase, it waits for the specified
     * retrieval interval before attempting to buy the next ticket.
     */
    @Override
    public void run() {
        // Loop until the customer has bought the specified number of tickets
        while (ticketsAdded < totalTickets) {
            Ticket ticket = ticketPool.buyTicket(); // Buy a ticket from the pool

            if(ticket!=null) {
                System.out.println("Ticket bought by " + customerName + " " + ticket);
                LoggerUtil.log("INFO", "Ticket bought by " + customerName + " " + ticket);

                ticketsAdded++; // Increment the number of tickets the customer has bought
                System.out.println(customerName + " bought " + ticketsAdded + " tickets\n");
                LoggerUtil.log("INFO", customerName + " bought " + ticketsAdded + " tickets");
            }
            try {
                Thread.sleep(retrievalInterval * 1000L); // Wait before the next ticket purchase
            } catch (InterruptedException e) {
                LoggerUtil.log("ERROR", "Thread interrupted for customer " + customerName + " " + e.getMessage());
            }
        }
        System.out.println("Customer " + customerName + " has finished buying tickets\n");
        LoggerUtil.log("INFO","Customer " + customerName + " has finished buying tickets");
    }

}


