package com.example.sensorsdataprocessor.job;

import com.example.sensorsdataprocessor.dto.NotificationDto;
import com.example.sensorsdataprocessor.model.mongo.Notification;
import com.example.sensorsdataprocessor.repository.NotificationRepository;
import com.example.sensorsdataprocessor.service.NotifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnomalyDailyJob {

    private final NotifierService notifierService;
    private final NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Execute at midnight (00:00:00) every day
    public void notifyAllUsers() {
        // TODO paging + jms queue
        notificationRepository.findAll()
                .parallelStream()
                .map(this::toDailyDto)
                .forEach(notifierService::notifyUser);
    }

    private NotificationDto toDailyDto(Notification notification) {
        return new NotificationDto(
                notification.getEmail(),
                notification.getJobId(),
                notification.getSeverity(),
                OffsetDateTime.now().minusDays(1L),
                OffsetDateTime.now()
        );
    }

}
