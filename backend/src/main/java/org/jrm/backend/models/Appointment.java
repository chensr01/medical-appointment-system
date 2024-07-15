package org.jrm.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * The Appointment class represents an appointment between a doctor and a patient.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Appointment implements HasUuid {
    @Id
    private UUID id;
    private UUID patientId;
    private UUID timeSlotId;
    private UUID testID;

    public Appointment(UUID patientId, UUID timeSlotId, UUID testID) {
        this.id = UUID.randomUUID();
        this.patientId = patientId;
        this.timeSlotId = timeSlotId;
        this.testID = testID;
    }
}
