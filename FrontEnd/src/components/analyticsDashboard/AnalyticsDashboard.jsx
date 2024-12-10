import React, { PureComponent } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

export default class AnalyticsDashboard extends PureComponent {
  constructor(props) {

    // Initializing state to store sales data and error status
    super(props);
    this.state = {
      salesData: [], // Initial state for sales data
      error: false, // State to track errors
    };
  }

  componentDidMount() {
    this.fetchSalesData();
    this.interval = setInterval(this.fetchSalesData, 5000); // Fetch data every 5 seconds
  }

  componentWillUnmount() {
    clearInterval(this.interval); // Clear interval on component unmount
  }

  // Fetch sales data from backend and append timestamp
  fetchSalesData = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/tickets/sales');
      if (response.ok) {
        const data = await response.json();
        const currentTime = new Date().toLocaleTimeString(); // Get current time in HH:MM:SS format

        // Update the graph with new data and reset the error state
        this.setState((prevState) => ({
          salesData: [
            ...prevState.salesData,
            { time: currentTime, sales: data.ticketSales },
          ],
          error: false,
        }));
      } else {
        console.error('HTTP error while fetching sales data');
        this.handleError();
      }
    } catch (error) {
      console.error('Error fetching sales data:', error);
      this.handleError();
    }
  };

  // Handle errors by resetting the graph
  handleError = () => {
    this.setState({
      salesData: [], // Clear sales data
      error: true, // Set error state
    });
  };

  render() {
    const { salesData, error } = this.state;

    return (
      <div style={{ width: '100%', height: '400px' }}>
        <h2>Sales Over Time</h2>
        {error && <p style={{ color: 'red' }}>Failed to fetch sales data. Please check the backend service.</p>}
        <ResponsiveContainer width="100%" height="80%">
          <LineChart
            width={500}
            height={300}
            data={salesData}
            margin={{
              top: 5,
              right: 30,
              left: 20,
              bottom: 5,
            }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="time" label={{ value: 'Time', position: 'insideBottomRight', offset: -5 }} />
            <YAxis label={{ value: 'Sales', angle: -90, position: 'insideLeft' }}
            tickFormatter={(value) => `$${value}`} />
            <Tooltip />
            <Legend />
            <Line type="monotone" dataKey="sales" stroke="#8884d8" activeDot={{ r: 8 }} />
          </LineChart>
        </ResponsiveContainer>
      </div>
    );
  }
}
