package org.jrm.healthAdmin.services;

import org.jrm.healthAdmin.models.TestResultForCentralDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@Profile("production")
public class ActualCentralDBService implements CentralDBService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public HttpStatusCode sendData(TestResultForCentralDB data) {
        System.out.println("Called Central DB Service");

        // Prepare the URL
        String url = "http://128.2.205.51/centraldb/api/testresult/create";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity
        HttpEntity<TestResultForCentralDB> request = new HttpEntity<>(data, headers);

        // Send the post request
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return responseEntity.getStatusCode();
    }
}
