package com.melissa.iit.cli.thread;

import com.melissa.iit.cli.Ticket;
import com.melissa.iit.cli.TicketPool;
import com.melissa.iit.cli.util.LoggerUtil;

import java.util.UUID;

/**
 * Represents a customer who purchases tickets from a ticket pool.
 * Each customer operates on a separate thread and buys tickets until the specified limit is reached.
 */
public class Customer implements Runnable {

    private final String customerId; // Unique ID for the customer
    private final String customerName; // Name of the customer
    private final int ticketRetrievalRate; // Time interval between ticket purchases
    private final int totalTickets; // Total number of tickets the customer wants to buy
    private final TicketPool ticketPool;


    /**
     * Constructs a new Customer instance.
     *
     * @param customerName        Name of the customer.
     * @param ticketRetrievalRate Interval (in seconds) between ticket purchases.
     * @param totalTickets        Total number of tickets the customer wants to buy.
     * @param ticketPool          The shared ticket pool.
     */
    public Customer(String customerName, int ticketRetrievalRate, int totalTickets, TicketPool ticketPool) {
        this.customerName = customerName;
        this.customerId = UUID.randomUUID().toString();
        this.ticketRetrievalRate = ticketRetrievalRate;
        this.totalTickets = totalTickets;
        this.ticketPool = ticketPool;
    }


    /**
     * The execution logic for the customer thread. It simulates buying tickets from the ticket pool
     * until the customer has bought the specified number of tickets. After each purchase, the customer
     * waits for the specified retrieval interval before purchasing another ticket.
     */
    @Override
    public void run() {
        try {
            int ticketsPurchased = 0;
            // Loop until the customer has bought the specified number of tickets
            while (ticketsPurchased < totalTickets && !ticketPool.isStopRequested()) {
                if(Thread.interrupted()){
                    System.out.println(customerName + " has been interrupted and is stopping.");
                    LoggerUtil.log("INFO", customerName + " has been interrupted and is stopping.");
                    return;
                }
                // Attempt to buy a ticket from the ticket pool
                Ticket ticket = ticketPool.buyTicket();

                // Check if a ticket was successfully bought
                if (ticket != null) {
                    String logMessage = "Ticket bought by " + customerName + " (" + customerId+ ") "  + ticket;
                    System.out.println(logMessage);
                    LoggerUtil.log("INFO", logMessage);

                    ticketsPurchased++; // Increment the number of tickets the customer has bought
                    String message = customerName + " bought " + ticketsPurchased + " tickets";
                    System.out.println(message);
                    LoggerUtil.log("INFO", message);
                    Thread.sleep(ticketRetrievalRate * 1000L); // Wait before the next ticket purchase
                }
            }
            System.out.println("Customer " + customerName + " has finished buying tickets");
            LoggerUtil.log("INFO", "Customer " + customerName + " has finished buying tickets");

        } catch (InterruptedException e) {
            String errorMessage = "Thread interrupted for customer " + customerName + " " + e.getMessage();
            System.out.println(errorMessage);
            LoggerUtil.log("ERROR", errorMessage);
            Thread.currentThread().interrupt();
        }catch (Exception e){
            System.out.println("Unexpected error for customer " + customerName);
            LoggerUtil.log("ERROR", "Unexpected error for customer " + customerName);
        }

    }

}


