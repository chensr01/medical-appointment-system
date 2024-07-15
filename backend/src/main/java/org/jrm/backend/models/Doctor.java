package org.jrm.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * The Doctor class represents a doctor in the system.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document
public class Doctor implements HasUuid {
    @Id
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String specialty;
}
