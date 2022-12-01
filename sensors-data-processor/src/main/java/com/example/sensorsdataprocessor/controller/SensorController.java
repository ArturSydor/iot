package com.example.sensorsdataprocessor.controller;

import com.example.sensorsdataprocessor.model.mongo.Sensor;
import com.example.sensorsdataprocessor.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService service;

    @GetMapping("/{id}")
    public Sensor getOneById(@Header("X-Tenant") String tenantId, @PathVariable String id) {
        return service.getOneById(tenantId, id);
    }

    @GetMapping
    public List<Sensor> getAll(@Header("X-Tenant") String tenantId) {
        return service.getAll(tenantId);
    }

    @PutMapping
    public Sensor createOrUpdate(@Valid @RequestBody Sensor sensor) {
        return service.createOrUpdate(sensor);
    }

}
