package org.jrm.backend.repositories;

import org.jrm.backend.models.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface TimeSlotRepository extends MongoRepository<TimeSlot, UUID> {
    List<TimeSlot> findByDoctorId(UUID doctorId);

    List<TimeSlot> findByIsAvailableTrue();
}
