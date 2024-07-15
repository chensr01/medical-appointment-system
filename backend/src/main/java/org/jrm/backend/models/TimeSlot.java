package org.jrm.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

/**
 * The TimeSlot class represents an appointment time slot for a doctor.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class TimeSlot implements HasUuid {
    @Id
    
    private UUID id;

    private UUID doctorId;

    private Date startTime;

    private Date endTime;

    private String location;

    private String type;

    @Setter
    private boolean isAvailable;
}
