package com.iit.ticket_booking_system.thread;

import com.iit.ticket_booking_system.service.Vendor;

public class VendorThreadInfo {
    private Vendor vendor;
    private Thread vendorThread;

    public VendorThreadInfo(Vendor vendor, Thread vendorThread){
        this.vendor = vendor;
        this.vendorThread = vendorThread;
    }

    public Vendor getVendor(){
        return vendor;
    }


    public Thread getVendorThread(){
        return vendorThread;
    }


}
