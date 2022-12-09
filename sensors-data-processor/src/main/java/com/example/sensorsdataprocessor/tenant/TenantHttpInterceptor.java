package com.example.sensorsdataprocessor.tenant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import static com.example.sensorsdataprocessor.common.Constants.TENANT;

@Slf4j
@Component
public class TenantHttpInterceptor implements WebRequestInterceptor {

    @Override
    public void preHandle(WebRequest request) {
        String tenantId = request.getHeader(TENANT);

        if (tenantId != null && !tenantId.isEmpty()) {
            TenantHolder.put(tenantId);
            log.debug("{} header get from request: {}", TENANT, tenantId);
        } else {
            String errorMessage = String.format("%s header not found.", TENANT);
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) {
        TenantHolder.clear();
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) {
        // Nothing needs to be done here.
    }
}
