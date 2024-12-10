package com.melissa.iit.cli;

/**
 * Represents a Ticket in the ticket booking system.
 * A ticket has a unique ID, event name, and price.
 */
public class Ticket {

    private final int ticketID; // Unique ticket ID
    private final String eventName; //event name
    private final double price; //price of ticket

    /**
     * Constructs a new Ticket with the specified ticket ID, event name, and price.
     *
     * @param ticketID  the unique ID of the ticket
     * @param eventName the name of the event for the ticket
     * @param price     the price of the ticket
     */
    public Ticket(int ticketID, String eventName, double price) {
        this.ticketID = ticketID;
        this.eventName = eventName;
        this.price = price;
    }

    /**
     * Gets the unique ID of the ticket.
     *
     * @return the ticket ID
     */
    public int getTicketID() {
        return ticketID;
    }


    /**
     * Gets the name of the event for this ticket.
     *
     * @return the event name
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Gets the price of the ticket.
     *
     * @return the price of the ticket
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns a string representation of the ticket.
     *
     * @return a formatted string with ticket details
     */
    public String toString() {
        return "Ticket Information: " + "Ticket ID: " + ticketID +
                " Event Name: " + eventName + "Ticket Price: " + price;
    }


}
