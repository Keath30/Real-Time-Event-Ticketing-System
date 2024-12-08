package com.iit.ticket_booking_system.thread;

import com.iit.ticket_booking_system.service.Customer;

public class CustomerThreadInfo {

    private Customer customer;
    private Thread customerThread;

    public CustomerThreadInfo(Customer customer, Thread customerThread){
        this.customer = customer;
        this.customerThread = customerThread;
    }

    public Customer getCustomer(){
        return customer;
    }

    public Thread getCustomerThread(){
        return customerThread;
    }
}
