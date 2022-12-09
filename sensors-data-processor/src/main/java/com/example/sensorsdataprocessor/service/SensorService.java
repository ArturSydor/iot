package com.example.sensorsdataprocessor.service;

import com.example.sensorsdataprocessor.model.mongo.Sensor;
import com.example.sensorsdataprocessor.repository.SensorRepository;
import com.example.sensorsdataprocessor.tenant.TenantHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository repository;

    public Sensor getOneById(String id) {
        return repository.findByTenantIdAndId(TenantHolder.get(), id)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
    }

    public List<Sensor> getAll() {
        return repository.findAllByTenantId(TenantHolder.get());
    }

    public boolean isSensorTokenValid(String token) {
        return repository.findByTenantIdAndToken(TenantHolder.get(), token).isPresent();
    }

    public Sensor createOrUpdate(Sensor sensor) {
        return repository.save(sensor);
    }

}
