package org.jrm.healthAdmin.models;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TestIdResponse {
    public TestIdResponse(UUID test_id) {
        this.test_id = test_id;
    }

    private UUID test_id;
}
