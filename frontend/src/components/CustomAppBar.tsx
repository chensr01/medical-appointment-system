import React, { useContext } from 'react';
import { AppBar, Toolbar, Typography, IconButton, Button } from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../contexts/AuthContext';

interface CustomAppBarProps {
  title: string;
}

const CustomAppBar = ({ title }: CustomAppBarProps) => {
  const navigate = useNavigate();
  const { auth, logout } = useContext(AuthContext);

  const handleLogout = () => {
    logout(); // Clears the authentication context
    navigate('/'); // Redirects to the login page
  };

  return (
    <AppBar position="relative">
    <Toolbar>
      <IconButton onClick={() => navigate('/home')}>
        <HomeIcon fontSize='large' style={{ color: 'white' }}/>
      </IconButton>
      <Typography variant="h4" style={{ flexGrow: 1, marginLeft: '20px' }}>{title}</Typography>
      {auth && (
        <>
          <Typography variant="subtitle1" component="div" sx={{ marginRight: 2 }}
            onClick={() => {navigate('/my-appointments');}}
            style={{ cursor: 'pointer' }}>
            {auth.name} {/* Display the logged-in user's name */}
          </Typography>
          <Button color="inherit" onClick={handleLogout}>
            Log Out
          </Button>
        </>
      )}
    </Toolbar>
  </AppBar>
  );
};

export default CustomAppBar;
