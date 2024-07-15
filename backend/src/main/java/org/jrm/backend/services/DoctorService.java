package org.jrm.backend.services;

import lombok.AllArgsConstructor;
import org.jrm.backend.models.Doctor;
import org.jrm.backend.repositories.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class DoctorService {
    private DoctorRepository doctorRepository;



    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(UUID id) {
        return doctorRepository.findById(id);
    }

    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(UUID id) {
        doctorRepository.deleteById(id);
    }

    // for future use
    // public Optional<Doctor> updateDoctor(UUID id, Doctor updatedDoctor) {
    //     return doctorRepository.findById(id).map(existingDoctor -> {
    //         existingDoctor.setName(updatedDoctor.getName());
    //         existingDoctor.setEmail(updatedDoctor.getEmail());
    //         existingDoctor.setPhone(updatedDoctor.getPhone());
    //         existingDoctor.setSpecialty(updatedDoctor.getSpecialty());
    //         return doctorRepository.save(existingDoctor);
    //     });
    // }


}
