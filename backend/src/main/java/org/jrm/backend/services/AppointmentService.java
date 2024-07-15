package org.jrm.backend.services;

import lombok.AllArgsConstructor;
import org.jrm.backend.exceptions.*;
import org.jrm.backend.models.*;
import org.jrm.backend.repositories.AppointmentRepository;
import org.jrm.backend.repositories.PatientRepository;
import org.jrm.backend.repositories.TimeSlotRepository;
import org.jrm.backend.requests.ModifyAppointmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@Service
public class AppointmentService {
    private PatientService patientService;

    private DoctorService doctorService;

    private TimeSlotRepository timeSlotRepository;

    private AppointmentRepository appointmentRepository;

    private PatientRepository patientRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<TimeSlotResponse> getTimeSlots() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mma");
        List<TimeSlot> timeSlotList = timeSlotRepository.findByIsAvailableTrue();
        List<TimeSlotResponse> timeSlotResponseList = timeSlotList.stream()
                .map(timeSlot -> {
                    String doctor = doctorService.getDoctorById(timeSlot.getDoctorId()).get().getName();
                    String date = dateFormat.format(timeSlot.getStartTime());
                    String time = timeFormat.format(timeSlot.getStartTime()) + " - " + timeFormat.format(timeSlot.getEndTime());
                    String location = timeSlot.getLocation();
                    String type = timeSlot.getType();
                    return new TimeSlotResponse(timeSlot.getId(), doctor, date, time, location, type);
                })
                .collect(Collectors.toList());
        return timeSlotResponseList;
    }

    public List<TimeSlot> getTimeSlotsByDoctorId(UUID doctorId) throws NotFoundException {
        if (doctorService.getDoctorById(doctorId).isEmpty()) {
            throw new NotFoundException();
        }
        return timeSlotRepository.findByDoctorId(doctorId);
    }

    public Appointment newAppointment(UUID patientId, UUID timeSlotId) throws TimeSlotUnavailableException, NotFoundException, RegisterTestException {
        var timeSlot = timeSlotRepository.findById(timeSlotId);
        if (timeSlot.isEmpty()) {
            throw new NotFoundException();
        }
        if (appointmentRepository.findByTimeSlotId(timeSlotId).isPresent()) {
            throw new TimeSlotUnavailableException();
        }
        // Reformat request body for healthcare admin service API
        var patient = patientRepository.findById(patientId).get();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String startDate = formatter.format(timeSlot.get().getStartTime());
        String endDate = formatter.format(timeSlot.get().getEndTime());
        RegisterTestRequest registerTestRequest = new RegisterTestRequest(
                patient.getEmail(),
                patient.getName(),
                startDate,
                endDate,
                timeSlot.get().getLocation(),
                timeSlot.get().getType()
        );
        // Call external healthcare admin service API
        ResponseEntity<TestIdResponse> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(
                    "http://128.2.204.151:8848/test",
                    registerTestRequest,
                    TestIdResponse.class
            );
        } catch (HttpClientErrorException e) {
            throw new RegisterTestException();
        }
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            throw new RegisterTestException();
        }
        // Get testId from response body
        var testId = responseEntity.getBody().test_id();
        UUID testIdUUID = UUID.fromString(testId);
        System.out.println("Received test_id: " + testId);

        // Set the time slot to unavailable
        timeSlot.get().setAvailable(false); // fix invalid timeslot fetch by setting timeslot at first
        timeSlotRepository.save(timeSlot.get());
        var appointment = new Appointment(patientId, timeSlotId, testIdUUID);
        appointmentRepository.save(appointment);
        return appointment;
    }

    public List<AppointmentResponse> getAppointmentsByPatientId(UUID patientId) throws NotFoundException,
            UnableToGetTestResultException, UnableToGetQuarantineException {
        if (patientService.getPatientById(patientId).isEmpty()) {
            throw new NotFoundException();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mma");
        List<Appointment> appointmentList = appointmentRepository.findByPatientId(patientId);
        List<AppointmentResponse> appointmentResponseList = new ArrayList<>();

        for (Appointment appointment : appointmentList) {
            TimeSlot timeSlot = timeSlotRepository.findById(appointment.getTimeSlotId()).orElseThrow(NotFoundException::new);
            String doctor = doctorService.getDoctorById(timeSlot.getDoctorId()).orElseThrow(NotFoundException::new).getName();
            String date = dateFormat.format(timeSlot.getStartTime());
            String time = timeFormat.format(timeSlot.getStartTime()) + " - " + timeFormat.format(timeSlot.getEndTime());

            // get test result from healthcare admin service
            ResponseEntity<TestResponse> responseEntity;
            UUID testId = appointment.getTestID();
            String testIdStr = testId.toString();
            String url = "http://128.2.204.151:8848/test/" + testIdStr;
            try {
                responseEntity = restTemplate.getForEntity(url, TestResponse.class);
            } catch (HttpClientErrorException e) {
                throw new UnableToGetTestResultException();
            }
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                throw new UnableToGetTestResultException();
            }

            // check test result
            TestResponse healthcareResponse = responseEntity.getBody();
//            System.out.println("TYPE is:" + healthcareResponse.getType());
            MedicalInfo medicalInfo = healthcareResponse.getMedicalInfo();
//            System.out.println("is flu:" + (medicalInfo instanceof FluInfo));
            String testResult;
            if (medicalInfo instanceof FluInfo) {
                testResult = ((FluInfo) medicalInfo).getTestResult();
            } else {
                testResult = ((CovidInfo) medicalInfo).getTestResult();
            }
            // test results are not ready
            if (testResult.equals("PENDING")){
                appointmentResponseList.add(new AppointmentResponse(
                        appointment.getId(), doctor, appointment.getPatientId(), time, date, appointment.getTimeSlotId(),
                        timeSlot.getLocation(), timeSlot.getType(), testResult, "Wait for test result")
                );
            }
            // test results are negative or get flu only
            else if (testResult.equals("NEGATIVE") || testResult.equals("UNKNOWN") || healthcareResponse.getType().equals("flu")){
                appointmentResponseList.add(new AppointmentResponse(
                        appointment.getId(), doctor, appointment.getPatientId(), time, date, appointment.getTimeSlotId(),
                        timeSlot.getLocation(), timeSlot.getType(), testResult, "No quarantine needed")
                );
            }
            // test result is covid positive
            else {
                // get quarantine duration from public info service
                // arrange request body
                Integer vacCount = 0;
                Integer disCount = 0;
                Boolean lungCondition = false;
                if (medicalInfo instanceof CovidInfo) {
                    vacCount = ((CovidInfo) medicalInfo).getVaccineCount();
                    disCount = ((CovidInfo) medicalInfo).getCovidCount();
                    lungCondition = ((CovidInfo) medicalInfo).getLungCondition();
                }
                DateTimeFormatter dobformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dob = healthcareResponse.getPatientInfo().getDateOfBirth().format(dobformatter);

                // send request to public info service
                ResponseEntity<QuarantineResponse> quarantineResponseResponseEntity;
                QuarantineRequest quarantineRequest = new QuarantineRequest(
                        healthcareResponse.getType(),
                        healthcareResponse.getPatientInfo().getCity(),
                        healthcareResponse.getPatientInfo().getState(),
                        healthcareResponse.getPatientInfo().getZipcode(),
                        dob,
                        healthcareResponse.getPatientInfo().getSex(),
                        vacCount,
                        disCount,
                        lungCondition
                );
                try {
                    quarantineResponseResponseEntity = restTemplate.postForEntity(
                            "http://128.2.205.66:8888/quarantine_result/",
                            quarantineRequest,
                            QuarantineResponse.class
                    );
                } catch (HttpClientErrorException e) {
                    System.out.println("HTTP Status Code: " + e.getStatusCode());  // Print HTTP status code
                    System.out.println("Response Body: " + e.getResponseBodyAsString());  // Print the response body
                    throw new UnableToGetQuarantineException();
                }
                if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                    throw new UnableToGetQuarantineException();
                }
                appointmentResponseList.add(new AppointmentResponse(
                        appointment.getId(), doctor, appointment.getPatientId(), time, date, appointment.getTimeSlotId(),
                        timeSlot.getLocation(), timeSlot.getType(), testResult, quarantineResponseResponseEntity.getBody().quarantine_duration().toString()+" days")
                );
            }

        }
        return appointmentResponseList;
    }

    public Appointment modifyAppointment(ModifyAppointmentRequest request) throws NotFoundException, TimeSlotUnavailableException, ModifyTestException {
        // Check if the appointment exists
        Appointment existingAppointment = appointmentRepository.findById(request.id())
                .orElseThrow(NotFoundException::new);
        // Set the old time slot to available
        TimeSlot oldTimeSlot = timeSlotRepository.findById(existingAppointment.getTimeSlotId())
                .orElseThrow(() -> new NotFoundException());
        // Check if the new timeslot is booked
        if (appointmentRepository.findByTimeSlotId(request.timeSlotId()).isPresent()) {
            throw new TimeSlotUnavailableException();
        }
        TimeSlot newTimeSlot = timeSlotRepository.findById(request.timeSlotId())
                .orElseThrow(() -> new NotFoundException());

        // Call healthcare admin service api to modify appointment
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String startDate = formatter.format(newTimeSlot.getStartTime());
        String endDate = formatter.format(newTimeSlot.getEndTime());
        ModifyTestRequest modifyTestRequest = new ModifyTestRequest(
                existingAppointment.getTestID().toString(),
                startDate,
                endDate,
                newTimeSlot.getLocation(),
                newTimeSlot.getType()
        );
        try {
            // Creating an HttpEntity that contains the request body
            HttpEntity<ModifyTestRequest> requestEntity = new HttpEntity<>(modifyTestRequest);
            // Making a PUT request to the specified URL and expecting no response body
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    "http://128.2.204.151:8848/test",
                    HttpMethod.PUT,
                    requestEntity,
                    Void.class
            );
            // Check if the status code is 200 OK
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                throw new ModifyTestException();
            }
        } catch (HttpClientErrorException e) {
            throw new ModifyTestException();
        }

        oldTimeSlot.setAvailable(true);
        timeSlotRepository.save(oldTimeSlot); // Save the old time slot
        // Set the new time slot to unavailable
        newTimeSlot.setAvailable(false);
        timeSlotRepository.save(newTimeSlot); // Save the new time slot
        // Update the fields for the existing appointment
        existingAppointment.setTimeSlotId(request.timeSlotId());
        return appointmentRepository.save(existingAppointment);
    }

    public void deleteAppointment(UUID appointmentId) throws NotFoundException, DeleteTestException {
        var appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isEmpty()) {
            return;
        }
        // Set the time slot to available
        var timeSlot = timeSlotRepository.findById(appointment.get().getTimeSlotId()).orElseThrow(() -> new NotFoundException());;

        // Call healthcare admin service api to delete appointment
        try {
            // Prepare the URL with path variable
            String url = "http://128.2.204.151:8848/test/{test_id}";
            // Create an HttpEntity with null body as DELETE usually does not require a body
            HttpEntity<?> entity = new HttpEntity<>(null);
            // Making a DELETE request and capturing the response to check the status code
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    entity,
                    Void.class,
                    appointment.get().getTestID().toString()  // Path variable substitution
            );
            // Check if the status code is 200 OK
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                throw new DeleteTestException();
            }
        } catch (HttpClientErrorException e) {
            throw new DeleteTestException();
        }


        timeSlot.setAvailable(true);
        timeSlotRepository.save(timeSlot);
        appointmentRepository.deleteById(appointmentId);
    }

}
