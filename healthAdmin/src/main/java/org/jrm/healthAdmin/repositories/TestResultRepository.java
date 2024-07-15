package org.jrm.healthAdmin.repositories;
import org.jrm.healthAdmin.models.TestResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;
public interface TestResultRepository extends MongoRepository<TestResult, UUID> {
}
