package org.jrm.healthAdmin.configs;

import org.jrm.healthAdmin.models.*;
import org.jrm.healthAdmin.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
class LoadStorage {

    private static final Logger log = LoggerFactory.getLogger(LoadStorage.class);

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    CommandLineRunner createDummyTestResults(TestResultRepository repository) {
        // todo: for testing purposes only. Remove this method in production.
        List<TestResult> testResults = new ArrayList<>();
        var uuid = UUID.fromString("DFEE3590-A44D-4D57-B27E-B76346ACD832");
        var type = "covid";
        var medicalInfo = new CovidMedicalInfo("Positive", 1, 1, true);
        var patientInfo = new PatientInfo("Bilbo Baggins", "male", "2001-08-26", "PA", "Pittsburgh", "15213");
        testResults.add(new TestResult(uuid, type, medicalInfo, patientInfo));

        return args -> {
            for (var result : testResults) {
                log.info("Preloading " + repository.save(result));
            }
        };
    }
}