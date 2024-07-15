package org.jrm.healthAdmin.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Appointment {
    @Id
    private UUID testId;
    private String email;
    private String patientName;
    private String startTime;
    private String endTime;
    private String location;
    private String apptType;

    // Constructor
    public Appointment(String email, String patientName, String startTime, String endTime, String location, String apptType) {
        this.testId = UUID.randomUUID(); // Automatically generate a unique ID
        this.email = email;
        this.patientName = patientName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.apptType = apptType;
    }

    // Getters and setters

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getApptType() {
        return apptType;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }
}

