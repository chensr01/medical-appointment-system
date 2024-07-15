package org.jrm.backend.requests;

import java.util.UUID;

public record NewAppointmentRequest(UUID patientId, UUID timeSlotId) {
}
