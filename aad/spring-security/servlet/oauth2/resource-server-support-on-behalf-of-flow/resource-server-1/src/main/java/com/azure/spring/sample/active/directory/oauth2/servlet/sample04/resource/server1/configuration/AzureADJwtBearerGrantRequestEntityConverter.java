package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server1.configuration;

import org.springframework.security.oauth2.client.endpoint.JwtBearerGrantRequest;
import org.springframework.security.oauth2.client.endpoint.JwtBearerGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

public class AzureADJwtBearerGrantRequestEntityConverter extends JwtBearerGrantRequestEntityConverter {

    @Override
    protected MultiValueMap<String, String> createParameters(JwtBearerGrantRequest jwtBearerGrantRequest) {
        MultiValueMap<String, String> parameters = super.createParameters(jwtBearerGrantRequest);
        parameters.add("requested_token_use", "on_behalf_of");
        return parameters;
    }

}

