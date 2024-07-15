package org.jrm.healthAdmin.services;

import org.jrm.healthAdmin.models.TestResult;
import org.jrm.healthAdmin.models.TestResultForCentralDB;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
@Profile("mock")
public class MockCentralDBService implements CentralDBService {
    @Override
    public HttpStatusCode sendData(TestResultForCentralDB data) {
        // Simulated response for testing/development
        // Check if any of the required fields are null or empty
        if (isNullOrEmpty(data.getPatient_name()) ||
                isNullOrEmpty(data.getEmail()) ||
                isNullOrEmpty(data.getGender()) ||
                isNullOrEmpty(data.getState()) ||
                isNullOrEmpty(data.getCity()) ||
                isNullOrEmpty(data.getZipcode()) ||
                isNullOrEmpty(data.getDateOfBirth()) ||
                data.getTestId() == null ||
                isNullOrEmpty(data.getTestType()) ||
                isNullOrEmpty(data.getTestLocation()) ||
                isNullOrEmpty(data.getTestReportDate()) ||
                isNullOrEmpty(data.getTestResult())) {
            return HttpStatus.BAD_REQUEST;
        }

        // If all fields are properly filled
        return HttpStatus.OK;
    }

    // Utility method to check if a String is null or empty
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
