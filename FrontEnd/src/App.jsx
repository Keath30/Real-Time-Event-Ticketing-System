import './App.css'
import Header from './components/header/header'
import ControlPanel from './components/ControlPanel/ControlPanel'
import TicketDashboard from './components/ticketDashboard/TicketDasboard'
import AnalyticsDashboard from './components/analyticsDashboard/AnalyticsDashboard'
import LogDisplay from './components/LogDisplay/LogDisplay'

function App() {
  return (
    <div className="app-container">
      <Header
        title="Ticket Booking System"
      />
      <div className="main-content">
        <ControlPanel />
        <TicketDashboard />
        <AnalyticsDashboard></AnalyticsDashboard>

      </div>
      <LogDisplay />


    </div>
  );
}

export default App
