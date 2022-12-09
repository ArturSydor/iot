package com.example.sensorsdataprocessor.tenant;

import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class TenantHolder {

    private static final ThreadLocal<String> tenantHolder = new ThreadLocal<>();

    public void put(String tenantId) {
        Objects.requireNonNull(tenantId);
        tenantHolder.set(tenantId);
    }

    public String get() {
        return tenantHolder.get();
    }

    public void clear() {
        tenantHolder.remove();
    }

}