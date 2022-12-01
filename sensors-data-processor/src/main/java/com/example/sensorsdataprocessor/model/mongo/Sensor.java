package com.example.sensorsdataprocessor.model.mongo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@Document("sensors")
public class Sensor {

    @Id
    private String id;

    @Length(min = 2)
    @NotBlank
    private String tenantId;

    @NotBlank
    private String token;

    @DecimalMin("-90")
    @DecimalMax("90")
    private BigDecimal latitude;

    @DecimalMin("-180")
    @DecimalMax("180")
    private BigDecimal longitude;

    private String name;

    private String description;

}
