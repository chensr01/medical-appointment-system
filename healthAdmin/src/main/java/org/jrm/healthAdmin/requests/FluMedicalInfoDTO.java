package org.jrm.healthAdmin.requests;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FluMedicalInfoDTO implements MedicalInfoDTO {
    @NotBlank
    @JsonProperty("days_sick")
    private int daysSick;

    @NotBlank
    private String type;

    @NotBlank
    @JsonProperty("have_fever")
    private boolean haveFever;

    @NotBlank
    private double temperature;
}
