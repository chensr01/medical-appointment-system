package org.jrm.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.UUID;

/**
 * The Patient class represents a patient in the system.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Patient implements HasUuid {
    @Id
    private UUID id;
    private String name;
    private String email;

    public Patient(String name, String email) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
    }
}
