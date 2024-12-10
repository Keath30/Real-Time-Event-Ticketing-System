package com.iit.ticket_booking_system.service;

import com.iit.ticket_booking_system.TicketPool;
import com.iit.ticket_booking_system.config.Configuration;
import com.iit.ticket_booking_system.config.ConfigurationManager;
import com.iit.ticket_booking_system.thread.Customer;
import com.iit.ticket_booking_system.thread.CustomerThreadInfo;
import com.iit.ticket_booking_system.thread.Vendor;
import com.iit.ticket_booking_system.thread.VendorThreadInfo;
import com.iit.ticket_booking_system.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class that handles the ticket booking system logic, including managing vendors,
 * customers, and the overall system status.
 */
@Service
public class TicketService {

    private final TicketPool ticketPool;
    private boolean isRunning = false;
    private final List<VendorThreadInfo> vendorThreads = new ArrayList<>();
    private final List<CustomerThreadInfo> customerThreads = new ArrayList<>();


    /**
     * Constructor to initialize TicketService with the given ticket pool.
     *
     * @param ticketPool The ticket pool used to manage the available tickets.
     */
    @Autowired
    public TicketService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    /**
     * Starts the ticket booking system with the specified maximum ticket capacity.
     *
     * @param maxCapacity The maximum capacity of the ticket pool.
     * @return A message indicating the system startup status.
     */
    public String startSystem(int maxCapacity) {
        LoggerUtil.log("INFO", "Attempting to start the system");

        // Check if the system is already running
        if (isRunning) {
            LoggerUtil.log("WARNING", "System is already running");
            return "System is already Running";
        }

        // If maxCapacity is not 0, set the configuration for the system
        if (maxCapacity != 0) {
            Configuration configuration = new Configuration();
            configuration.setMaxTicketCapacity(maxCapacity);
            ConfigurationManager.saveConfiguration(configuration, "config.json");

        }
        isRunning = true; // Mark the system as running
        LoggerUtil.log("INFO", "System started successfully!");
        System.out.println("System started successfully!");
        return "System started successfully!";
    }

    /**
     * Stops the ticket booking system and interrupts all vendor and customer threads.
     * Clears the ticket sales and logs.
     *
     * @return A message indicating the system stop status.
     */
    public synchronized String stopSystem() {
        LoggerUtil.log("INFO", "Attempting to stop the system");

        // Check if the system is running
        if (!isRunning) {
            LoggerUtil.log("WARNING", "System is not running!");
            return "System is not running!";
        }
        isRunning = false;

        // Interrupt all vendor threads
        for (VendorThreadInfo vendorThreadInfo : vendorThreads) {
            vendorThreadInfo.getVendorThread().interrupt();
        }
        vendorThreads.clear(); // Clear the vendor thread list

        // Interrupt all customer threads
        for (CustomerThreadInfo customerThreadInfo : customerThreads) {
            customerThreadInfo.getCustomerThread().interrupt();
        }

        customerThreads.clear(); // Clear the customer thread list

        ticketPool.clearSales();  // Clear sales records


        System.out.println("System stopped successfully!");
        LoggerUtil.log("INFO", "System stopped successfully!");
        return "System stopped successfully!";
    }

    /**
     * Checks if the ticket booking system is currently running.
     *
     * @return true if the system is running, false otherwise.
     */
    public synchronized boolean isSystemRunning() {
        return isRunning;
    }

    /**
     * Retrieves the maximum capacity of the ticket pool.
     *
     * @return The maximum ticket capacity.
     */
    public synchronized int getMaxCapacity() {
        return ticketPool.getMaxCapacity();
    }

    /**
     * Retrieves the total sales value from the ticket pool.
     *
     * @return The total sales value.
     */
    public synchronized double getTotalSales() {
        return ticketPool.getTotalSales();
    }

    /**
     * Adds a vendor to the system and starts their respective thread.
     *
     * @param id                The vendor's unique identifier.
     * @param name              The vendor's name.
     * @param eventName         The event associated with the vendor.
     * @param ticketsPerRelease Number of tickets released by the vendor at a time.
     * @param releaseInterval   The time interval between each ticket release.
     * @param totalTickets      The total number of tickets available for release.
     * @param price             The price of each ticket.
     */
    public void addVendor(String id, String name, String eventName, int ticketsPerRelease, int releaseInterval, int totalTickets, double price) {
        // Ensure the system is running before adding a vendor
        if (!isRunning) {
            System.out.println("System is not started");
            LoggerUtil.log("WARNING", "System is not started!");
        }

        Vendor vendor = new Vendor(id, name, eventName, ticketPool, ticketsPerRelease, releaseInterval, totalTickets, price);
        Thread vendorThread = new Thread(vendor, "Vendor: " + vendor.getVendorId());
        VendorThreadInfo vendorThreadInfo = new VendorThreadInfo(vendor, vendorThread);
        vendorThreads.add(vendorThreadInfo); // Add vendor to the vendor list
        vendorThread.start(); // Start the vendor thread
        System.out.println("Vendor added and started: " + vendor.getVendorName());
        LoggerUtil.log("INFO", "Vendor added and started: " + vendor.getVendorName());
    }

