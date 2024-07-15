package org.jrm.healthAdmin.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CovidMedicalInfo implements MedicalInfo{
    public CovidMedicalInfo(String testResult, int vaccineCount, int covidCount, boolean lungCondition) {
        this.testResult = testResult;
        this.vaccineCount = vaccineCount;
        this.covidCount = covidCount;
        this.lungCondition = lungCondition;
    }

    public CovidMedicalInfo(String testResult) {
        this.testResult = testResult;
        this.vaccineCount = null;
        this.covidCount = null;
        this.lungCondition = null;
    }

    @JsonProperty("test_result")
    private String testResult;

    @JsonProperty("vaccine_count")
    private Integer vaccineCount;

    @JsonProperty("covid_count")
    private Integer covidCount;

    @JsonProperty("lung_condition")
    private Boolean lungCondition;
}
