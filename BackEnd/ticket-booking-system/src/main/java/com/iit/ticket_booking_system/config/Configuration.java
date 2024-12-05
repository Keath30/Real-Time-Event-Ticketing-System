package com.iit.ticket_booking_system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;


//@org.springframework.context.annotation.Configuration
//@ConfigurationProperties(prefix = "ticket.pool")
public class Configuration implements Serializable {

    private int maxTicketCapacity;
    private int totalTickets;
    private int ticketsPerRelease;
    private int releaseInterval;
    private int retrievalInterval;
    private int ticketQuantity;


//    public Configuration(int maxTicketCapacity, int totalTickets, int ticketsPerRelease, int releaseInterval, int retrievalInterval, int ticketQuantity) {
//        this.maxTicketCapacity = maxTicketCapacity;
//        this.totalTickets = totalTickets;
//        this.ticketsPerRelease = ticketsPerRelease;
//        this.releaseInterval = releaseInterval;
//        this.retrievalInterval = retrievalInterval;
//        this.ticketQuantity = ticketQuantity;
//    }
//
    //Getters
    public int getMaxTicketCapacity(){
        return maxTicketCapacity;
    }

    public int getTotalTickets(){
        return totalTickets;
    }

    public int getTicketsPerRelease(){
        return ticketsPerRelease;
    }

    public int getReleaseInterval(){
        return releaseInterval;
    }

    public int getRetrievalInterval(){
        return retrievalInterval;
    }

    public int getTicketQuantity(){
        return ticketQuantity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity){
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public void setTotalTickets(int totalTickets){
        this.totalTickets = totalTickets;
    }

    public void setTicketsPerRelease(int ticketsPerRelease){
        this.ticketsPerRelease = ticketsPerRelease;
    }

    public void setReleaseInterval(int releaseInterval){
        this.releaseInterval = releaseInterval;
    }

    public void setRetrievalInterval(int retrievalInterval){
        this.retrievalInterval = retrievalInterval;
    }

    public void setTicketQuantity(int ticketQuantity){
        this.ticketQuantity = ticketQuantity;
    }
}
