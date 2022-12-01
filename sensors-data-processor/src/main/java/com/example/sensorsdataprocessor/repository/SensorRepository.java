package com.example.sensorsdataprocessor.repository;

import com.example.sensorsdataprocessor.model.mongo.Sensor;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SensorRepository extends MongoRepository<Sensor, String> {

    Optional<Sensor> findByTenantIdAndId(String tenantId, String id);

    Optional<Sensor> findByTenantIdAndToken(String tenantId, String token);

    List<Sensor> findAllByTenantId(String tenantId);

}