    /**
     * Adds a customer to the system and starts their respective thread.
     *
     * @param id                The customer's unique identifier.
     * @param name              The customer's name.
     * @param retrievalInterval The time interval between customer ticket retrievals.
     * @param totalTickets      The total number of tickets the customer intends to retrieve.
     */
    public void addCustomer(String id, String name, int retrievalInterval, int totalTickets) {
        // Ensure the system is running before adding a customer
        if (!isRunning) {
            System.out.println("System is not started");
            LoggerUtil.log("WARNING", "System is not started!");
        }

        Customer customer = new Customer(id, name, retrievalInterval, totalTickets, ticketPool);
        Thread customerThread = new Thread(customer, "Customer: " + customer.getCustomerId());
        CustomerThreadInfo customerThreadInfo = new CustomerThreadInfo(customer, customerThread);
        customerThreads.add(customerThreadInfo); // Add customer to the customer list
        customerThread.start();  // Start the customer thread
        System.out.println("Customer added and started: " + customer.getCustomerName());
        LoggerUtil.log("INFO", "Customer added and started: " + customer.getCustomerName());
    }

    /**
     * Removes a vendor from the system and interrupts their respective thread.
     *
     * @param vendorName The name of the vendor to be removed.
     */
    public void removeVendor(String vendorName) {

        // Iterate through the vendor list to find and remove the vendor by name
        for (int i = 0; i < vendorThreads.size(); i++) {
            VendorThreadInfo vendorToRemove = vendorThreads.get(i);

            if (vendorToRemove.getVendor().getVendorName().trim().equalsIgnoreCase(vendorName.trim())) {
                vendorToRemove.getVendorThread().interrupt(); // Interrupt the vendor thread
                vendorThreads.remove(i);  // Remove the vendor from the list
                System.out.println("Vendor: " + vendorName + " removed successfully");
                LoggerUtil.log("INFO", "Vendor: " + vendorName + " removed successfully");
                return;
            }
            System.out.println(vendorToRemove.getVendor().getVendorName());
        }

        // Vendor was not found in the list
        System.out.println("Vendor: " + vendorName + " not found");
        LoggerUtil.log("INFO", "Vendor: " + vendorName + " not found");
    }

    /**
     * Removes a customer from the system and interrupts their respective thread.
     *
     * @param customerName The name of the customer to be removed.
     */
    public void removeCustomer(String customerName) {

        // Iterate through the customer list to find and remove the customer by name
        for (int i = 0; i < customerThreads.size(); i++) {
            CustomerThreadInfo customerToRemove = customerThreads.get(i);

            if (customerToRemove.getCustomer().getCustomerName().trim().equalsIgnoreCase(customerName.trim())) {
                customerToRemove.getCustomerThread().interrupt(); // Interrupt the customer thread
                customerThreads.remove(i); // Remove the customer from the list
                System.out.println("Customer: " + customerName + " removed successfully");
                LoggerUtil.log("INFO", "Customer: " + customerName + " removed successfully");
                return;
            }
            System.out.println(customerToRemove.getCustomer().getCustomerName());
        }

        // Customer was not found in the list
        System.out.println("Customer: " + customerName + " not found");
        LoggerUtil.log("INFO", "Customer: " + customerName + " not found");

    }

    /**
     * Retrieves the current status of the ticket system as a map of key-value pairs.
     * The status includes details like the current ticket pool size, total tickets added,
     * maximum capacity, and the count of active vendors and customers.
     *
     * @return A map containing the current status of the ticket system.
     */
    public Map<String, Object> getTicketStatus() {
        Map<String, Object> ticketStatus = new HashMap<>();

        // Add the current size of the ticket pool to the status map
        ticketStatus.put("currentSize", ticketPool.getCurrentSize());

        // Add the total number of tickets added to the pool since system start
        ticketStatus.put("totalTicketsAdded", ticketPool.getTotalTicketsAdded());

        // Add the maximum capacity of the ticket pool
        ticketStatus.put("maxCapacity", ticketPool.getMaxCapacity());

        // Add the number of currently active vendor threads
        ticketStatus.put("activeVendors", vendorThreads.size());

        // Add the number of currently active customer threads
        ticketStatus.put("activeCustomers", customerThreads.size());

        return ticketStatus;

    }

}
