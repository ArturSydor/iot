package com.example.sensorsdataprocessor.controller;

import com.example.sensorsdataprocessor.dto.NotificationDto;
import com.example.sensorsdataprocessor.model.mongo.Notification;
import com.example.sensorsdataprocessor.repository.NotificationRepository;
import com.example.sensorsdataprocessor.service.NotifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotifierService notifierService;
    private final NotificationRepository notificationRepository;


    @GetMapping
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    @PostMapping("/direct")
    public void notifyUser(@Valid @RequestBody NotificationDto notificationDto) {
        if (Objects.isNull(notificationDto.anomalyRangeFrom()) || Objects.isNull(notificationDto.anomalyRangeTo())) {
            log.error("For direct user notification anomalyRangeFrom and anomalyRangeTo are required.");
            return;
        }
        notifierService.notifyUser(notificationDto);
    }

    @PostMapping
    public Notification create(@Valid @RequestBody NotificationDto notificationDto) {
        log.info("Saving new notification: {}", notificationDto);
        if (notificationRepository.findByEmailAndJobId(notificationDto.email(), notificationDto.jobId()).isEmpty()) {
            return notificationRepository.save(notificationDto.toEntity());
        } else {
            throw new RuntimeException(String.format("Notification already exists for email=%s and jobId=%s",
                    notificationDto.email(), notificationDto.jobId()));
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        notificationRepository.deleteById(id);
    }

}
