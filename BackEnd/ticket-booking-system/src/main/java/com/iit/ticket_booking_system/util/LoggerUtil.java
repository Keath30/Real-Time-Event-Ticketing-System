package com.iit.ticket_booking_system.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class LoggerUtil {

    /**
     * Utility class for managing logging functionality in the ticket booking system.
     * Provides methods for logging messages to a file and maintaining an in-memory log list.
     */
    private static final String LOG_FILE = "ticket_booking_system.log"; // Log file name
    private static final List<String> logArray = new ArrayList<>(); // In-memory list of log messages

    private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName()); // Logger instance

    // Static block to configure the logger with a file handler and formatter
    static {

        try {
            logger.setUseParentHandlers(false);  // Disable default console logging
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);  // Append mode
            fileHandler.setLevel(Level.ALL); // Log all levels
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);  // Attach the file handler to the logger


        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger file handler", e);
        }
    }

    /**
     * Logs a message with a specified level and adds it to the in-memory log list.
     *
     * @param level   The severity level of the log (INFO, WARNING, ERROR).
     * @param message The log message to record.
     */
    public static synchronized void log(String level, String message) {
        String logMessage = level + " : " + message; // Format log message
        if ("INFO".equals(level)) {
            logger.info(message);
        } else if ("WARNING".equals(level)) {
            logger.warning(message);

        } else if ("ERROR".equals(level)) {
            logger.severe(message);
        }
        logArray.add(logMessage); // Add the log to the in-memory list
    }

    /**
     * Retrieves the in-memory list of logged messages.
     *
     * @return A copy of the list containing logged messages.
     */
    public static List<String> getLogs() {
        return new ArrayList<>(logArray);
    }

    /**
     * Clears all logged messages from the in-memory list.
     */
    public static void clearLogs() {
        logArray.clear();
        ;
    }


}

