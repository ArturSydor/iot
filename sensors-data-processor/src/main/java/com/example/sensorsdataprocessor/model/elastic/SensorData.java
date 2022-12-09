package com.example.sensorsdataprocessor.model.elastic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class SensorData implements Document {

    private static final String INDEX_BASE_NAME = "sensors";

    private String id;
    private Long timestamp;
    private String tenantId;
    private String token;

    private Map<String, Object> extraFields = new HashMap<>();

    @Override
    public String baseIndexName() {
        return INDEX_BASE_NAME;
    }
}
