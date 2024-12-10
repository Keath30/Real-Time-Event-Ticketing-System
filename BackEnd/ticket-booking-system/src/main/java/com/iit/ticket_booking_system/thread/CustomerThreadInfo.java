package com.iit.ticket_booking_system.thread;


/**
 * Encapsulates information about a customer and its associated thread.
 * This class is used to manage and track customer threads in the ticket booking system.
 */
public class CustomerThreadInfo {

    private final Customer customer; // Reference to the customer associated with the thread
    private final Thread customerThread; // The thread executing the customer's actions

    /**
     * Constructs a new instance of {@code CustomerThreadInfo}.
     *
     * @param customer       The customer object representing the customer.
     * @param customerThread The thread handling the customer's actions.
     */
    public CustomerThreadInfo(Customer customer, Thread customerThread) {
        this.customer = customer;
        this.customerThread = customerThread;
    }

    /**
     * Gets the customer associated with this thread.
     *
     * @return The {@link Customer} object.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Gets the thread managing the customer's actions.
     *
     * @return The {@link Thread} object.
     */
    public Thread getCustomerThread() {
        return customerThread;
    }
}
