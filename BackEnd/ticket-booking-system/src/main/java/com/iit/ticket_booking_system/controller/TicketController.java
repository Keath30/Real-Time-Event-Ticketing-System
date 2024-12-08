package com.iit.ticket_booking_system.controller;

import com.iit.ticket_booking_system.model.Sales;
import com.iit.ticket_booking_system.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5176")   // Allow CORS for this controller
@RequestMapping("/api/tickets")

public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> start() {
        Map<String, Object> response = new HashMap<>();
        String message = ticketService.startSystem();
        response.put("message", message);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stop")
    public ResponseEntity<Map<String, Object>> stop() {
        Map<String, Object> response = new HashMap<>();
        String message = ticketService.stopSystem();
        response.put("message", message);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sales")
    public List<Sales> getSales(){
        return ticketService.getSalesList();
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> response = new HashMap<>();
        String message = ticketService.isSystemRunning() ? "Running" : "Stopped";
        response.put("message", message);
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }


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
            String id = UUID.randomUUID().toString();
            ticketService.addVendor(id, name, eventName, ticketsPerRelease, releaseInterval, totalTickets, price);
            response.put("message", "Vendor added and processing tickets");
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid Input: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }

    }

    @PostMapping("/customer/add")
    public ResponseEntity<Map<String, Object>> addCustomer(
            @RequestParam String name,
            @RequestParam int retrievalInterval,
            @RequestParam int totalTickets
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            String id = UUID.randomUUID().toString();
            ticketService.addCustomer(id, name, retrievalInterval, totalTickets);
            response.put("message", "Customer " + name + " added and retrieving tickets");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("message", "Invalid Input: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }

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
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }

    }

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
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("message", "Error: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/tickets")
    public ResponseEntity<Map<String, Object>> getTicketStatus() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String,Object> ticketStatus = ticketService.getTicketStatus();
            response.put("status", "success");
            response.put("ticketStatus", ticketStatus);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error: " + e.getMessage());

            return ResponseEntity.status(500).body(response);
        }
    }
}

