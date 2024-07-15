package org.jrm.backend.repositories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.jrm.backend.models.Doctor;
import java.util.UUID;


public interface DoctorRepository extends MongoRepository<Doctor, UUID>{
}
