package org.jrm.backend.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TimeSlotResponse {

    private UUID id;

    private String doctor;

    private String date;

    private String time;

    private String location;

    private String type;

}
