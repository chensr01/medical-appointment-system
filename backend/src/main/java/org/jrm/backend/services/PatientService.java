package org.jrm.backend.services;

import lombok.AllArgsConstructor;
import org.jrm.backend.models.Patient;
import org.jrm.backend.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PatientService {
    private PatientRepository patientRepository;

    public Optional<Patient> getPatientById(UUID id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public Patient createPatient(String name, String email) {
        return new Patient(name, email);
    }

    public void savePatient(Patient patient) {
        patientRepository.save(patient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
