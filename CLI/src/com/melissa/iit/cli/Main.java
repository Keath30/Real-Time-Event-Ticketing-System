package com.melissa.iit.cli;

import com.melissa.iit.cli.config.Configuration;
import com.melissa.iit.cli.config.ConfigurationManager;
import com.melissa.iit.cli.thread.Customer;
import com.melissa.iit.cli.thread.Vendor;
import com.melissa.iit.cli.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Main class for the Ticket Booking Application.
 * Handles the configuration setup, ticket management, and multiThreaded customer and vendor operations.
 */
public class Main {
    static Scanner scanner = new Scanner(System.in);
    private static final List<Thread> vendorThreads = new ArrayList<>();
    private static final List<Thread> customerThreads = new ArrayList<>();
    private static boolean isRunning = false;


    public static void main(String[] args) {
        TicketPool ticketPool = new TicketPool();

        System.out.println("""
                ***************************
                WELCOME TO TICKET BOOKING APPLICATION
                ***************************
                """);
        System.out.println("Type 'start' to to begin ticket handling\n" +
                "Type 'stop' to stop ticket handling\n");

        while (true) {
            System.out.println("Enter command: ");
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("start")) {
                start();
            } else if (command.equalsIgnoreCase("stop")) {
                ticketPool.requestStop();
                System.out.println("stopped");
                break;
                
            }else {
                System.out.println("Invalid Command.");
            }
        }

    }

        public static void start(){

        if (isRunning){
            System.out.println("Ticket handling system is already running");
        }
            int totalTickets = 0;
            int maxTicketCapacity;
            int quantity = 0;
            int ticketReleaseRate = 0;
            int ticketRetrievalRate = 0;
            int vendorsCount;
            int customersCount;
            int ticketsPerRelease = 0;
            double price = 0;
            String eventName = "movie";
            
        // Prompting the user to choose between loading a configuration or entering a new one
        System.out.println("\nEnter\n1 - Load configuration from file\n2 - Enter new configuration");

        int choice = inputTypeValidation(scanner);

        if (choice == 1) {
            // Attempt to load configuration from a predefined JSON file
            Configuration config = ConfigurationManager.loadConfiguration("config.txt");
            if (config == null) {
                System.out.println("Failed to load configuration. Please enter a new configuration");
                choice = 2; // Prompt user to enter new configuration if loading fails
            } else {
                System.out.println("Configuration loaded successfully");
                LoggerUtil.log("INFO", "Configuration loaded successfully");

                // Retrieve configuration values
                totalTickets = config.getTotalTickets();
                ticketReleaseRate = config.getTicketReleaseRate();
                ticketRetrievalRate = config.getTicketRetrievalRate();
                quantity = config.getQuantity();
            }

        }

        if(choice == 2) {
            // Collect user inputs for creating a new configuration
            totalTickets = inputValidation(scanner, "Enter the total number of tickets added by the vendor: ");
            maxTicketCapacity = inputValidation(scanner, "Enter the maximum ticket capacity: ");
            ticketReleaseRate = inputValidation(scanner, "Enter ticket release rate in seconds: ");
            ticketRetrievalRate = inputValidation(scanner, "Enter ticket retrieval rate in seconds: ");
            quantity = inputValidation(scanner, "Enter the number of tickets to be bought by each customer: ");


            // Save the new configuration
            Configuration newConfig = new Configuration(totalTickets, maxTicketCapacity, quantity, ticketReleaseRate, ticketRetrievalRate);
            ConfigurationManager.saveConfigurationToTextFile(newConfig, "config.txt");
            System.out.println("New configuration saved!");

        }

        // Prompt for the number of vendors and customers
            ticketsPerRelease = inputValidation(scanner, "Enter the number of tickets per release: ");
            price = inputValidation(scanner, "Enter the price of a ticket: ");
        vendorsCount = inputValidation(scanner, "Enter the number of vendors: ");
        customersCount = inputValidation(scanner, "Enter the number of customers: ");

        TicketPool ticketPool = new TicketPool();

        // Create and start vendor threads
        for (int i = 0; i < vendorsCount; i++) {
            String name = "Vendor " + i;
            String id = UUID.randomUUID().toString();

            // Vendors add tickets to the pool
            Vendor vendor = new Vendor(id, name, eventName, ticketPool, ticketsPerRelease, ticketReleaseRate, totalTickets, price);
            Thread vendorThread = new Thread(vendor);
            vendorThread.start();
            vendorThreads.add(vendorThread);
        }

        // Create and start customer threads
        for (int i = 0; i < customersCount; i++) {
            String name = "Customer " + i;

            // Customers retrieve tickets from the pool
            Customer customer = new Customer(name, ticketRetrievalRate, quantity, ticketPool);
            Thread customerThread = new Thread(customer);
            customerThread.start();
            customerThreads.add(customerThread);
        }
        isRunning = true;
            System.out.println("Ticket handling system started");
            LoggerUtil.log("INFO","Ticket handling system started");

    }

//    public static void stop(){
//        if(!isRunning){
//            System.out.println("Ticket handling system is not running");
//            return;
//        }
//
//        for(Thread thread: vendorThreads){
//            thread.interrupt();
//        }
//
//        for(Thread thread: customerThreads){
//            thread.interrupt();
//        }
//        vendorThreads.clear();
//        customerThreads.clear();
//
//        try{
//            for(Thread thread: vendorThreads){
//                thread.join();
//            }
//            for(Thread thread: customerThreads){
//                thread.join();
//            }
//        }catch (InterruptedException e){
//            System.out.println("Error while stopping threads");
//        }
//
//        isRunning = false;
//        System.out.println("Ticket handling system stopped");
//        LoggerUtil.log("INFO", "Ticket handling system stopped");
//    }

    /**
     * Validates integer input from the user.
     * Ensures the value is a positive integer.
     *
     * @param scanner Scanner object for user input.
     * @param prompt  The message to display to the user.
     * @return A valid positive integer entered by the user.
     */
    public static int inputValidation(Scanner scanner, String prompt) {
        int output;
        while (true) {
            System.out.println(prompt);
            if (scanner.hasNextInt()) {
                output = scanner.nextInt();
                if (output > 0) {
                    break;
                } else {
                    System.out.println("Invalid Input. Value must be between greater than zero");
                }
            } else {
                System.out.println("Invalid Input. Please enter an integer");
                scanner.next(); // Clear the invalid input
            }
        }
        return output;
    }

    /**
     * Validates the user's choice input to ensure it is 1 or 2.
     *
     * @param scanner Scanner object for user input.
     * @return A valid choice (1 or 2).
     */
    public static int inputTypeValidation(Scanner scanner){
        while (!scanner.hasNextInt()){
            System.out.println("Enter 1 or 2: ");
            scanner.next();
        }
        int choice = scanner.nextInt();

        // Recursive call for invalid choice
        if (choice!=1 && choice!=2){
            System.out.println("Invalid choice. Enter 1 or 2: ");
            return inputTypeValidation(scanner);
        }
        return choice;
    }
}
