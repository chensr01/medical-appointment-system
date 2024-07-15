package org.jrm.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuarantineRequest {
    private String appt_type;
    private String city;
    private String state;
    private String zipcode;
    private String dob;
    private String gender;
    private Integer vaccination_count;
    private Integer disease_count;
    private Boolean lung_condition;
}
