package org.jrm.backend.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jrm.backend.models.*;
import org.jrm.backend.repositories.AppointmentRepository;
import org.jrm.backend.repositories.DoctorRepository;
import org.jrm.backend.repositories.PatientRepository;
import org.jrm.backend.repositories.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
class LoadStorage {
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Adding an interceptor to log request body
        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                // Log the URL and the Request Body
//                System.out.println("Sending request to URL: " + request.getURI());
                String requestBody = new String(body, StandardCharsets.UTF_8);
//                System.out.println("Request Body: " + requestBody);
                return execution.execute(request, body);
            }
        });
        return restTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(LoadStorage.class);

    private static final UUID doctorId1 = UUID.fromString("72FFE602-837D-47F6-A145-87A0F368C9A6");
    private static final UUID doctorId2 = UUID.fromString("66CD7D8E-3ED5-4CDB-BBA6-D10076ADDC52");

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Bean
    CommandLineRunner initPatients(PatientRepository repository) {
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient(UUID.fromString("DFEE3590-A44D-4D57-B27E-B76346ACD832"), "Bilbo Baggins", "bilbob@ring.com"));
        patients.add(new Patient(UUID.fromString("E33C62BF-EA59-4A91-9830-72B23DA93D20"), "Frodo Baggins", "frodob@ring.com"));
        patients.add(new Patient(UUID.fromString("5A0D8A5D-50DA-4DB9-8CF8-28D94178B262"), "Samwise Gamgee", "samwiseg@ring.com"));
        patients.add(new Patient(UUID.fromString("DEB33B30-D041-41DD-8AAC-068C554A20A9"), "Meriadoc Brandybuck", "meriadocb@ring.com"));

        return args -> {
            for (var patient : patients) {
                log.info("Preloading " + repository.save(patient));
            }
        };
    }

    @Bean
    CommandLineRunner initDoctors(DoctorRepository repository) {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor(doctorId1, "Dr. John Doe", "johnd@doctor.com", "123-456-7890", "Cardiology"));
        doctors.add(new Doctor(doctorId2, "Dr. Jane Smith", "janes@doctor.com", "456-789-0123", "Neurology"));

        return args -> {
            for (var doctor : doctors) {
                log.info("Preloading " + repository.save(doctor));
            }
        };
    }

    @Bean
    CommandLineRunner initTimeSlots(TimeSlotRepository repository) throws ParseException {
        List<TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new TimeSlot(UUID.fromString("6635C3FF-8120-4716-96AC-920257BA4D82"), doctorId1, formatter.parse("2021-12-01T08:00:00"), formatter.parse("2021-12-01T08:30:00"), "UPMC", "covid", true));
        timeSlots.add(new TimeSlot(UUID.fromString("AA68E9D5-3DF5-4282-A262-A7B3AB4D7FA0"), doctorId1, formatter.parse("2021-12-01T08:30:00"), formatter.parse("2021-12-01T09:00:00"), "CVS", "covid", true));
        timeSlots.add(new TimeSlot(UUID.fromString("08769894-702F-4C7F-A60F-0FF2ED2E82A8"), doctorId2, formatter.parse("2021-12-01T09:00:00"), formatter.parse("2021-12-01T09:30:00"), "UPMC", "flu", true));
        timeSlots.add(new TimeSlot(UUID.fromString("A2DB5ACB-1A94-4602-ACBF-E7E7E6B86DA9"), doctorId2, formatter.parse("2021-12-01T09:30:00"), formatter.parse("2021-12-01T10:00:00"), "CVS", "flu", true));
        timeSlots.add(new TimeSlot(UUID.fromString("90b5ef39-67bd-4ae9-91b7-c7f5170289d2"), doctorId1, formatter.parse("2021-12-01T10:00:00"), formatter.parse("2021-12-01T10:30:00"), "Allegheny General Hospital", "flu", true));
        timeSlots.add(new TimeSlot(UUID.fromString("d7ca0d8d-e488-468a-80ee-bdcf653ddb94"), doctorId2, formatter.parse("2021-12-01T11:00:00"), formatter.parse("2021-12-01T11:30:00"), "Westmoreland Hospital", "covid", true));

        return args -> {
            for (var timeSlot : timeSlots) {
                log.info("Preloading " + repository.save(timeSlot));
            }
        };
    }

//    @Bean
//    CommandLineRunner initAppointments(AppointmentRepository appointmentRepository) {
//        List<Appointment> appointments = new ArrayList<>();
//        appointments.add(new Appointment(UUID.fromString("A3D3A3A3-3A3A-3A3A-3A3A-3A3A3A3A3A3A"), UUID.fromString("DFEE3590-A44D-4D57-B27E-B76346ACD832"), UUID.fromString("6635C3FF-8120-4716-96AC-920257BA4D82")));
//
//        return args -> {
//            for (var appointment : appointments) {
//                log.info("Preloading " + appointmentRepository.save(appointment));
//            }
//        };
//    }
}