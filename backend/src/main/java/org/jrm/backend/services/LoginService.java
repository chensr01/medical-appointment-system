package org.jrm.backend.services;

import lombok.AllArgsConstructor;
import org.jrm.backend.exceptions.UserExistsException;
import org.jrm.backend.exceptions.NotFoundException;
import org.jrm.backend.models.Patient;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoginService {
    private PatientService patientService;

    public Patient register(String name, String email) throws UserExistsException {
        var existingPatient = patientService.getPatientByEmail(email);
        if (existingPatient.isPresent()) {
            throw new UserExistsException();
        }
        var patient = patientService.createPatient(name, email);
        patientService.savePatient(patient);
        return patient;
    }

    public Patient login(String name, String email) throws NotFoundException {
        var patient = patientService.getPatientByEmail(email);
        if (patient.isEmpty()) {
            throw new NotFoundException();
        }
        return patient.get();
    }
}
