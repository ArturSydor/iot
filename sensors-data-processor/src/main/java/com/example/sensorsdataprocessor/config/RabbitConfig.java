package com.example.sensorsdataprocessor.config;

import com.example.sensorsdataprocessor.model.elastic.SensorData;
import com.example.sensorsdataprocessor.service.ElasticService;
import com.example.sensorsdataprocessor.service.SensorService;
import com.example.sensorsdataprocessor.tenant.TenantHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    private final ElasticService elasticService;

    private final SensorService sensorService;

    @Bean
    public Consumer<SensorData> sensorDataConsumer() {
        return event -> {
            log.info("Received an event: {}", event);
            TenantHolder.put(event.getTenantId());
            if (sensorService.isSensorTokenValid(event.getToken())) {
                elasticService.save(event);
            } else {
                log.error("Invalid sensor data for entry: {}", event);
            }
            TenantHolder.clear();
        };
    }

    @Bean
    public Queue externalJavaClientQueue() {
        return new Queue("sensor-consumer");
    }

    @Bean
    public Exchange messageExchangeFanout() {
        return new TopicExchange("amq.topic");
    }

    @Bean
    public Binding springQueueBinding(Queue localSpringQueue, Exchange messageExchangeFanout) {
        return BindingBuilder
                .bind(localSpringQueue)
                .to(messageExchangeFanout)
                .with("")
                .noargs();
    }

}

