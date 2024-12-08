package com.iit.ticket_booking_system.model;

public class Ticket {

    private int ticketID; // Unique ticket ID
    private String eventName; //event name
    private double price; //price of ticket

    public Ticket(int ticketID, String eventName, double price){
        this.ticketID = ticketID;
        this.eventName = eventName;
        this.price = price;
    }

    public int getTicketID(){
        return ticketID;
    }

    public String getEventName(){
        return eventName;
    }

    public double getPrice(){
        return price;
    }

    public void setTicketID(int ticketID){
        this.ticketID = ticketID;
    }

    public void setEventName(String eventName){
        this.eventName = eventName;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public String toString(){
        return "Ticket Information: " + "Ticket ID: " + ticketID +
                " Event Name: " + eventName + " Ticket Price: " + price;
    }


}
