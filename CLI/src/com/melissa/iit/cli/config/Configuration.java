package com.melissa.iit.cli.config;


import java.io.Serializable;

/**
 * Configuration class for managing the properties of the ticket pool.
 * This class is used to load configuration properties from external sources like `application.properties`.
 * It is designed for managing the ticket pool's settings such as total tickets, capacity, release rate, and retrieval rate.
 * <p>
 * This class implements {@link Serializable} for object serialization and deserialization.
 * </p>
 */
public class Configuration implements Serializable {

    private final int maxTicketCapacity; // maximum ticket capacity for the ticket pool
    private final int totalTickets;  // Total number of tickets available for purchase
    private final int quantity;  // Quantity of tickets per customer purchase
    private final int ticketReleaseRate; // Rate at which tickets are released into the pool
    private final int ticketRetrievalRate; // Rate at which tickets are retrieved from the pool by customers

    /**
     * Constructor to initialize the configuration with specified values.
     *
     * @param totalTickets           Total tickets available in the pool.
     * @param maxTicketCapacity     Maximum number of tickets the pool can hold.
     * @param quantity              Quantity of tickets per customer purchase.
     * @param ticketReleaseRate     Rate at which tickets are released into the pool.
     * @param ticketRetrievalRate   Rate at which customers retrieve tickets from the pool.
     */
    public Configuration(int totalTickets, int maxTicketCapacity, int quantity, int ticketReleaseRate, int ticketRetrievalRate){
        this.totalTickets = totalTickets;
        this.maxTicketCapacity = maxTicketCapacity;
        this.quantity = quantity;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketRetrievalRate = ticketRetrievalRate;
    }

    /**
     * Gets the total number of tickets available in the pool.
     *
     * @return the total number of tickets.
     */
    public int getTotalTickets(){
        return totalTickets;
    }

    /**
     * Gets the maximum capacity of the ticket pool.
     *
     * @return the maximum ticket capacity.
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }


    /**
     * Gets the quantity of tickets a customer can purchase at a time.
     *
     * @return the quantity of tickets per customer purchase.
     */
    public int getQuantity(){
        return quantity;
    }

    /**
     * Gets the ticket release rate (how quickly tickets are released into the pool).
     *
     * @return the ticket release rate.
     */
    public int getTicketReleaseRate(){
        return ticketReleaseRate;
    }

    /**
     * Gets the ticket retrieval rate (how quickly tickets are retrieved from the pool by customers).
     *
     * @return the ticket retrieval rate.
     */
    public int getTicketRetrievalRate(){
        return ticketRetrievalRate;
    }


}
