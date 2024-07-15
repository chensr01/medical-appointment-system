import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';
import { CssBaseline, AppBar, Toolbar, Typography, Box, TextField, Button } from '@mui/material';

export const LoginForm: React.FC = () => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [error, setError] = useState('');
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    try {
        await login(name, email); // Assume login is an async function
        navigate('/home'); // Redirect to the home page after successful login
      } catch (error) {
        setError('Failed to login. Please check your email and try again.');
        // Handle errors (e.g., display error message)
      }
  };


  return (
    <>
      <CssBaseline />
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Medical Appointment System
          </Typography>
        </Toolbar>
      </AppBar>
      <Box
        component="form"
        onSubmit={handleSubmit}
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          height: 'calc(100vh - 64px)', // Adjust for AppBar height
          '& .MuiTextField-root': { m: 1, width: '25ch' },
          '& .MuiButton-root': { m: 1, width: '25ch' },
        }}
      >
        <Typography variant="h5" component="h1" gutterBottom sx={{ mb: 2 }}>
          Login
        </Typography>
        <TextField
          label="Name"
          variant="outlined"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
        <TextField
          label="Email"
          variant="outlined"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <Button variant="contained" color="primary" type="submit" sx={{ mt: 2, px: 5, py: 2 }}>
          Sign In
        </Button>
        {error && (
          <Typography color="error" sx={{ mt: 2 }}>
            {error}
          </Typography>
        )}
      </Box>
    </>
  );

  
};
