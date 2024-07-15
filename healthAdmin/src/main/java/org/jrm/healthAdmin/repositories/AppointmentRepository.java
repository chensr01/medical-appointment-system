package org.jrm.healthAdmin.repositories;

import org.jrm.healthAdmin.models.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends MongoRepository<Appointment, UUID> {
    
    @Query("{'location': ?0, '$or': [{'startTime': {'$gte': ?1, '$lt': ?2}}, {'endTime': {'$gt': ?1, '$lte': ?2}}]}")
    List<Appointment> findAppointmentsInTimeRangeForLocation(String location, String startTime, String endTime);
}