package org.jrm.backend.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.jrm.backend.models.Appointment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository extends MongoRepository<Appointment, UUID> {
    List<Appointment> findByPatientId(UUID patientId);

    Optional<Appointment> findByTimeSlotId(UUID timeSlotId);
}

