package com.example.sensorsdataprocessor.mapper;

import com.example.sensorsdataprocessor.dto.SensorDto;
import com.example.sensorsdataprocessor.model.mongo.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static com.example.sensorsdataprocessor.common.Constants.MAPPER_COMPONENT_MODEL;

@Mapper(componentModel = MAPPER_COMPONENT_MODEL)
public interface SensorMapper {

    SensorDto toDto(Sensor sensor);

    @Mapping(target = "tenantId",
            expression = "java(com.example.sensorsdataprocessor.tenant.TenantHolder.get())")
    Sensor toModel(SensorDto sensorDto);

}
