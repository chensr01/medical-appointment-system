package org.jrm.healthAdmin.requests;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class MedicalInfoDeserializer extends JsonDeserializer<MedicalInfoDTO> {

    @Override
    public MedicalInfoDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode root = jp.readValueAsTree();

        // Determine the type of the medical info
        String type = root.get("type").asText();
        JsonNode medicalInfoNode = root;

        // Deserialize the medical info based on its type
        if ("flu".equalsIgnoreCase(type)) {
            return mapper.treeToValue(medicalInfoNode, FluMedicalInfoDTO.class);
        } else if ("covid".equalsIgnoreCase(type)) {
            return mapper.treeToValue(medicalInfoNode, CovidMedicalInfoDTO.class);
        } else {
            throw new IOException("Unsupported type: " + type);
        }
    }
}