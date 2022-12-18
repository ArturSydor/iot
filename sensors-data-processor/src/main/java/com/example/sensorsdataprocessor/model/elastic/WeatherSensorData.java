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
public class WeatherSensorData extends SensorData {

    private static final String INDEX_BASE_NAME = "weather";

    private BigDecimal airTemperature;
    private BigDecimal wetBulbTemperature;
    private BigDecimal humidity;
    private BigDecimal rainIntensity;
    private BigDecimal intervalRain;
    private BigDecimal totalRain;
    private BigDecimal windDirection;
    private BigDecimal windSpeed;
    private BigDecimal maxWindSpeed;
    private BigDecimal barometricPressure;
    private BigDecimal solarRadiation;

    @Override
    public String baseIndexName() {
        return INDEX_BASE_NAME;
    }
}
