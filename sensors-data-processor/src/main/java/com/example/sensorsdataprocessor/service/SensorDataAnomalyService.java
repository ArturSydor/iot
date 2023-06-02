package com.example.sensorsdataprocessor.service;

import co.elastic.clients.elasticsearch.ml.Anomaly;
import co.elastic.clients.elasticsearch.ml.ElasticsearchMlClient;
import co.elastic.clients.elasticsearch.ml.GetRecordsRequest;
import co.elastic.clients.util.DateTime;
import com.example.sensorsdataprocessor.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorDataAnomalyService {

    private final ElasticsearchMlClient mlClient;

    public List<Anomaly> getAnomalies(NotificationDto notificationDto) {
        var request = new GetRecordsRequest.Builder()
                .jobId(notificationDto.jobId())
                .start(DateTime.of(notificationDto.anomalyRangeFrom().format(DateTimeFormatter.ISO_DATE_TIME)))
                .end(DateTime.of(notificationDto.anomalyRangeTo().format(DateTimeFormatter.ISO_DATE_TIME)))
                .build();

        try {
            log.info("GetRecords request: {}", request);
            var response = mlClient.getRecords(request);
            log.info("GetRecords response: {}", response);

            return response.records()
                    .stream()
                    .filter(anomaly -> anomaly.recordScore() >= notificationDto.severity())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error when getting anomalies from elastic.", e);
            throw new RuntimeException("Error when getting anomalies from elastic");
        }
    }

}
