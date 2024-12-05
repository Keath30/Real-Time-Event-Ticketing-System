package com.iit.ticket_booking_system.service;

import com.iit.ticket_booking_system.model.Ticket;

import java.util.UUID;


public class Vendor implements Runnable{

    private final String vendorId;
    private final String vendorName;
    private final int ticketsPerRelease; //  Number of tickets a vendor adds in one release
    private final int releaseInterval; // Time interval between releases
    private final TicketPool ticketPool;
    private final int totalTickets;
    private int ticketsAdded = 0;

    public Vendor(String vendorName, TicketPool ticketPool, int ticketsPerRelease, int releaseInterval, int totalTickets){
        this.vendorName = vendorName;
        this.totalTickets = totalTickets;
        this.vendorId = UUID.randomUUID().toString();
        this.ticketPool = ticketPool;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
    }

    public String getVendorId(){
        return vendorId;
    }
    @Override
    public void run() {
       while (ticketsAdded < totalTickets) {
           synchronized (ticketPool){
               for (int i = 0; i < ticketsPerRelease; i++) {
                   if(ticketsAdded >= totalTickets || ticketPool.getCurrentSize() >= ticketPool.getMaxCapacity()){
                       break;
                   }

                   String eventName = "Concert" + (ticketsAdded+1);
                   double price = 100.0;

                   Ticket ticket = new Ticket(ticketsAdded+1,eventName,price);
                   ticketPool.addTickets(ticket);
                   System.out.println("Ticket added by " + vendorId + "\n");
                   ticketsAdded++;
               }
               try {
                   Thread.sleep(releaseInterval* 1000);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
       }
    }
}
