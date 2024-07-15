package org.jrm.healthAdmin.models;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode // for comparing the content field by field (needed for test)
public class TestResultForCentralDB {
    private String patient_name;
    private String email;
    private String gender;
    private String state;
    private String city;
    private String zipcode;
    private String dateOfBirth;
    private Integer daysSick;
    private Boolean haveFever;
    private Integer bodyTemperature;
    private Integer vaccineCount;
    private Integer covidCount;
    private Boolean lungCondition;
    private UUID testId;
    private String testType;
    private String testLocation;
    private String testReportDate;
    private String testResult;
}
