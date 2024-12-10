import React, { useEffect, useState } from "react";
import "./LogDisplay.css";

const LogDisplay = () => {
  const [logs, setLogs] = useState([]); // State to store logs
  const [error, setError] = useState(""); // State to store errors

  useEffect(() => {
    const fetchLogs = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/tickets/logs");


        if (!response.ok) {
          throw new Error("Failed to fetch logs from server");
        }

        const data = await response.json();
        console.log(data);

        if (data.status === "success") {
          setLogs(data.logs); // Update logs state
        } else {
          setError(data.message || "Failed to fetch logs");
        }
      } catch (err) {
        console.error(err);
        console.error("Error fetching logs: ", err);
        setError("Error fetching logs");
      }
    };

    fetchLogs(); // Call the function to fetch logs

    const interval = setInterval(fetchLogs, 10000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="container">
      <h2>System Logs</h2>
      {error && <p className="error">{error}</p>} {/* Display error if exists */}
      <div className="log-container">
        {logs.length > 0 ? (
          logs.map((log, index) => (
            <p key={index} className="log">
              {log}
            </p>
          ))
        ) : (
          <p>No logs available</p> // Display if no logs are available
        )}
      </div>
    </div>
  );
};

export default LogDisplay;
