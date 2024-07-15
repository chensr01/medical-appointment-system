package org.jrm.healthAdmin.requests;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class EnterMedicalInfoRequest {
    @JsonProperty("test_id")
    private UUID testId;


    @JsonProperty("medical_info")
    @JsonDeserialize(using = MedicalInfoDeserializer.class)
    private MedicalInfoDTO medicalInfo;
}
