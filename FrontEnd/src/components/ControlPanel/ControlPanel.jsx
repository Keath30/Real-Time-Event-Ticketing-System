import React, { useState } from 'react';
import './ControlPanel.css';

const ControlPanel = () => {
    const [showMaxCapacityForm, setShowMaxCapacityForm] = useState(false);
    const [showVendorForm, setShowVendorForm] = useState(false);
    const [showCustomerForm, setShowCustomerForm] = useState(false);
    const [showRemoveVendorForm, setShowRemoveVendorForm] = useState(false);
    const [showRemoveCustomerForm, setShowRemoveCustomerForm] = useState(false);
    const [maxCapacity, setMaxCapacity] = useState('');

    // State for vendor form inputs
    const [vendorName, setVendorName] = useState('');
    const [eventTitle, setEventTitle] = useState('');
    const [ticketsPerRelease, setTicketsPerRelease] = useState('');
    const [releaseInterval, setReleaseInterval] = useState('');
    const [totalTickets, setTotalTickets] = useState('');
    const [price, setPrice] = useState('');

    //state for customer form inputs
    const [customerName, setCustomerName] = useState('');
    const [retrievalInterval, setRetrievalInterval] = useState('');
    const [quantity, setQuantity] = useState('');

    const [message, setMessage] = useState('System not started');

    const handleMaxCapacitySubmit = async (event) => {
        event.preventDefault(); // Prevent form submission from refreshing the page


        const Data = {
            maxCapacity: maxCapacity,
        };

        const errorMessage = validateForm(Data); // Using the vendorData object to validate inputs
        if (errorMessage) {
            alert(errorMessage);  // Show error message if validation fails
            return;  // Stop form submission if validation fails
        }

        if (maxCapacity < 0 || maxCapacity > 100) {
            alert('Please enter a valid max capacity between 0 and 100.');
            return;
        }

        try {
            const queryParams = `maxCapacity=${maxCapacity}`;
            const response = await fetch(`http://localhost:8080/api/tickets/start?${queryParams}`, {
                method: 'POST',
            });


            if (response.ok) {
                alert("Maximum Capacity set to " + maxCapacity);
                const data = await response.json();
                setMessage(data.message || 'System started successfully with max capacity.');
                setShowMaxCapacityForm(false); // Hide the form after success
            } else {
                const errorData = await response.json();
                alert('Error starting the system: ' + (errorData.message || 'Unknown error.'));
            }
        } catch (error) {
            console.error('Error starting the system:', error);
            alert('Failed to start the system.');
        }
    };

    // Method to start the system with default capacity (0)
    const handleStartWithDefault = async () => {
        try {
            // Pass default maxCapacity (0) as a query parameter
            const response = await fetch('http://localhost:8080/api/tickets/start?maxCapacity=0', {
                method: 'POST',
            });

            if (response.ok) {
                const data = await response.json();
                setMessage(data.message || 'System started successfully with default capacity.');
                setShowMaxCapacityForm(false); // Hide the form after success
            } else {
                const errorData = await response.json();
                alert('Error starting the system: ' + (errorData.message || 'Unknown error.'));
            }
        } catch (error) {
            console.error('Error starting the system with default capacity:', error);
            alert('Failed to start the system with default capacity.');
        }
    };

    const handleMaxCapacityClick = async () => {
        setShowMaxCapacityForm(true); // Show form to add a vendor
    };

    const handleAddVendorClick = async () => {
        setShowVendorForm(true); // Show form to add a vendor
    };

    const handleAddCustomerClick = async () => {
        setShowCustomerForm(true); // Show form to add a customer
    };

    const handleRemoveVendorClick = async () => {
        setShowRemoveVendorForm(true); // Show form to remove a vendor
    };

    const handleRemoveCustomerClick = async () => {
        setShowRemoveCustomerForm(true); // Show form to remove a customer
    };



    const handleStopClick = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/tickets/stop', {
                method: 'POST',
            });
            if (response.ok) {
                console.log('System stopped');
                setMessage('System stopped successfully');
            } else {
                console.error('Failed to stop the system');
            }
        } catch (error) {
            console.error('Error stopping the system', error);
        }
    };

    const handleVendorSubmit = async (event) => {
        event.preventDefault();

        const vendorData = {
            name: vendorName,
            eventName: eventTitle,
            ticketsPerRelease: ticketsPerRelease,
            releaseInterval: releaseInterval,
            totalTickets: totalTickets,
            price: price,
        };

        const errorMessage = validateForm(vendorData); // Using the vendorData object to validate inputs
        if (errorMessage) {
            alert(errorMessage);  // Show error message if validation fails
            return;  // Stop form submission if validation fails
        }

        const queryParams = new URLSearchParams(vendorData).toString();
        console.log('Sending request with params:', queryParams);  // Debugging log

        try {
            const response = await fetch(`http://localhost:8080/api/tickets/vendor/add?${queryParams}`, {
                method: 'POST',
            });

            const data = await response.json();

            if (response.ok) {
                alert('Vendor added successfully');
                setMessage(data.message);
                setShowVendorForm(false);
            } else {
                alert('Error: ' + data.message);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error occurred while adding vendor');
        }
    };

    const handleCustomerSubmit = async (event) => {
        event.preventDefault();


        const customerData = {
            name: customerName,
            retrievalInterval: retrievalInterval,
            totalTickets: quantity,
        };

        const queryParams = new URLSearchParams(customerData).toString();

        try {
            const response = await fetch(`http://localhost:8080/api/tickets/customer/add?${queryParams}`, {
                method: 'POST',
            });

            const data = await response.json();

            if (response.ok) {
                alert('Customer added successfully');
                setMessage(data.message);
                setShowCustomerForm(false); // Hide form after success
            } else {
                alert('Error: ' + data.message);
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error occurred while adding customer');
        }
    };

    const validateForm = (inputs) => {
        for (const key in inputs) {
            if (!inputs[key] || inputs[key] <= 0) {
                return `Please enter a valid value for ${key}`;
            }
        }
        return null; // Returns null if everything is valid
    };


    const handleRemoveVendorSubmit = async (event) => {
        event.preventDefault();

        const vendorName = event.target.vendorName.value;

        if (!vendorName) {
            console.error('Vendor name is required');
            return;
        }

        try {
            const queryParams = new URLSearchParams({ name: vendorName }).toString();

            const response = await fetch(`http://localhost:8080/api/tickets/vendor/remove?${queryParams}`, {
                method: 'DELETE',
            });

            const data = await response.json();

            if (response.ok) {
                console.log('Vendor removed:', data.message);
                setMessage(data.message);
                setShowRemoveVendorForm(false); // Hide the form after success
            } else {
                console.error('Failed to remove vendor:', data.message);
            }
        } catch (error) {
            console.error('Error removing vendor', error);
        }
    };

    const handleRemoveCustomerSubmit = async (event) => {
        event.preventDefault();

        const customerName = event.target.customerName.value;

        if (!customerName) {
            console.error('Customer name is required');
            return;
        }

        try {
            const queryParams = new URLSearchParams({ name: customerName }).toString();

            const response = await fetch(`http://localhost:8080/api/tickets/customer/remove?${queryParams}`, {
                method: 'DELETE',
            });

            const data = await response.json();

            if (response.ok) {
                console.log('Customer removed:', data.message);
                setMessage(data.message);
                setShowRemoveCustomerForm(false);
            } else {
                console.error('Failed to remove customer:', data.message);
            }
        } catch (error) {
            console.error('Error removing customer', error);
        }
    };

    return (
        <div className="control-panel">


            <div className="action-buttons">
                <button className="start-btn" onClick={handleMaxCapacityClick}>Start</button>
                <button onClick={handleAddVendorClick}>Add Vendor</button>
                <button onClick={handleAddCustomerClick}>Add Customer</button>
                <button onClick={handleRemoveVendorClick}>Remove Vendor</button>
                <button onClick={handleRemoveCustomerClick}>Remove Customer</button>
                <button className="stop-btn" onClick={handleStopClick}>Stop</button>


                {/* Max Capacity Form */}
                {showMaxCapacityForm && (
                    <div className="form-container">
                        <div className="form">
                            <form onSubmit={handleMaxCapacitySubmit}>
                                <label>Enter Max Ticket Capacity:</label>
                                <input
                                    type="number"
                                    value={maxCapacity}
                                    onChange={(e) => setMaxCapacity(e.target.value)}
                                />
                                <div className="form-buttons">
                                    <button type="submit" className="set-button">
                                        Set Capacity
                                    </button>
                                    <button
                                        type="button"
                                        className="default-button"
                                        onClick={handleStartWithDefault}
                                    >
                                        Use Default
                                    </button>
                                    <button
                                        type="button"
                                        className="cancel-button"
                                        onClick={() => setShowMaxCapacityForm(false)}
                                    >
                                        Cancel
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}

                {/* Vendor Form */}
                {showVendorForm && (
                    <div className="form-container">
                        <div className="form">
                            <form onSubmit={handleVendorSubmit}>
                                <label>Enter Name:</label>
                                <input
                                    type="text"
                                    name="vendorName"
                                    value={vendorName}
                                    onChange={(e) => setVendorName(e.target.value)} // Update vendorName state
                                />
                                <label>Enter Event Name:</label>
                                <input
                                    type="text"
                                    name="eventTitle"
                                    value={eventTitle}
                                    onChange={(e) => setEventTitle(e.target.value)} // Update eventTitle state
                                />
                                <label>Enter Tickets Per Release:</label>
                                <input
                                    type="number"
                                    name="ticketsPerRelease"
                                    value={ticketsPerRelease}
                                    onChange={(e) => setTicketsPerRelease(e.target.value)} // Update ticketsPerRelease state
                                />
                                <label>Enter Release Interval:</label>
                                <input
                                    type="number"
                                    name="releaseInterval"
                                    value={releaseInterval}
                                    onChange={(e) => setReleaseInterval(e.target.value)} // Update releaseInterval state
                                />
                                <label>Enter Quantity:</label>
                                <input
                                    type="number"
                                    name="quantity"
                                    value={totalTickets}
                                    onChange={(e) => setTotalTickets(e.target.value)} // Update totalTickets state
                                />
                                <label>Enter Price:</label>
                                <input
                                    type="number"
                                    name="price"
                                    value={price}
                                    onChange={(e) => setPrice(e.target.value)} // Update price state
                                />
                                <div className="form-buttons">
                                    <button type="submit" className="add-button">Add</button>
                                    <button type="button" className="cancel-button" onClick={() => setShowVendorForm(false)}>Cancel</button>
                                </div>
                            </form>
                        </div>

                    </div>
                )}

                {/* Customer Form */}
                {showCustomerForm && (
                    <div className="form-container">
                        <div className="form">
                            <form onSubmit={handleCustomerSubmit}>
                                <label>Enter Name:</label>
                                <input
                                    type="text"
                                    name="name"
                                    value={customerName}
                                    onChange={(e) => setCustomerName(e.target.value)}
                                />

                                <label>Enter Retrieval Interval:</label>
                                <input
                                    type="number"
                                    name="retrievalInterval"
                                    value={retrievalInterval}
                                    onChange={(e) => setRetrievalInterval(e.target.value)}
                                />
                                <label>Enter Quantity:</label>
                                <input
                                    type="number"
                                    name="quantity"
                                    value={quantity}
                                    onChange={(e) => setQuantity(e.target.value)}
                                />
                                <div className="form-buttons">
                                    <button type="submit" className="add-button">Add</button>
                                    <button type="button" className="cancel-button" onClick={() => setShowCustomerForm(false)}>Cancel</button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}

                {/* Remove Vendor Form */}
                {showRemoveVendorForm && (
                    <div className="form-container">
                        <div className="form">
                            <form onSubmit={handleRemoveVendorSubmit}>
                                <label>Enter Vendor Name to Remove:</label>
                                <input type="text" name="vendorName" />
                                <div className="form-buttons">
                                    <button type="submit" className="remove-button">Remove</button>
                                    <button type="button" className="cancel-button" onClick={() => setShowRemoveVendorForm(false)}>Cancel</button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}

                {/* Remove Customer Form */}
                {showRemoveCustomerForm && (
                    <div className="form-container">
                        <div className="form">
                            <form onSubmit={handleRemoveCustomerSubmit}>
                                <label>Enter Customer Name to Remove:</label>
                                <input type="text" name="customerName" />
                                <div className="form-buttons">
                                    <button type="submit" className="remove-button">Remove</button>
                                    <button type="button" className="cancel-button" onClick={() => setShowRemoveCustomerForm(false)}>Cancel</button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}
            </div>
            <div className="message">
                <pre>{` ${message}`}</pre>
            </div>
        </div>
    );
};

export default ControlPanel;