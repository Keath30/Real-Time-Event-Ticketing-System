package com.iit.ticket_booking_system;

import com.iit.ticket_booking_system.config.Configuration;
import com.iit.ticket_booking_system.config.ConfigurationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class TicketBookingSystemApplication {

	public static void main(String[] args) {
		System.out.println("""
                ***************************
                WELCOME TO TICKET BOOKING APPLICATION
                ***************************
                """);
		System.out.println("\nEnter\n1 - Load configuration from file\n2 - Enter new configuration");

		Scanner scanner = new Scanner(System.in);
		int choice = scanner.nextInt();

		if(choice == 1){
			Configuration config = ConfigurationManager.loadConfiguration("config.json");
			if(config == null){
				System.out.println("Failed to load configuration");
			}else{
				System.out.println("Configuration loaded successfully");
			}

		}else{
			Configuration newConfig = new Configuration();
			System.out.println("Enter Maximum Ticket Capacity: ");
			newConfig.setMaxTicketCapacity(scanner.nextInt());

			ConfigurationManager.saveConfiguration(newConfig, "config.json");
			ConfigurationManager.saveConfigurationToTextFile(newConfig, "config.txt");
			System.out.println("New configuration saved!");



		}
		 SpringApplication.run(TicketBookingSystemApplication.class, args);

	}

}
