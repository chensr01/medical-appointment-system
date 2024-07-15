package org.jrm.healthAdmin.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FluMedicalInfo implements MedicalInfo {
    public FluMedicalInfo(String testResult, int daysSick, boolean haveFever, double temperature) {
        this.testResult = testResult;
        this.daysSick = daysSick;
        this.haveFever = haveFever;
        this.temperature = temperature;
    }

    public FluMedicalInfo(String testResult) {
        this.testResult = testResult;
        this.daysSick = null;
        this.haveFever = null;
        this.temperature = null;
    }


    @JsonProperty("test_result")
    private String testResult;

    @JsonProperty("days_sick")
    private Integer daysSick;

    @JsonProperty("have_fever")
    private Boolean haveFever;

    @JsonProperty("temperature")
    private Double temperature;
}
