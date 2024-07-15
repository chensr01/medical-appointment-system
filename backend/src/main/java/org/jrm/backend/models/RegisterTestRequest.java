package org.jrm.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterTestRequest {
    private String email;
    private String patient_name;
    private String start_time;
    private String end_time;
    private String location;
    private String appt_type;

}
