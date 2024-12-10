package com.melissa.iit.cli.config;


import com.melissa.iit.cli.util.LoggerUtil;

import java.io.*;

/**
 * ConfigurationManager is responsible for saving and loading configurations.
 * It supports saving configurations in both JSON and plain text formats.
 */
public class ConfigurationManager {

    /**
     * Saves the given configuration to a text file in a human-readable format.
     * This method serializes the configuration object and writes it to the specified file.
     *
     * @param config   The configuration object to save.
     * @param textFile The file path where the configuration will be saved.
     */
    public static void saveConfigurationToTextFile(Configuration config, String textFile) {

        if(config==null || textFile==null || textFile.isEmpty()){
            String errorMessage = "Invalid configuration or file";
            System.err.println(errorMessage);
            LoggerUtil.log("ERROR",errorMessage);
            return;
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(textFile))){
            oos.writeObject(config); // Serialize the configuration object and save to file
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
     * Loads the configuration from the specified file.
     * This method reads the configuration object from a serialized file.
     *
     * @param file The file path to load the configuration from.
     * @return The configuration object, or null if loading fails.
     */
    public static Configuration loadConfiguration(String file) {

        //validate the file path
        if(file == null || file.isEmpty()){
            String errorMessage = "Invalid file";
            System.err.println(errorMessage);
            LoggerUtil.log("ERROR",errorMessage);
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Configuration) ois.readObject(); // Deserialize the configuration object from file

        } catch (IOException | ClassNotFoundException e) {
            // Handle errors during the file reading process
            String errorMessage = "Error configuration from file: " + file + ". " + e.getMessage();
            System.err.println(errorMessage); // Print the error message to stderr
            LoggerUtil.log("ERROR", errorMessage); // Log the error message
            return null; // Return null if there was an error
        }
    }

}
