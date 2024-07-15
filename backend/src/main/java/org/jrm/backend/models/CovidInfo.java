package org.jrm.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeName("covid")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CovidInfo implements MedicalInfo {
    @JsonProperty("test_result")
    private String testResult;
    @JsonProperty("vaccine_count")
    private Integer vaccineCount;
    @JsonProperty("covid_count")
    private Integer covidCount;
    @JsonProperty("lung_condition")
    private Boolean lungCondition;
}

