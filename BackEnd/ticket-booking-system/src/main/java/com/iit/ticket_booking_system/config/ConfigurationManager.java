package com.iit.ticket_booking_system.config;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigurationManager {

    private static final Gson gson = new Gson();

    public static void saveConfiguration(Configuration configuration, String file){
        try(FileWriter writer = new FileWriter(file)){
            gson.toJson(configuration, writer);
            System.out.println("Configuration saved to: " + file);

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void saveConfigurationToTextFile(Configuration config, String textFile) {
        String content = "Maximum Ticket Capacity: " + config.getMaxTicketCapacity() + "\n";

        try {
            Files.write(Paths.get(textFile), content.getBytes());
            System.out.println("Configuration saved to " + textFile);
        } catch (IOException e) {
            System.err.println("Error saving configuration to text file: " + e.getMessage());
        }
    }

    public static Configuration loadConfiguration(String file){
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, Configuration.class);

        }catch (IOException e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

}
