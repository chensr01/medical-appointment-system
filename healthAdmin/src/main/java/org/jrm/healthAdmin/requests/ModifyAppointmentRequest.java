package org.jrm.healthAdmin.requests;
import java.util.UUID;
public record ModifyAppointmentRequest(UUID test_id, String start_time, String end_time, String location, String appt_type) {
}
