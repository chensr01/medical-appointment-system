package org.jrm.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PatientInfo {
    private String email;
    private String sex;
    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;
    private String state;
    private String city;
    private String zipcode;
}

