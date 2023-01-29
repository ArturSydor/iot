package com.example.sensorsdataprocessor.model.elastic;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record Location(
        @NotNull
        @DecimalMin(value = "-90")
        @DecimalMax(value = "90")
        BigDecimal lat,
        @NotNull
        @DecimalMin(value = "-180")
        @DecimalMax(value = "180")
        BigDecimal lon
) {
}
