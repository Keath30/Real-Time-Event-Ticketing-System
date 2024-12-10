import React, { useState, useEffect } from 'react';
import './TicketDashboard.css';

const TicketDashboard = () => {
  const [ticketStatus, setTicketStatus] = useState({
    currentSize: 0,
    totalTicketsAdded: 0,
    maxCapacity: 0,
    activeVendors: 0,
    activeCustomers: 0,
  });

  const [systemStatus, setSystemStatus] = useState('System not started'); // Default system status

  // Fetch ticket status from the backend
  const fetchTicketStatus = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/tickets/tickets');
      const data = await response.json();

      if (response.ok && data.status === 'success') {
        setTicketStatus(data.ticketStatus); // Update ticket status data
      } else {
        console.error(data.message || 'Failed to fetch ticket status');
        setSystemStatus('System not started');
      }
    } catch (error) {
      console.error('Error fetching ticket status:', error);
      setSystemStatus('System not started');
    }
  };


  // Fetch ticket status and system status every 5 seconds
  useEffect(() => {
    fetchTicketStatus();
    const intervalId = setInterval(() => {
      fetchTicketStatus();
    }, 5000);

    return () => clearInterval(intervalId);
  }, []);


  return (
    <div className="dashboard">
      <h2 className="dashboard-header">Ticket Availability</h2>

      <div className="availability-summary">
        <div className="summary-item">
          <p>Total Tickets:</p>
          <p>{ticketStatus.totalTicketsAdded}</p>
        </div>
        <div className="summary-item">
          <p>Current Tickets:</p>
          <p>{ticketStatus.currentSize}</p>
        </div>
        <div className="summary-item">
          <p>Max Capacity:</p>
          <p>{ticketStatus.maxCapacity}</p>
        </div>
        <div className="summary-item">
          <p>Active Vendors:</p>
          <p>{ticketStatus.activeVendors}</p>
        </div>
        <div className="summary-item">
          <p>Active Customers:</p>
          <p>{ticketStatus.activeCustomers}</p>
        </div>
      </div>
    </div>
  );
};

export default TicketDashboard;
