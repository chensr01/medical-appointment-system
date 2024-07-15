package org.jrm.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonTypeName("flu")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FluInfo implements MedicalInfo {
    @JsonProperty("test_result")
    private String testResult;
    @JsonProperty("days_sick")
    private Integer daysSick;
    @JsonProperty("have_fever")
    private Boolean haveFever;
    @JsonProperty("temperature")
    private Double temperature;
}

