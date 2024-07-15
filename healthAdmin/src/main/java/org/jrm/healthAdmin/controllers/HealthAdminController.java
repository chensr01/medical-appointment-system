package org.jrm.healthAdmin.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jrm.healthAdmin.exceptions.FailedSaveToCentralDBException;
import org.jrm.healthAdmin.exceptions.MedicalInfoTypeMismatchException;
import org.jrm.healthAdmin.exceptions.NotFoundException;
import org.jrm.healthAdmin.models.*;
import org.jrm.healthAdmin.requests.*;
import org.jrm.healthAdmin.services.HealthProviderService;
import org.jrm.healthAdmin.services.SchedulingAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class HealthAdminController {
    private final SchedulingAppService schedulingAppService;
    private final HealthProviderService healthProviderService;


    // API for health provider
    @PostMapping("/test/test-result")
    public ResponseEntity<Void> enterTestResult(@RequestBody EnterTestResultRequest request) {
        try {
            healthProviderService.addTestResult(request);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (FailedSaveToCentralDBException e) {
            return  ResponseEntity.status(428).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/test/medical-info")
    public ResponseEntity<Void> enterMedicalInfo(@Valid @RequestBody EnterMedicalInfoRequest request) {
        try {
            healthProviderService.addMedicalInfo(request);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (MedicalInfoTypeMismatchException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/patient-info")
    public ResponseEntity<Void> enterPatientInfo(@RequestBody EnterPatientInfoRequest request) {
        try {
            healthProviderService.addPatientInfo(request);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }



    // API for scheduling app
    @PostMapping("/test")
    public ResponseEntity<TestIdResponse> registerTestAppointment(@RequestBody RegisterAppointmentRequest request) {
        ResponseEntity<?> response = schedulingAppService.registerAppointment(request);
        if (response.getStatusCode().is2xxSuccessful()) {
            // Assuming the service method handles creation and returns the Appointment directly
            Appointment registeredAppointment = (Appointment) response.getBody();
            return ResponseEntity.ok(new TestIdResponse(registeredAppointment.getTestId()));
        } else {
            return new ResponseEntity<>(response.getStatusCode());
        }
    }

    @PutMapping("/test")
    public ResponseEntity<Void> modifyTestAppointment(@RequestBody ModifyAppointmentRequest request) {
        ResponseEntity<?> response = schedulingAppService.modifyAppointment(request);
        return new ResponseEntity<>(response.getStatusCode());
    }

    @GetMapping("/test/{test_id}")
    public ResponseEntity<?> getTestResult(@PathVariable UUID test_id) {
        if (test_id == null) {
            return ResponseEntity.badRequest().body("Test ID is required.");
        }
        var testResult = schedulingAppService.getTestResult(test_id);
        if (testResult == null) {
            // According to the best practices, it is better to return a 404 status code if the resource is not found.
            // However, since the API doc specifies to return a 400 status code, we will return that.
            return ResponseEntity.notFound().build();
        }
        var response = TestResultResponse.fromTestResult(testResult);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/test/{test_id}")
    public ResponseEntity<?> deleteTest(@PathVariable UUID test_id) {
        if (test_id == null) {
            return ResponseEntity.badRequest().body("Test ID is required.");
        }
        schedulingAppService.deleteAppointment(test_id);
        return ResponseEntity.ok().build();
    }
}
