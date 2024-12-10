package com.iit.ticket_booking_system;

import com.iit.ticket_booking_system.config.Configuration;
import com.iit.ticket_booking_system.config.ConfigurationManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

/**
 * Main entry point for the Ticket Booking System application.
 * Initializes the system configuration and starts the Spring Boot application.
 */
@SpringBootApplication
public class TicketBookingSystemApplication {

    /**
     * Main method that runs the Ticket Booking System application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        System.out.println("""
                ***************************
                WELCOME TO TICKET BOOKING APPLICATION
                ***************************
                """);

        // Prompting the user to choose between loading a configuration or entering a new one
        System.out.println("\nEnter\n1 - Load configuration from file\n2 - Enter new configuration");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice == 1) {
            // Attempt to load configuration from a predefined JSON file
            Configuration config = ConfigurationManager.loadConfiguration("config.json");
            if (config == null) {
                System.out.println("Failed to load configuration");
            } else {
                System.out.println("Configuration loaded successfully");
            }

        } else {
            Configuration newConfig = new Configuration();

            // Prompting user for maximum ticket capacity and setting it in the configuration
            System.out.println("Enter Maximum Ticket Capacity: ");
            newConfig.setMaxTicketCapacity(scanner.nextInt());

            // Saving the new configuration to both JSON and text files
            ConfigurationManager.saveConfiguration(newConfig, "config.json");
            ConfigurationManager.saveConfigurationToTextFile(newConfig, "config.txt");

            System.out.println("New configuration saved!");


        }
        // Starting the Spring Boot application
        SpringApplication.run(TicketBookingSystemApplication.class, args);

    }

}
