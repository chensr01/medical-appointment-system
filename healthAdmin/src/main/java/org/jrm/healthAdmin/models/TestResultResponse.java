package org.jrm.healthAdmin.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestResultResponse {
    private String type;
    private MedicalInfo medical_info;
    private PatientInfo patient_info;

    public TestResultResponse(String type, MedicalInfo medical_info, PatientInfo patient_info) {
        this.type = type;
        this.medical_info = medical_info;
        this.patient_info = patient_info;
    }

    public static TestResultResponse fromTestResult(TestResult testResult) {
        return new TestResultResponse(testResult.getType(), testResult.getMedical_info(), testResult.getPatient_info());
    }
}
