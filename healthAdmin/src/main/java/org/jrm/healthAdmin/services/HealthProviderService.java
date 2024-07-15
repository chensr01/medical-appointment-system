package org.jrm.healthAdmin.services;
import lombok.AllArgsConstructor;
import org.jrm.healthAdmin.exceptions.FailedSaveToCentralDBException;
import org.jrm.healthAdmin.exceptions.MedicalInfoTypeMismatchException;
import org.jrm.healthAdmin.exceptions.NotFoundException;
import org.jrm.healthAdmin.models.*;
import org.jrm.healthAdmin.repositories.AppointmentRepository;
import org.jrm.healthAdmin.repositories.TestResultRepository;
import org.jrm.healthAdmin.requests.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@AllArgsConstructor
@Service
public class HealthProviderService {
    private TestResultRepository testResultRepository;
    private AppointmentRepository appointmentRepository;
    private CentralDBService centralDBService;

    public void setCentralDBService(CentralDBService centralDBService) {
        this.centralDBService = centralDBService;
    }

    public void pushTestResultToCentralDB(TestResult testResult) throws FailedSaveToCentralDBException {
        Appointment appointment = appointmentRepository.findById(testResult.getTestId()).get();

        Integer daysSick = null;
        Boolean haveFever = null;
        Integer temperature = null;
        String test_result;
        Integer vaccineCount = null;
        Integer covidCount = null;
        Boolean lungCondition = null;
        String endTime = appointment.getEndTime();

        LocalDateTime ldt = LocalDateTime.parse(endTime);
        String dateString = ldt.toLocalDate().toString();

        if (testResult.getMedical_info() instanceof FluMedicalInfo) {
            FluMedicalInfo fluInfo = (FluMedicalInfo) testResult.getMedical_info();
            daysSick = fluInfo.getDaysSick();
            haveFever = fluInfo.getHaveFever();
            temperature = (int) fluInfo.getTemperature().doubleValue();
            test_result = fluInfo.getTestResult().toLowerCase(Locale.ENGLISH);
        } else {
            CovidMedicalInfo covidInfo = (CovidMedicalInfo) testResult.getMedical_info();
            test_result = covidInfo.getTestResult().toLowerCase(Locale.ENGLISH);
            vaccineCount = covidInfo.getVaccineCount();
            covidCount = covidInfo.getCovidCount();
            lungCondition = covidInfo.getLungCondition();
        }

        TestResultForCentralDB processedTestResult = new TestResultForCentralDB(
                appointment.getPatientName(),
                appointment.getEmail(),
                testResult.getPatient_info().getSex(),
                testResult.getPatient_info().getState(),
                testResult.getPatient_info().getCity(),
                testResult.getPatient_info().getZipcode(),
                testResult.getPatient_info().getDate_of_birth(),
                daysSick,
                haveFever,
                temperature,
                vaccineCount,
                covidCount,
                lungCondition,
                testResult.getTestId(),
                testResult.getType(),
                appointment.getLocation(),
                dateString,
                test_result
        );

        HttpStatusCode statusCode = centralDBService.sendData(processedTestResult);
        if (statusCode != HttpStatus.OK) {
            throw new FailedSaveToCentralDBException();
        }
    }

    public void addPatientInfo(EnterPatientInfoRequest request) throws NotFoundException {
        Optional<TestResult> testResultOptional = testResultRepository.findById(request.test_id());
        if (testResultOptional.isPresent()) {
            TestResult testResult = testResultOptional.get();
            PatientInfo patientInfo = new PatientInfo(
                    testResult.getPatient_info().getEmail(),
                    request.sex(),
                    request.date_of_birth(),
                    request.state(),
                    request.city(),
                    request.zipcode()
            );
            testResult.setPatient_info(patientInfo);
            testResultRepository.save(testResult);

        } else {
            throw new NotFoundException();
        }
    }

    public void addMedicalInfo(EnterMedicalInfoRequest request) throws NotFoundException, MedicalInfoTypeMismatchException {
        Optional<TestResult> testResultOptional = testResultRepository.findById(request.getTestId());
        if (testResultOptional.isPresent()) {
            TestResult testResult = testResultOptional.get();

            MedicalInfoDTO medicalInfoDTO = request.getMedicalInfo();
            MedicalInfo medicalInfo;
            // Use instanceof to check the type and cast accordingly
            if (medicalInfoDTO instanceof CovidMedicalInfoDTO && testResult.getType().equals("covid")) {
                medicalInfo = new CovidMedicalInfo(
                        "PENDING",
                        ((CovidMedicalInfoDTO) medicalInfoDTO).getVaccineCount(),
                        ((CovidMedicalInfoDTO) medicalInfoDTO).getCovidCount(),
                        ((CovidMedicalInfoDTO) medicalInfoDTO).isLungCondition()
                );
            } else if (medicalInfoDTO instanceof FluMedicalInfoDTO && testResult.getType().equals("flu")) {
                medicalInfo = new FluMedicalInfo(
                        "PENDING",
                        ((FluMedicalInfoDTO) medicalInfoDTO).getDaysSick(),
                        ((FluMedicalInfoDTO) medicalInfoDTO).isHaveFever(),
                        ((FluMedicalInfoDTO) medicalInfoDTO).getTemperature()
                );
            } else {
                throw new MedicalInfoTypeMismatchException();
            }
            testResult.setMedical_info(medicalInfo);

            testResultRepository.save(testResult);

        } else {
            throw new NotFoundException();
        }
    }

    public void addTestResult(EnterTestResultRequest request) throws NotFoundException, FailedSaveToCentralDBException {
        Optional<TestResult> testResultOptional = testResultRepository.findById(request.test_id());

        if (testResultOptional.isPresent()) {
            TestResult testResult = testResultOptional.get();
            MedicalInfo medicalInfo = testResult.getMedical_info();

            if (medicalInfo instanceof CovidMedicalInfo covidMedicalInfo) {
                covidMedicalInfo.setTestResult(request.test_results());
                testResult.setMedical_info(covidMedicalInfo);
            } else {
                FluMedicalInfo fluMedicalInfo = (FluMedicalInfo)medicalInfo;
                fluMedicalInfo.setTestResult(request.test_results());
                testResult.setMedical_info(fluMedicalInfo);
            }
            testResultRepository.save(testResult);

            // Send the data to centralDB service
            pushTestResultToCentralDB(testResult);

        } else {
            throw new NotFoundException();
        }
    }
}
