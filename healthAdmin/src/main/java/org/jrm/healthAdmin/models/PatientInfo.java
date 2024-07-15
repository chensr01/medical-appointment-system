package org.jrm.healthAdmin.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientInfo {

    public PatientInfo(String email) {
        this.email = email;
        this.sex = null;
        this.date_of_birth = null;
        this.state = null;
        this.city = null;
        this.zipcode = null;
    }

    private String email;
    private String sex;
    private String date_of_birth;
    private String state;
    private String city;
    private String zipcode;

}
