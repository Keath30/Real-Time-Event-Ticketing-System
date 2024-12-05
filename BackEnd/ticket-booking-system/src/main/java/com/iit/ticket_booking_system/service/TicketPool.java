package com.iit.ticket_booking_system.service;

import com.iit.ticket_booking_system.config.Configuration;
import com.iit.ticket_booking_system.config.ConfigurationManager;
import com.iit.ticket_booking_system.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class TicketPool {

    private final List<Ticket> tickets = Collections.synchronizedList(new LinkedList<>()); //store tickets
    private static int totalTicketsAdded = 0; //Total number of tickets added by vendors
    private final int maxCapacity; //Maximum tickets allowed in pool

    public TicketPool(){
        ConfigurationManager configurationManager = new ConfigurationManager();
        Configuration config = configurationManager.loadConfiguration("config.json");
        this.maxCapacity = config.getMaxTicketCapacity();
    }

    public synchronized void addTickets(Ticket ticket)  {
        while (tickets.size() >= maxCapacity){
            System.out.println("Ticket pool is full\n");
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        tickets.add(ticket);
        totalTicketsAdded++;
        System.out.println("Number of Tickets in the pool: " + getCurrentSize() + "\n");
        notifyAll();
    }

    public synchronized Ticket buyTicket() {
        while (tickets.isEmpty()){
            try {
                System.out.println("No tickets available.");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Ticket ticket = tickets.remove(0);
        notifyAll();
        System.out.println("Number of Tickets in the pool: " + getCurrentSize() + "\n");
        return ticket;
    }

    public int getCurrentSize(){
        return tickets.size();
    }

    public int getMaxCapacity(){
        return maxCapacity;
    }

    public int getTotalTicketsAdded(){
        return totalTicketsAdded;
    }


}
