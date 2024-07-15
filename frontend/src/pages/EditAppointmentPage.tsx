import React, { useState} from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Button, Typography, Paper, Container, Grid, Select, MenuItem, FormControl, InputLabel, TextField} from '@mui/material';
import axios from 'axios';


function EditAppointmentPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const slotInfo = location.state || {};

  const [form, setForm] = useState({
    name: '',
    phone: '',
    email: '',
    comments: '',
    date: '', // Assuming date is part of the form now
    time: '', // Assuming time is part of the form now
    doctor: '', // Assuming doctor is part of the form now
    location: '', // Assuming location is part of the form now
  });

  // TODO: using any is a bad practice, replace it with a proper type
  const handleInputChange = (event: any) => {
    const { name, value } = event.target;
    setForm({ ...form, [name]: value });
  };

  // TODO: using any is a bad practice, replace it with a proper type
  const handleSubmit = (event: any) => {
    event.preventDefault();
    const appointmentDetails = {
      ...slotInfo,
      ...form,
    };
    axios.post('http://localhost:5000/appointments', appointmentDetails)
      .then(() => alert('Appointment saved!'))
      .catch(error => console.error('There was an error!', error));
  };

  // Example data for select options
  const dates = ["2024-02-24", "2024-02-25"]; // Example dates
  const times = ["10:00", "11:00"]; // Example times
  const doctors = ["Dr. Smith", "Dr. Jones"]; // Example doctors
  const locations = ["Room 101", "Room 102"]; // Example locations

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} style={{ padding: '16px', margin: '16px 0' }}>
        <Typography variant="h4" align="center" gutterBottom>
          Edit Appointment
        </Typography>
        
        <form onSubmit={handleSubmit}>
          <Grid container spacing={2} alignItems="center" justifyContent="space-between">
            {/* Select for Date */}
            <Grid item xs={6}>
              <FormControl fullWidth margin="normal">
                <InputLabel>Date</InputLabel>
                <Select
                  label="Date"
                  name="date"
                  value={form.date}
                  onChange={handleInputChange}
                >
                  {dates.map((date, index) => (
                    <MenuItem key={index} value={date}>{date}</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            {/* Select for Time */}
            <Grid item xs={6}>
              <FormControl fullWidth margin="normal">
                <InputLabel>Time</InputLabel>
                <Select
                  label="Time"
                  name="time"
                  value={form.time}
                  onChange={handleInputChange}
                >
                  {times.map((time, index) => (
                    <MenuItem key={index} value={time}>{time}</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            {/* Select for Doctor */}
            <Grid item xs={6}>
              <FormControl fullWidth margin="normal">
                <InputLabel>Doctor</InputLabel>
                <Select
                  label="Doctor"
                  name="doctor"
                  value={form.doctor}
                  onChange={handleInputChange}
                >
                  {doctors.map((doctor, index) => (
                    <MenuItem key={index} value={doctor}>{doctor}</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>

            {/* Select for Location */}
            <Grid item xs={6}>
              <FormControl fullWidth margin="normal">
                <InputLabel>Location</InputLabel>
                <Select
                  label="Location"
                  name="location"
                  value={form.location}
                  onChange={handleInputChange}
                >
                  {locations.map((location, index) => (
                    <MenuItem key={index} value={location}>{location}</MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
          </Grid>

          <TextField
            label="Comments"
            name="comments"
            value={form.comments}
            onChange={handleInputChange}
            multiline
            rows={6}
            fullWidth
            margin="normal"
          />

          <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '16px' }}>
            <Button 
              variant="contained" 
              style={{ backgroundColor: 'grey', color: 'white' }} 
              onClick={() => window.history.back()}
            >
              Cancel
            </Button>
            <Button 
              type="submit" 
              variant="contained" 
              color="primary" 
              onClick={() => navigate('/my-appointments')}>
              Submit
            </Button>
          </div>
        </form>
      </Paper>
    </Container>
  );
}

export default EditAppointmentPage;
