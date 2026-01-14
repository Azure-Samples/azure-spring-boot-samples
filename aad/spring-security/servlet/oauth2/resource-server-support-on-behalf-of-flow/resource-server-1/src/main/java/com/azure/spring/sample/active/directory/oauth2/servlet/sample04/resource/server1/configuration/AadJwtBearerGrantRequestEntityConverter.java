package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server1.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.endpoint.DefaultOAuth2TokenRequestParametersConverter;
import org.springframework.security.oauth2.client.endpoint.JwtBearerGrantRequest;
import org.springframework.util.MultiValueMap;

public class AadJwtBearerGrantRequestEntityConverter
    implements Converter<JwtBearerGrantRequest, MultiValueMap<String, String>> {

    private final DefaultOAuth2TokenRequestParametersConverter<JwtBearerGrantRequest> delegate =
            new DefaultOAuth2TokenRequestParametersConverter<>();

    @Override
    public MultiValueMap<String, String> convert(JwtBearerGrantRequest source) {
        MultiValueMap<String, String> parameters = delegate.convert(source);
        parameters.add("requested_token_use", "on_behalf_of");
        return parameters;
    }
}
