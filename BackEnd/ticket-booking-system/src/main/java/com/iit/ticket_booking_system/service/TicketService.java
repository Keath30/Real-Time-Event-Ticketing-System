package com.iit.ticket_booking_system.service;

import com.iit.ticket_booking_system.model.Sales;
import com.iit.ticket_booking_system.thread.CustomerThreadInfo;
import com.iit.ticket_booking_system.thread.VendorThreadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {

    private final TicketPool ticketPool;
    private boolean isRunning = false;
    private final List<VendorThreadInfo> vendorThreads = new ArrayList<>();
    private final List<CustomerThreadInfo> customerThreads = new ArrayList<>();
    private final List<Sales> salesList = new ArrayList<>();

    @Autowired
    public TicketService(TicketPool ticketPool){
        this.ticketPool = ticketPool;

    }

    public synchronized String startSystem(){
        if(isRunning){
            return "System is already Running";
        }
        isRunning = true;
        System.out.println("System started successfully!");
        return "System started successfully!";
    }

    public synchronized String stopSystem(){
        if(!isRunning){
            return "System is not running!";
        }
        isRunning = false;

        for(VendorThreadInfo vendorThreadInfo: vendorThreads){
            vendorThreadInfo.getVendorThread().interrupt();
        }
        vendorThreads.clear();

        for(CustomerThreadInfo customerThreadInfo: customerThreads){
            customerThreadInfo.getCustomerThread().interrupt();
        }
        customerThreads.clear();

        System.out.println("System stopped successfully!");
        return "System stopped successfully!";
    }

    public synchronized boolean isSystemRunning(){
        return isRunning;
    }
    public void addVendor(String id, String name, String eventName, int ticketsPerRelease, int releaseInterval, int totalTickets, double price){
        if(!isRunning){
            throw new IllegalStateException("System is not started");
        }

        Vendor vendor = new Vendor(id, name, eventName,ticketPool, ticketsPerRelease, releaseInterval, totalTickets, price);
        Thread vendorThread = new Thread(vendor, "Vendor: " + vendor.getVendorId());
        VendorThreadInfo vendorThreadInfo = new VendorThreadInfo(vendor, vendorThread);
        vendorThreads.add(vendorThreadInfo);
        vendorThread.start();
        System.out.println("Vendor added and started: " + vendor.getVendorName());
    }

    public void addCustomer(String id, String name, int retrievalInterval, int totalTickets){
        if(!isRunning){
            throw new IllegalStateException("System is not started");
        }

        Customer customer = new Customer(id, name, retrievalInterval, totalTickets, ticketPool);
        Thread customerThread = new Thread(customer, "Customer: " + customer.getCustomerId());
        CustomerThreadInfo customerThreadInfo = new CustomerThreadInfo(customer, customerThread);
        customerThreads.add(customerThreadInfo);
        customerThread.start();
        System.out.println("Customer added and started: " + customer.getCustomerName());
    }

    public void removeVendor(String vendorName){
        VendorThreadInfo removedVendor = null;

        for (int i = 0; i < vendorThreads.size(); i++) {
            VendorThreadInfo vendorToRemove = vendorThreads.get(i);

            if (vendorToRemove.getVendor().getVendorName().trim().equalsIgnoreCase(vendorName.trim())) {
                vendorToRemove.getVendorThread().interrupt();
                vendorThreads.remove(i);
                System.out.println("Vendor: " + vendorName + " removed successfully");
                return;
            }
            System.out.println(vendorToRemove.getVendor().getVendorName());
        }

        System.out.println("Vendor: " + vendorName + " not found");
    }


    public void removeCustomer(String customerName){
        CustomerThreadInfo removedCustomer = null;

        for (int i = 0; i < customerThreads.size(); i++) {
            CustomerThreadInfo customerToRemove = customerThreads.get(i);

            if (customerToRemove.getCustomer().getCustomerName().trim().equalsIgnoreCase(customerName.trim())) {
                customerToRemove.getCustomerThread().interrupt();
                customerThreads.remove(i);
                System.out.println("Customer: " + customerName + " removed successfully");
                return;
            }
            System.out.println(customerToRemove.getCustomer().getCustomerName());
        }

        System.out.println("Customer: " + customerName + " not found");

    }

    public Map<String,Object> getTicketStatus(){
        Map<String,Object> ticketStatus = new HashMap<>();
        ticketStatus.put("currentSize",ticketPool.getCurrentSize());
        ticketStatus.put("totalTicketsAdded",ticketPool.getTotalTicketsAdded());
        ticketStatus.put("maxCapacity",ticketPool.getMaxCapacity());
        ticketStatus.put("activeVendors", vendorThreads.size());
        ticketStatus.put("activeCustomers",customerThreads.size());
        return ticketStatus;

    }

    public void logSale(int ticketsSold){
        synchronized (salesList){
            salesList.add(new Sales(System.currentTimeMillis(),ticketsSold));
        }
    }

    public List<Sales> getSalesList(){
        synchronized (salesList){
            return new ArrayList<>(salesList);
        }
    }

    public List<VendorThreadInfo> getVendorThreads(){
        return vendorThreads;
    }

    public List<CustomerThreadInfo> getCustomerThreads(){
        return customerThreads;
    }
}
