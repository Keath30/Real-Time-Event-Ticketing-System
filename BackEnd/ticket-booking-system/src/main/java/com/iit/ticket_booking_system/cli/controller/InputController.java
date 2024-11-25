package com.iit.ticket_booking_system.cli.controller;

import com.iit.ticket_booking_system.cli.config.Configuration;

import java.util.Scanner;

public class InputController {

    public static void start(){
        Scanner scanner = new Scanner(System.in);
        int totalTickets = inputValidation(scanner, "Enter Total Number of Tickets: ", 0);
        int ticketReleaseRate = inputValidation(scanner, "Enter Ticket Release Rate: ", 0);
        int customerRetrievalRate = inputValidation(scanner, "Enter Customer Retrieval Rate: ", 0);
        int maxTicketCapacity = inputValidation(scanner, "Enter Maximum Ticket Capacity: ", 0);

        Configuration configuration = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        ConfigurationManager.saveConfiguration(configuration, "config.json");
        ConfigurationManager.saveConfigurationToTextFile(configuration, "config.txt");
    }

    public static int inputValidation(Scanner scanner, String prompt, int min){
        int output;
        while (true){
            System.out.println(prompt);
            if(scanner.hasNextInt()){
                output = scanner.nextInt();
                if( output > min){
                    break;
                }
                else{
                    System.out.println("Invalid Input. Value must be greater than 0");
                }
            }else{
                System.out.println("Invalid Input. Please enter an integer");
                scanner.next();
            }
        }
        return output;

    }
}
