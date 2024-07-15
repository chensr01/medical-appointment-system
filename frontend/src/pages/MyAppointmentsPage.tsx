import React, { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { Typography, Grid, Card, CardContent, CardActions, IconButton } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { AuthContext } from '../contexts/AuthContext';
import CustomAppBar from '../components/CustomAppBar';
import axiosInstance from '../axiosConfig/axiosConfig'
import {Appointment} from "../models/appointment";

function MyAppointmentsPage() {
  const [appointments, setAppointments] = useState([] as Appointment[]);
  const navigate = useNavigate();
  const { auth } = useContext(AuthContext);

  // Get appointments data from backend
  useEffect(() => {
    axiosInstance.get('/appointment', {
      params: {
        patientId: auth?.patientId
      }
    })
        .then(response => {
          setAppointments(response.data);
        })
        .catch(error => console.error('Error occurred when getting appointments!', error));
  }, []);

  // Navigate to EditAppointmentPage when clicking the edit button
  const handleEditClick = (appointment: Appointment) => {
    navigate('/slots', { state: { ...appointment } });
  };
  
  // Delete the appointment record when clicking the delete button
  const handleDeleteClick = (appointmentId: string) => {
    // Send a DELETE request to the backend
    axiosInstance.delete('/appointment', {
      params: {
        appointmentId: appointmentId
      }
    })
      .then((response) => {
        // If the delete was successful, filter out the deleted appointment
        setAppointments(appointments.filter((appointment) => appointment.id !== appointmentId));
      })
      .catch((error) => {
        // Handle any errors here
        console.error('There was an error deleting the appointment!', error);
      });
  };

  return (
    <>
      <CustomAppBar title='My Appointments'/>

      <Grid container spacing={3} style={{ padding: '30px' }}>
        {appointments.length > 0 ? (
          appointments.map((appointment) => (
            <Grid item xs={12} sm={6} md={4} lg={12} key={appointment.id}>
              <Card variant="outlined">
                {/* Appointment details */}
                <CardContent>
                  <Typography variant="subtitle1">
                    Time: {appointment.date}  {appointment.time}
                  </Typography>
                  <Typography variant="subtitle1">
                    Doctor: {appointment.doctor}
                  </Typography>
                  <Typography variant="subtitle1">
                    Location: {appointment.location}
                  </Typography>
                  <Typography variant="subtitle1">
                    Type: {appointment.type}
                  </Typography>
                  <Typography variant="subtitle1">
                    Test Result: {appointment.testResult}
                  </Typography>
                  <Typography variant="subtitle1">
                    Quarantine Result: {appointment.quarantineResult}
                  </Typography>
                </CardContent>

                {/* Edit & Delete buttons */}
                <CardActions disableSpacing sx={{ justifyContent: 'flex-end'}}>
                  <IconButton 
                    aria-label="edit" 
                    style={{ marginRight: '30px'}}
                    onClick={() => handleEditClick(appointment)}
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton 
                    aria-label="delete" 
                    style={{ marginRight: '20px'}}
                    onClick={() => handleDeleteClick(appointment.id)}
                  >
                    <DeleteIcon />
                  </IconButton>
                </CardActions>
              </Card>
            </Grid>
          ))
        ) : (
          <Grid item xs={12}>
            <Typography variant='h6'>No appointment records</Typography>
          </Grid>
        )}
      </Grid>
    </>
  );
}

export default MyAppointmentsPage;
