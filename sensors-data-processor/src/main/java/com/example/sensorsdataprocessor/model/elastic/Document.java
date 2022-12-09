package com.example.sensorsdataprocessor.model.elastic;

import java.util.Objects;

public interface Document {

    default String indexName(String tenantId) {
        Objects.requireNonNull(tenantId);
        return String.format("iot_%s_%s", tenantId, baseIndexName());
    }

    String baseIndexName();

}
