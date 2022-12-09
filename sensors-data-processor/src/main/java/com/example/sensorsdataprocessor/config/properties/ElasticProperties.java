package com.example.sensorsdataprocessor.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Getter
@Setter
@ConfigurationProperties(prefix = "elasticsearch")
@ConfigurationPropertiesScan
public class ElasticProperties {

    private String hostname;
    private int port;

}
