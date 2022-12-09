package com.example.sensorsdataprocessor.controller;

import com.example.sensorsdataprocessor.dto.SensorDto;
import com.example.sensorsdataprocessor.mapper.SensorMapper;
import com.example.sensorsdataprocessor.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService service;

    private final SensorMapper sensorMapper;

    @GetMapping("/{id}")
    public SensorDto getOneById(@PathVariable String id) {
        return sensorMapper.toDto(service.getOneById(id));
    }

    @GetMapping
    public List<SensorDto> getAll() {
        return service.getAll().stream()
                .map(sensorMapper::toDto)
                .toList();
    }

    @PutMapping
    public SensorDto createOrUpdate(@Valid @RequestBody SensorDto sensorDto) {
        var sensor = sensorMapper.toModel(sensorDto);
        return sensorMapper.toDto(service.createOrUpdate(sensor));
    }

}
