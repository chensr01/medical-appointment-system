package org.jrm.backend.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TestResponseDeserializer extends JsonDeserializer<TestResponse> {

    @Override
    public TestResponse deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode rootNode = mapper.readTree(jp);

        String type = rootNode.get("type").asText();
        MedicalInfo medicalInfo;
        if ("flu".equals(type)) {
            medicalInfo = mapper.treeToValue(rootNode.get("medical_info"), FluInfo.class);
        } else if ("covid".equals(type)) {
            medicalInfo = mapper.treeToValue(rootNode.get("medical_info"), CovidInfo.class);
        } else {
            throw new RuntimeException("Unrecognized type: " + type);
        }

        PatientInfo patientInfo = mapper.treeToValue(rootNode.get("patient_info"), PatientInfo.class);

        TestResponse testResponse = new TestResponse();
        testResponse.setType(type);
        testResponse.setMedicalInfo(medicalInfo);
        testResponse.setPatientInfo(patientInfo);

        return testResponse;
    }
}

