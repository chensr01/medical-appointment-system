package org.jrm.backend.requests;

import java.util.UUID;

public record ModifyAppointmentRequest(UUID id, UUID timeSlotId) {
}
