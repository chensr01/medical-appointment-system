package org.jrm.backend.controllers;

import lombok.AllArgsConstructor;
import org.jrm.backend.exceptions.*;
import org.jrm.backend.models.Appointment;
import org.jrm.backend.models.AppointmentResponse;
import org.jrm.backend.models.TimeSlot;
import org.jrm.backend.models.TimeSlotResponse;
import org.jrm.backend.requests.ModifyAppointmentRequest;
import org.jrm.backend.requests.NewAppointmentRequest;
import org.jrm.backend.services.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/appointment/timeslots/all")
    public ResponseEntity<List<TimeSlotResponse>> getTimeSlots() {
        var timeSlots = appointmentService.getTimeSlots();
        return ResponseEntity.ok(timeSlots);
    }

    @GetMapping("/appointment/timeslots")
    public ResponseEntity<List<TimeSlot>> getTimeSlotsByDoctorId(@RequestParam UUID doctorId) {
        try {
            var timeSlots = appointmentService.getTimeSlotsByDoctorId(doctorId);
            return ResponseEntity.ok(timeSlots);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/appointment")
    public ResponseEntity<Appointment> newAppointment(@RequestBody NewAppointmentRequest request) {
        try {
            // Note: we don't support descriptions yet, so we're passing an empty string
            var appointment = appointmentService.newAppointment(request.patientId(), request.timeSlotId());
            return ResponseEntity.ok(appointment);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (TimeSlotUnavailableException e) {
            return ResponseEntity.badRequest().build();
        } catch (RegisterTestException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    @GetMapping("/appointment")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPatientId(@RequestParam UUID patientId) {
        try {
            var appointments = appointmentService.getAppointmentsByPatientId(patientId);
            return ResponseEntity.ok(appointments);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UnableToGetTestResultException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        } catch (UnableToGetQuarantineException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/appointment/modify")
    public ResponseEntity<Appointment> modifyAppointment(@RequestBody ModifyAppointmentRequest request) {
        try {
            var updatedAppointment = appointmentService.modifyAppointment(request);
            return ResponseEntity.ok(updatedAppointment);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/appointment")
    public ResponseEntity<Void> deleteAppointment(@RequestParam UUID appointmentId) {
        try {
            appointmentService.deleteAppointment(appointmentId);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DeleteTestException e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
