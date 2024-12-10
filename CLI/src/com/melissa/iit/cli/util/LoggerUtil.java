package com.melissa.iit.cli.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class LoggerUtil {

    /**
     * Utility class for managing logging functionality in the ticket booking system.
     * Provides methods for logging messages to a file and maintaining an in-memory log list.
     * <p>
     * This class is responsible for logging messages with various severity levels (INFO, WARNING, ERROR).
     * It supports logging to both the console and a file, with an in-memory log list for real-time log tracking.
     * </p>
     */
    private static final String LOG_FILE = "ticket_booking_system.log"; // Log file name
    private static final List<String> logArray = new ArrayList<>(); // In-memory list of log messages

    private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName()); // Logger instance

    // Static block to configure the logger with a file handler and formatter
    static {

        try {
            logger.setUseParentHandlers(false);  // Disable default console logging


            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setLevel(Level.ALL); // Log all levels
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);  // Attach the file handler to the logger


        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger file handler", e);
        }
    }

    /**
     * Logs a message with a specified severity level and adds it to the in-memory log list.
     * <p>
     * This method ensures that the log message is both recorded in a file and added to the in-memory list for real-time tracking.
     * </p>
     *
     * @param level   The severity level of the log (INFO, WARNING, ERROR).
     * @param message The log message to record.
     */
    public static synchronized void log(String level, String message) {
        String logMessage = level + " : " + message; // Format log message

        if ("INFO".equals(level)) {
            logger.info(message);
        }
        else if ("WARNING".equals(level)) {
            logger.warning(message);

        }
        else if ("ERROR".equals(level)) {
            logger.severe(message);
        }
        logArray.add(logMessage); // Add the log to the in-memory list
    }



}

