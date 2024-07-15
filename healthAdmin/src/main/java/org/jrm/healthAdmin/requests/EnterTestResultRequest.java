package org.jrm.healthAdmin.requests;
import java.util.UUID;
public record EnterTestResultRequest(UUID test_id, String test_results) {
}
