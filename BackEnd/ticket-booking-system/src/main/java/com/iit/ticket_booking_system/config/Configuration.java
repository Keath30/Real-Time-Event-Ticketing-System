package com.iit.ticket_booking_system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Configuration class for managing the properties of the ticket pool.
 * This class is annotated with @Configuration to make it a Spring Bean.
 * It is used to load configuration properties from application.properties or application.properties.
 */


@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "ticket.pool") // Binds the properties with the "ticket.pool" prefix
public class Configuration {

    private int maxTicketCapacity; // Variable to store the maximum ticket capacity for the ticket pool

    /**
     * Gets the maximum ticket capacity.
     *
     * @return the maxTicketCapacity
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    /**
     * Sets the maximum ticket capacity.
     *
     * @param maxTicketCapacity the maxTicketCapacity to set
     */

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

}
