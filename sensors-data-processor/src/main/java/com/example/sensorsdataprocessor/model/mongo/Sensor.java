package com.example.sensorsdataprocessor.model.mongo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Document("sensors")
@CompoundIndex(def = """
        {
            'tenantId': 1,
            'token': 1
        }
        """, unique = true)
public class Sensor {

    @Id
    private String id;

    @Length(min = 2)
    @NotBlank
    private String tenantId;

    @NotBlank
    private String token;

    @NotNull
    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    private BigDecimal latitude;

    @NotNull
    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    private BigDecimal longitude;

    private String name;

    private String description;

}
