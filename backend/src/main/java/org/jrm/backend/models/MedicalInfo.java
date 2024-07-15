package org.jrm.backend.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = FluInfo.class, name = "flu"),
//        @JsonSubTypes.Type(value = CovidInfo.class, name = "covid")
//})
public interface MedicalInfo {
}