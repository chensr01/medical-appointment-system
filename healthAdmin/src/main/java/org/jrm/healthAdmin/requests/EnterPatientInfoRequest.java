package org.jrm.healthAdmin.requests;

import java.util.UUID;

public record EnterPatientInfoRequest(UUID test_id, String sex, String date_of_birth, String state, String city, String zipcode) {
}
