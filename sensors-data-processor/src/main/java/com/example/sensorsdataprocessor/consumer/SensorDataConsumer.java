package com.example.sensorsdataprocessor.consumer;

import com.example.sensorsdataprocessor.dto.SensorData;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SensorDataConsumer {

    @StreamListener(Processor.INPUT)
    public void consumeData(SensorData sensorData) {
        log.info("Received sensor data >>> {}", sensorData);
    }

}
