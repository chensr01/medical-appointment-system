import React, {useState} from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import {Button, TextField, Typography, Paper, Container, Grid} from '@mui/material';
import axios from 'axios';

interface Form {
  name: string;
  phone: string;
  email: string;
  comments: string;
}

function CreateAppointmentPage() {
  // Extract the passed state from the ViewSlotsPage
  const navigate = useNavigate();
  const location = useLocation();
  const slotInfo = location.state || {}; // default to an empty object if state is undefined

  const [form, setForm] = useState({
    name: '',
    phone: '',
    email: '',
    comments: '',
  } as Form);

  // Receive inputs
  // TODO: using any is a bad practice, replace it with a proper type
  const handleInputChange = (event: any) => {
    const {name, value} = event.target;
    setForm({...form, [name]: value});
  };

  // Submit the form data to backend
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


  return (
    <Container maxWidth="sm">
      <Paper elevation={3} style={{padding: '16px', margin: '16px 0'}}>
        <Typography variant="h4" align="center" gutterBottom>
          New Appointment
        </Typography>

        <form onSubmit={handleSubmit}>

          {/* Display the appointment information as uneditable text fields */}
          <Grid container spacing={2} alignItems="center" justifyContent="space-between">
            <Grid item xs={6}>
              <TextField
                label="Date"
                value={slotInfo.date}
                disabled
                fullWidth
                margin="normal"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                label="Time"
                value={slotInfo.time}
                disabled
                fullWidth
                margin="normal"
              />
            </Grid>
          </Grid>

          <Grid container spacing={2} alignItems="center" justifyContent="space-between">
            <Grid item xs={6}>
              <TextField
                label="Doctor"
                value={slotInfo.doctor}
                disabled
                fullWidth
                margin="normal"
              />
            </Grid>
            <Grid item xs={6}>
              <TextField
                label="Location"
                value={slotInfo.location}
                disabled
                fullWidth
                margin="normal"
              />
            </Grid>
          </Grid>

          {/* Let user enter comments */}
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

          {/* Buttons for submit or cancel */}
          <div style={{display: 'flex', justifyContent: 'space-between', marginTop: '16px'}}>
            <Button
              variant="contained"
              style={{backgroundColor: 'grey', color: 'white'}}
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

export default CreateAppointmentPage;
