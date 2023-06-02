package com.example.sensorsdataprocessor.service;

import co.elastic.clients.elasticsearch.ml.Anomaly;
import com.example.sensorsdataprocessor.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifierService {

    private final SensorDataAnomalyService sensorDataAnomalyService;

    private final JavaMailSender mailSender;

    public void notifyUser(NotificationDto notification) {
        log.info("Processing notification: {}", notification);

        var anomalies = sensorDataAnomalyService.getAnomalies(notification)
                .stream()
                .map(this::anomalyToString)
                .collect(Collectors.joining("\n"));

        var email = new SimpleMailMessage();
        email.setFrom("sensor.management@gmail.com");
        email.setTo(notification.email());
        email.setSubject("Anomalies for job " + notification.jobId());
        email.setText("Anomalies:\n" + anomalies);

        mailSender.send(email);
    }

    private String anomalyToString(Anomaly anomaly) {
        return """
                Severity: %s, Date: %s, Typical: %s, Actual: %s;
                """
                .formatted(
                        anomaly.recordScore(),
                        new Date(anomaly.timestamp()),
                        anomaly.typical().get(0),
                        anomaly.actual().get(0)
                );
    }

}
