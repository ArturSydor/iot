package com.example.sensorsdataprocessor.model.elastic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class SensorData implements Document {

    private static final String INDEX_BASE_NAME = "sensors";

    private String id;
    private Date timestamp;
    private String tenantId;
    private String token;

    @Override
    public String baseIndexName() {
        return INDEX_BASE_NAME;
    }
}
