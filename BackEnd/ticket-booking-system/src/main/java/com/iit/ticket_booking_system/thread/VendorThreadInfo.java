package com.iit.ticket_booking_system.thread;

/**
 * Encapsulates information about a vendor and its associated thread.
 * This class is used to manage and track vendor threads in the ticket booking system.
 */
public class VendorThreadInfo {
    private final Vendor vendor; // Reference to the vendor associated with the thread
    private final Thread vendorThread; // The thread executing the vendor's actions

    /**
     * Constructs a new instance of {@code VendorThreadInfo}.
     *
     * @param vendor       The vendor object representing the vendor.
     * @param vendorThread The thread handling the vendor's actions.
     */
    public VendorThreadInfo(Vendor vendor, Thread vendorThread) {
        this.vendor = vendor;
        this.vendorThread = vendorThread;
    }

    /**
     * Gets the vendor associated with this thread.
     *
     * @return The {@link Vendor} object.
     */
    public Vendor getVendor() {
        return vendor;
    }

    /**
     * Gets the thread managing the vendor's actions.
     *
     * @return The {@link Thread} object.
     */
    public Thread getVendorThread() {
        return vendorThread;
    }


}
