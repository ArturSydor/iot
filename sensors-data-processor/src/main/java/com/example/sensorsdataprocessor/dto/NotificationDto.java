package com.example.sensorsdataprocessor.dto;

import com.example.sensorsdataprocessor.model.mongo.Notification;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

public record NotificationDto(
        @NotBlank String email,
        @NotBlank String jobId,
        double severity,
        OffsetDateTime anomalyRangeFrom,
        OffsetDateTime anomalyRangeTo
) {

    public Notification toEntity() {
        var newNotification = new Notification();
        newNotification.setEmail(email);
        newNotification.setJobId(jobId);
        newNotification.setSeverity(severity);
        return newNotification;
    }

}
