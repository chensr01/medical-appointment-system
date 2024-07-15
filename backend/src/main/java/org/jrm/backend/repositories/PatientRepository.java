package org.jrm.backend.repositories;

import org.jrm.backend.models.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;


public interface PatientRepository extends MongoRepository<Patient, UUID> {
    Optional<Patient> findByEmail(String email);
}
