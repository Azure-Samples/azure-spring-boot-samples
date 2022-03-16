package com.azure.spring.sample.reactive.servlet.oauth2.login.jwt.configuration;

import org.junit.jupiter.api.Test;

import static com.azure.spring.sample.reactive.servlet.oauth2.login.jwt.configuration.WebSecurityConfiguration.getTenantIdFromIssuerUri;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebSecurityConfigurationTest {

    @Test
    public void getTenantFromIssuerUriTest() {
        String tenantId = getTenantIdFromIssuerUri("https://login.microsoftonline.com/aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa/v2.0");
        assertEquals("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaa", tenantId);

        tenantId = getTenantIdFromIssuerUri("https://login.microsoftonline.com/11111111-1111-1111-1111-11111111/v2.0");
        assertEquals("11111111-1111-1111-1111-11111111", tenantId);
    }
}
