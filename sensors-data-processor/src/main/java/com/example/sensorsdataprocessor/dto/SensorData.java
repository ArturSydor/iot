package com.example.sensorsdataprocessor.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorData {

    private String id;
    private Long timestamp;
    private String tenantId;
    private String token;

    private Map<String, Object> extraFields = new HashMap<>();
}
