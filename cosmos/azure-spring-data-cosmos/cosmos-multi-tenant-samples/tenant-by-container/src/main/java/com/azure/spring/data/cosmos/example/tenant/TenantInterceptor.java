package com.azure.spring.data.cosmos.example.tenant;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

@Component
public class TenantInterceptor implements WebRequestInterceptor {

    // Capture a particular http request header to be used as the tenant identifier.
    // We are defining the header as "TenantId"
    private static final String TENANT_HEADER = "TenantId";
    private static final String TENANT_TIER_HEADER = "TenantTier";

    @Override
    public void preHandle(WebRequest request) {
        TenantStorage.setCurrentTenant(request.getHeader(TENANT_HEADER));
        TenantStorage.setCurrentTenantTier(request.getHeader(TENANT_TIER_HEADER));
    }

    @Override
    public void postHandle(WebRequest webRequest, ModelMap modelMap) {
        TenantStorage.clear();
    }

    @Override
    public void afterCompletion(WebRequest webRequest, Exception e) {

    }
}
