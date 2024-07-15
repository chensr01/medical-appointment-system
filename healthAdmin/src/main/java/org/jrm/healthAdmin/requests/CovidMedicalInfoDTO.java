package org.jrm.healthAdmin.requests;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CovidMedicalInfoDTO implements MedicalInfoDTO {
    @JsonProperty("vaccine_count")
    @NotBlank
    private int vaccineCount;

    @NotBlank
    private String type;

    @JsonProperty("covid_count")
    @NotBlank
    private int covidCount;

    @JsonProperty("lung_condition")
    @NotBlank
    private boolean lungCondition;
}
