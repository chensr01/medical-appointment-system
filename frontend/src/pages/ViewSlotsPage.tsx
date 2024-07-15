import React, { useEffect, useState, useContext } from 'react';
import { useLocation } from 'react-router-dom';
import { Grid, Card, CardContent, Typography, CardActionArea } from '@mui/material';
import axios from 'axios';
import { Slot } from "../models/appointment";
import { AuthContext } from '../contexts/AuthContext';
import CustomAppBar from '../components/CustomAppBar';
import axiosInstance from '../axiosConfig/axiosConfig'

function ViewSlotsPage() {
  const [slots, setSlots] = useState([] as Slot[]);
  const { auth } = useContext(AuthContext);
  const location = useLocation();
  const appointment = location.state;

  // Get slots data from backend
  useEffect(() => {
    axiosInstance.get('/appointment/timeslots/all')
      .then(response => {
        setSlots(response.data);
      })
      .catch(error => console.error('There was an error!', error));
  }, []);

  // Navigate to CreateSlotPage when click the card
  const handleCardClick = (slot: Slot) => {
    if (appointment) {
      // Update appointment
      axiosInstance.post('/appointment/modify', {
        id: appointment.id,
        timeSlotId: slot.id,
      })
        .then(() => {
          alert('Slot Updated Successfully!');
          // If the booked was successful, filter out the booked slot
          setSlots(slots.filter((currSlot) => currSlot.id !== slot.id));
        })
        .catch(error => console.error('There was an error!', error));     

    } else {
      // Add new appointment
      axiosInstance.post('/appointment', {
        patientId: auth?.patientId,
        timeSlotId: slot.id,
      })
        .then(() => {
          alert('Slot Booked Successfully!');
          // If the booked was successful, filter out the booked slot
          setSlots(slots.filter((currSlot) => currSlot.id !== slot.id));
        })
        .catch(error => console.error('There was an error!', error));
    }
  };

  return (
    <>
      <CustomAppBar title = 'View Availabe Slots'/>

      {/* Display all available slots in grid layout */}
      <Grid container spacing={2} style={{ marginTop: '10px' }}>
        {slots.length > 0 ? (
          slots.map((slot) => (
            <Grid item xs={12} sm={6} md={4} lg={3} key={slot.id}>
              <Card variant="outlined" onClick={() => handleCardClick(slot)}>
                <CardActionArea>
                  <CardContent>
                    <Typography variant="subtitle1">
                      Time: {slot.date} {slot.time}
                    </Typography>
                    <Typography variant="subtitle1">
                      Doctor: {slot.doctor}
                    </Typography>
                    <Typography variant="subtitle1">
                        Location: {slot.location}
                    </Typography>
                    <Typography variant="subtitle1">
                        Type: {slot.type}
                    </Typography>
                  </CardContent>
                </CardActionArea>
              </Card>
            </Grid>
          ))
        ) : (
          <Grid item xs={12}>
            <Typography variant='h6'>No available slots</Typography>
          </Grid>
        )}
      </Grid>
    </>
  );
}

export default ViewSlotsPage;
