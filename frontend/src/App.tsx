import * as React from 'react';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Link from '@mui/material/Link';
import ProTip from './ProTip';
import Button from '@mui/material/Button';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from "./pages/HomePage";
import ViewSlotsPage from "./pages/ViewSlotsPage";
import CreateAppointmentPage from "./pages/CreateAppointmentPage";
import MyAppointmentsPage from "./pages/MyAppointmentsPage";
import EditAppointmentPage from "./pages/EditAppointmentPage";
import { LoginForm } from "./pages/LoginForm";
import { AuthProvider } from './contexts/AuthContext';


function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/" element={<LoginForm />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/slots" element={<ViewSlotsPage />} />
          <Route path="/appointments/new" element={<CreateAppointmentPage />} />
          <Route path="/my-appointments" element={<MyAppointmentsPage />} />
          <Route path="/appointments/edit" element={<EditAppointmentPage />} />
          {/* Add a route for a not found page */}
          {/* <Route path="*" element={<NotFoundPage />} /> */}
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;