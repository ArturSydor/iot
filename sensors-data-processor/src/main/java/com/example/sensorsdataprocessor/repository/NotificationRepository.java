package com.example.sensorsdataprocessor.repository;

import com.example.sensorsdataprocessor.model.mongo.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationRepository extends MongoRepository<Notification, String> {

    Optional<Notification> findByEmailAndJobId(String email, String jobId);

}
