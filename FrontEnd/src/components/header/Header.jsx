import React, { useState, useEffect } from 'react';
import './Header.css';

const Header = ({ title }) => {
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
        console.error('Failed to fetch ticket status');
      }
    } catch (error) {
      console.error('Error fetching ticket status:', error);
    }
  };

  // Fetch system status from the backend
  const fetchSystemStatus = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/tickets/status');
      const data = await response.json();

      if (response.ok && data.status === 'success') {
        setSystemStatus(data.message); // Set system status message (Running/Stopped)
      } else {
        setSystemStatus('System not started'); //'System not started' if the backend fails
      }
    } catch (error) {
      console.error('Error fetching system status:', error);
      setSystemStatus('System not started');
    }
  };

  // Fetch ticket status and system status every 5 seconds
  useEffect(() => {
    fetchTicketStatus();
    fetchSystemStatus(); // Initial fetch of system status
    const intervalId = setInterval(() => {
      fetchTicketStatus();
      fetchSystemStatus(); // Refresh both every 5 seconds
    }, 5000);

    return () => clearInterval(intervalId);
  }, []);


  const getStatusClass = () => {
    if (systemStatus === 'Stopped') {
      return 'status-text stopped';
    }
    if (systemStatus === 'System not started') {
      return 'status-text not-started';
    }
    return 'status-text running';
  };

  return (
    <div className="header">
      <h1 className="header-title">{title}</h1>
      <div className="status-bar">
        <span>
          Status: <span className={getStatusClass()}>{systemStatus}</span> {/* Show system status */}
        </span>
        <span> | Vendors: {ticketStatus.activeVendors}</span>
        <span> | Customers: {ticketStatus.activeCustomers}</span>
      </div>
    </div>
  );
};

export default Header;
