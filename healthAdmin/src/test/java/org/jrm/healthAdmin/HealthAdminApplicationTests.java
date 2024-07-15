package org.jrm.healthAdmin;

import org.jrm.healthAdmin.exceptions.FailedSaveToCentralDBException;
import org.jrm.healthAdmin.exceptions.MedicalInfoTypeMismatchException;
import org.jrm.healthAdmin.exceptions.NotFoundException;
import org.jrm.healthAdmin.models.Appointment;
import org.jrm.healthAdmin.models.TestResultForCentralDB;
import org.jrm.healthAdmin.requests.*;
import org.jrm.healthAdmin.services.CentralDBService;
import org.jrm.healthAdmin.services.HealthProviderService;
import org.jrm.healthAdmin.services.MockCentralDBService;
import org.jrm.healthAdmin.services.SchedulingAppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@SpringBootTest
class HealthAdminApplicationTests {

    @Autowired
    private SchedulingAppService schedulingAppService;

    @Autowired
    private HealthProviderService healthProviderService;

    private CentralDBService spyCentralDBService;

    @BeforeEach
    void setUp() {
        spyCentralDBService = spy(new MockCentralDBService());
        healthProviderService.setCentralDBService(spyCentralDBService);
    }

    @Test
    public void testPostTestResult() throws NotFoundException, MedicalInfoTypeMismatchException, FailedSaveToCentralDBException {
        // register test
        RegisterAppointmentRequest registerAppointmentRequest = new RegisterAppointmentRequest(
                "lucas@gmail.com",
                "Lucas Lee",
                "2024-03-13T17:30:00",
                "2024-03-13T18:00:00",
                "UPMC",
                "covid"
        );
        ResponseEntity<Appointment> registerAppointmentResponse = schedulingAppService.registerAppointment(registerAppointmentRequest);
        UUID testId = registerAppointmentResponse.getBody().getTestId();

        // enter patient info
        EnterPatientInfoRequest enterPatientInfoRequest = new EnterPatientInfoRequest(
                testId,
                "male",
                "2000-10-12",
                "California",
                "Santa Barbara",
                "93106"
        );
        healthProviderService.addPatientInfo(enterPatientInfoRequest);

        // enter medical info
        CovidMedicalInfoDTO covidMedicalInfoDTO = new CovidMedicalInfoDTO(
                2,
                "covid",
                1,
                false
        );
        EnterMedicalInfoRequest enterMedicalInfoRequest = new EnterMedicalInfoRequest(
                testId,
                covidMedicalInfoDTO
        );
        healthProviderService.addMedicalInfo(enterMedicalInfoRequest);

        // enter test result
        EnterTestResultRequest enterTestResultRequest = new EnterTestResultRequest(
                testId,
                "POSITIVE"
        );
        healthProviderService.addTestResult(enterTestResultRequest);

        LocalDateTime ldt = LocalDateTime.parse("2024-03-13T18:00:00");
        String dateString = ldt.toLocalDate().toString();
        TestResultForCentralDB testResultForCentralDB = new TestResultForCentralDB(
                "Lucas Lee",
                "lucas@gmail.com",
                "male",
                "California",
                "Santa Barbara",
                "93106",
                "2000-10-12",
                null,
                null,
                null,
                2,
                1,
                false,
                testId,
                "covid",
                "UPMC",
                dateString,
                "positive"
        );

        verify(spyCentralDBService).sendData(testResultForCentralDB);

        // delete test
        schedulingAppService.deleteAppointment(testId);
    }

}
