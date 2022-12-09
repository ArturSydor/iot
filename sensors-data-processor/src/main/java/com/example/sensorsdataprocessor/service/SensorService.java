package com.example.sensorsdataprocessor.service;

import com.example.sensorsdataprocessor.model.mongo.Sensor;
import com.example.sensorsdataprocessor.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository repository;

    public Sensor getOneById(String tenantId, String id) {
        return repository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
    }

    public List<Sensor> getAll(String tenantId) {
        return repository.findAllByTenantId(tenantId);
    }

    public boolean isSensorTokenValid(String tenantId, String token) {
        return repository.findByTenantIdAndId(tenantId, token).isPresent();
    }

    public Sensor createOrUpdate(Sensor sensor) {
        return repository.save(sensor);
    }

}
