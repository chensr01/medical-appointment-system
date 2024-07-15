package org.jrm.healthAdmin.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class TestResult {

    // for initializing a testResult entity inside the scheduling service
    public TestResult(UUID test_id) {
        this.testId = test_id;
    }

    @Id
    private UUID testId;
    private String type;
    private MedicalInfo medical_info;
    private PatientInfo patient_info;

}
