package org.jrm.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private UUID id;
    private String doctor;
    private UUID patientId;
    private String time;
    private String date;
    private UUID timeSlotId;
    private String location;
    private String type;
    // add two fields
    private String testResult;
    private String quarantineResult;
}
