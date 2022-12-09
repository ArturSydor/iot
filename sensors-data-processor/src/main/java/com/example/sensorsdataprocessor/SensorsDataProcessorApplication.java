package com.example.sensorsdataprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.sensorsdataprocessor.config.properties")
public class SensorsDataProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SensorsDataProcessorApplication.class, args);
    }

}
