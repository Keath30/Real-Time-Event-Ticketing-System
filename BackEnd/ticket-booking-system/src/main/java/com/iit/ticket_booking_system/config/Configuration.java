package com.iit.ticket_booking_system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;


@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "ticket.pool")
public class Configuration implements Serializable {

    private int maxTicketCapacity;

    //Getters and Setters
    public int getMaxTicketCapacity(){
        return maxTicketCapacity;
    }


    public void setMaxTicketCapacity(int maxTicketCapacity){
        this.maxTicketCapacity = maxTicketCapacity;
    }

}
