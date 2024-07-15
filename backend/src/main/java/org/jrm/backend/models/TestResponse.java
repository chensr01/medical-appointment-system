package org.jrm.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@JsonDeserialize(using = TestResponseDeserializer.class)
public class TestResponse {
    private String type;

    @JsonProperty("medical_info")
    private MedicalInfo medicalInfo;

    @JsonProperty("patient_info")
    private PatientInfo patientInfo;
}

