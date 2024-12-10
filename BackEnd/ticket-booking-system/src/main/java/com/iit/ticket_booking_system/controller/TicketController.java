package com.iit.ticket_booking_system.controller;

import com.iit.ticket_booking_system.service.TicketService;
import com.iit.ticket_booking_system.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller for handling all ticket-related requests.
 * This includes starting and stopping the ticketing system, adding/removing vendors and customers,
 * fetching system status, ticket status, and sales, and retrieving logs.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173")   // Allow CORS for this controller
@RequestMapping("/api/tickets")

public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Starts the ticketing system with the given max capacity.
     *
     * @param maxCapacity the maximum capacity of the system
     * @return a response containing the status of the system start
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> start(
            @RequestParam int maxCapacity
    ) {
        Map<String, Object> response = new HashMap<>();
        try {

            String message = ticketService.startSystem(maxCapacity);
            response.put("message", message);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error starting the system: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Stops the ticketing system.
     *
     * @return a response containing the status of the system stop
     */
    @PostMapping("/stop")
    public ResponseEntity<Map<String, Object>> stop() {
        Map<String, Object> response = new HashMap<>();
        try {
            String message = ticketService.stopSystem();
            response.put("message", message);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error stopping the system: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Retrieves the current status of the system (running or stopped).
     *
     * @return a response containing the current status of the system
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> response = new HashMap<>();
        try {
            String message = ticketService.isSystemRunning() ? "Running" : "Stopped";
            response.put("message", message);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error fetching the system status: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Retrieves the current configuration of the system, such as max capacity.
     *
     * @return a response containing the system's configuration
     */
    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> getConfiguration() {
        Map<String, Object> response = new HashMap<>();
        try {
            int maxCapacity = ticketService.getMaxCapacity();
            response.put("maxCapacity", maxCapacity);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error fetching configuration: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }

    }

    /**
     * Adds a new vendor to the ticketing system with the specified parameters.
     *
     * @param name              the name of the vendor
     * @param eventName         the event associated with the vendor
     * @param ticketsPerRelease the number of tickets to release per interval
     * @param releaseInterval   the interval (in milliseconds) at which tickets will be released
     * @param totalTickets      the total number of tickets to be sold by the vendor
     * @param price             the price of each ticket
     * @return a response confirming the addition of the vendor
     */
    @PostMapping("/vendor/add")
    public ResponseEntity<Map<String, Object>> addVendor(
            @RequestParam String name,
            @RequestParam String eventName,
            @RequestParam int ticketsPerRelease,
            @RequestParam int releaseInterval,
            @RequestParam int totalTickets,
            @RequestParam double price
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            String id = UUID.randomUUID().toString(); // Generating unique ID for the vendor
            ticketService.addVendor(id, name, eventName, ticketsPerRelease, releaseInterval, totalTickets, price);
            response.put("message", "Vendor added and processing tickets");
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid Input: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response); // 400 Bad Request for invalid inputs
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error for unexpected issues
        }
    }

    /**
     * Adds a new customer to the ticketing system with the specified parameters.
     *
     * @param name              the name of the customer
     * @param retrievalInterval the interval (in seconds) at which the customer will retrieve tickets
     * @param totalTickets      the total number of tickets the customer is allowed to retrieve
     * @return a response confirming the addition of the customer
     */
    @PostMapping("/customer/add")
    public ResponseEntity<Map<String, Object>> addCustomer(
            @RequestParam String name,
            @RequestParam int retrievalInterval,
            @RequestParam int totalTickets
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            String id = UUID.randomUUID().toString(); // Generating unique ID for the customer
            ticketService.addCustomer(id, name, retrievalInterval, totalTickets);
            response.put("message", "Customer " + name + " added and retrieving tickets");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid Input: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response); // 400 Bad Request for invalid inputs
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error for unexpected issues
        }
    }

    /**
     * Removes a vendor from the system.
     *
     * @param name the name of the vendor to be removed
     * @return a response confirming the removal of the vendor
     */
    @DeleteMapping("/vendor/remove")
    public ResponseEntity<Map<String, Object>> removeVendor(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            ticketService.removeVendor(name);
            response.put("message", "Vendor removed");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid Input: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response); // 400 Bad Request for invalid inputs
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error for unexpected issues
        }

    }

    /**
     * Removes a customer from the system.
     *
     * @param name the name of the customer to be removed
     * @return a response confirming the removal of the customer
     */
    @DeleteMapping("/customer/remove")
    public ResponseEntity<Map<String, Object>> removeCustomer(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            ticketService.removeCustomer(name);
            response.put("message", "Customer removed");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid Input: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response); // 400 Bad Request for invalid inputs
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error for unexpected issues
        }
    }

    /**
     * Retrieves the current ticket status (e.g., number of tickets available).
     *
     * @return a response containing the ticket status
     */
    @GetMapping("/tickets")
    public ResponseEntity<Map<String, Object>> getTicketStatus() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> ticketStatus = ticketService.getTicketStatus();
            response.put("status", "success");
            response.put("ticketStatus", ticketStatus);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());

            return ResponseEntity.status(500).body(response); // 500 Internal Server Error for unexpected issues
        }
    }

    /**
     * Retrieves the total sales from the ticketing system.
     *
     * @return a response containing the total sales
     */
    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> getTotalSales() {
        Map<String, Object> response = new HashMap<>();
        try {
            double ticketSales = ticketService.getTotalSales();
            response.put("status", "success");
            response.put("ticketSales", ticketSales);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error for unexpected issues
        }
    }

    /**
     * Retrieves the logs for the ticketing system.
     *
     * @return a response containing the logs
     */
    @GetMapping("/logs")
    public ResponseEntity<Map<String, Object>> getLogs() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<String> logs = LoggerUtil.getLogs();
            response.put("status", "success");
            response.put("logs", logs);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error for unexpected issues
        }

    }
}

