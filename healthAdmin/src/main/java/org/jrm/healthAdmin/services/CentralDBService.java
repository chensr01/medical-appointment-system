package org.jrm.healthAdmin.services;

import org.jrm.healthAdmin.models.TestResult;
import org.jrm.healthAdmin.models.TestResultForCentralDB;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;


public interface CentralDBService {
    HttpStatusCode sendData(TestResultForCentralDB data);
}
