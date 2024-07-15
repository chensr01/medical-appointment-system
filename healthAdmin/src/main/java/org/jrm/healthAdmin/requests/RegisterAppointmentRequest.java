package org.jrm.healthAdmin.requests;

public record RegisterAppointmentRequest(String email, String patient_name, String start_time, String end_time, String location, String appt_type) {
}
