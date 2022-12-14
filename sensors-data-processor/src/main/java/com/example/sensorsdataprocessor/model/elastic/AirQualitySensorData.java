package com.example.sensorsdataprocessor.model.elastic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
public class AirQualitySensorData extends SensorData {

    private static final String INDEX_BASE_NAME = "air_quality";

    private BigDecimal dust;

    @Override
    public String baseIndexName() {
        return INDEX_BASE_NAME;
    }
}
