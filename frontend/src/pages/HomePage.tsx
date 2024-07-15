import React from 'react';
import { Link } from 'react-router-dom';
import { Typography, Button, Box, CssBaseline } from '@mui/material'
import CustomAppBar from '../components/CustomAppBar';

const HomePage = () => {

  return (
    <>
      <CssBaseline />
      <CustomAppBar title = ''/>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          height: 'calc(100vh - 64px)', // Adjust for AppBar height
        }}
      >
      {/* Heading */}
      <Typography variant="h3" component="h1" gutterBottom sx={{ mt: -14 }}>
        Welcome to the
      </Typography>
      <Typography variant="h3" component="h1" gutterBottom sx={{ marginBottom: 10 }}>
        Medical Appointment System
      </Typography>

      {/* Buttons for redirection */}
      <Box>
        <Button component={Link} to="/slots" variant="contained" color="primary" size="large" sx={{ m: 2, px: 5, py: 2 }}>
          Make Appointments
        </Button>
        <Button component={Link} to="/my-appointments" variant="contained" color="secondary" size="large" sx={{ m: 2, px: 5, py: 2 }}>
          My Appointments
        </Button>
      </Box>

    </Box>
    </>
  );
};

export default HomePage;
