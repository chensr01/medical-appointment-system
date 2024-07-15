package org.jrm.healthAdmin.services;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jrm.healthAdmin.models.Appointment;
import org.jrm.healthAdmin.models.TestResult;
import org.jrm.healthAdmin.models.*;
import org.jrm.healthAdmin.repositories.AppointmentRepository;
import org.jrm.healthAdmin.repositories.TestResultRepository;
import org.jrm.healthAdmin.requests.ModifyAppointmentRequest;
import org.jrm.healthAdmin.requests.RegisterAppointmentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class SchedulingAppService {

    private AppointmentRepository appointmentRepository;
    private TestResultRepository testResultRepository;

    // todo: returning ResponseEntity<?> is not a good practice in service.
    public ResponseEntity<Appointment> registerAppointment(RegisterAppointmentRequest request) {
        // Parse and validate start and end times
        LocalDateTime startTime = LocalDateTime.parse(request.start_time(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endTime = LocalDateTime.parse(request.end_time(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (!startTime.isBefore(endTime)) {
            return ResponseEntity.badRequest().build();
        }

        // // Check for time slot availability using the repository method
        // List<Appointment> overlappingAppointments = appointmentRepository.findAppointmentsInTimeRangeForLocation(
        //         request.location(), 
        //         request.start_time(), 
        //         request.end_time()
        // );
        // if (!overlappingAppointments.isEmpty()) {
        //     return ResponseEntity.badRequest().body("Requested time slot is not available.");
        // }

        // Create the Appointment entity from the request
        Appointment newAppointment = new Appointment(
                request.email(),
                request.patient_name(), // Note: This field might be a typo. Should it be patientName instead of email_name?
                request.start_time(),
                request.end_time(),
                request.location(),
                request.appt_type()
        );

        // Save the new Appointment entity
        Appointment savedAppointment = appointmentRepository.save(newAppointment);

        // Now that the Appointment is saved, its ID should be generated and accessible
        UUID appointmentId = savedAppointment.getTestId();
        String appointmentType = savedAppointment.getApptType();
        String patientEmail = savedAppointment.getEmail();

        MedicalInfo medicalInfo;
        switch (appointmentType.toLowerCase()) {
            case "covid":
                medicalInfo = new CovidMedicalInfo("PENDING");
                // Assuming vaccineCount, covidCount, and lungCondition can be set to their default values
                break;
            case "flu":
                medicalInfo = new FluMedicalInfo("PENDING");
                // Assuming daysSick, haveFever, and temperature can be set to their default values
                break;
            default:
                throw new IllegalArgumentException("Unsupported type: " + appointmentType);
        }

        PatientInfo patientInfo = new PatientInfo(patientEmail);

        // Use the appointmentId as test_id for TestResult
        TestResult newTestResult = new TestResult(appointmentId, appointmentType, medicalInfo, patientInfo); // Assuming this constructor exists

        // Save the new TestResult entity
        testResultRepository.save(newTestResult);

        return ResponseEntity.ok(savedAppointment);
    }

    public ResponseEntity<?> modifyAppointment(ModifyAppointmentRequest updateRequest) {
        UUID testId = updateRequest.test_id();
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(testId);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();

            LocalDateTime startTime = LocalDateTime.parse(updateRequest.start_time(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime endTime = LocalDateTime.parse(updateRequest.end_time(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            if (!startTime.isBefore(endTime)) {
                return ResponseEntity.badRequest().body("Start time must be before end time.");
            }

            // Further validation for time slot availability can be added here

            appointment.setStartTime(updateRequest.start_time());
            appointment.setEndTime(updateRequest.end_time());
            appointment.setLocation(updateRequest.location());
            appointment.setApptType(updateRequest.appt_type());

            appointmentRepository.save(appointment);
            return ResponseEntity.ok().body("Appointment updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid test_id input");
        }
    }

    public TestResult getTestResult(@NotNull UUID testId) {
        System.out.println("testID: " + testId);
        // todo: where is the first test result from?
        return testResultRepository.findById(testId).orElse(null);
    }

    public void deleteAppointment(@NotNull UUID testId) {
        appointmentRepository.deleteById(testId);
        testResultRepository.deleteById(testId);
    }
}
