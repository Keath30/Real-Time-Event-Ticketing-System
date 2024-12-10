package com.iit.ticket_booking_system.config;

import com.google.gson.Gson;
import com.iit.ticket_booking_system.util.LoggerUtil;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * ConfigurationManager is responsible for saving and loading configurations.
 * It supports saving configurations in both JSON and plain text formats.
 */

public class ConfigurationManager {

    private static final Gson gson = new Gson(); // Gson instance for serializing and deserializing objects

    /**
     * Saves the configuration object to a specified file in JSON format.
     *
     * @param configuration the configuration object to save
     * @param file          the file path where the configuration will be saved
     */

    public static void saveConfiguration(Configuration configuration, String file) {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(configuration, writer); // Convert the configuration object to JSON and write to file
            System.out.println("Configuration saved to: " + file);
            LoggerUtil.log("INFO", "Configuration saved to: " + file); // Log the save event

        } catch (IOException e) {
            String errorMessage = "Error saving configuration to file: " + file + ". " + e.getMessage();
            System.err.println(errorMessage); // Print the error message to stderr
            LoggerUtil.log("ERROR", errorMessage); // Log the error message
        }
    }

    /**
     * Saves the configuration to a text file in a human-readable format.
     *
     * @param config   the configuration object to save
     * @param textFile the file path where the configuration will be saved
     */

    public static void saveConfigurationToTextFile(Configuration config, String textFile) {
        // Prepare content to save in text format
        String content = "Maximum Ticket Capacity: " + config.getMaxTicketCapacity() + "\n";

        try {
            // Write the content to the specified text file
            Files.write(Paths.get(textFile), content.getBytes());
            System.out.println("Configuration saved to " + textFile);
            LoggerUtil.log("INFO", "Configuration saved to: " + textFile); //Log the save event
        } catch (IOException e) {
            // Handle errors during the file saving process
            String errorMessage = "Error saving configuration to text file: " + textFile + ". " + e.getMessage();
            System.err.println(errorMessage); // Print the error message to stderr
            LoggerUtil.log("ERROR", errorMessage); // Log the error message
        }
    }

    /**
     * Loads the configuration from a specified file in JSON format.
     *
     * @param file the file path to load the configuration from
     * @return the configuration object, or null if loading fails
     */

    public static Configuration loadConfiguration(String file) {
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, Configuration.class); // Deserialize the JSON content to a Configuration object

        } catch (IOException e) {
            // Handle errors during the file reading process
            String errorMessage = "Error loading configuration from file: " + file + ". " + e.getMessage();
            System.err.println(errorMessage); // Print the error message to stderr
            LoggerUtil.log("ERROR", errorMessage); // Log the error message
            return null; // Return null if there was an error
        }
    }

}
