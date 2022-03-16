package com.azure.spring.sample.reactive.servlet.oauth2.login.jwt.azure.activedirectory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.client.endpoint.AbstractOAuth2AuthorizationGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class AzureActiveDirectoryJwtClientAuthenticationParametersConverter<T extends AbstractOAuth2AuthorizationGrantRequest>
    implements Converter<T, MultiValueMap<String, String>> {

    private final static Logger LOGGER =
        LoggerFactory.getLogger(AzureActiveDirectoryJwtClientAuthenticationParametersConverter.class);
    private static final String CLIENT_ASSERTION_TYPE_VALUE = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";

    private final Map<String, AzureActiveDirectoryCertificateSignedJwtAssertionFactory> factories;

    public AzureActiveDirectoryJwtClientAuthenticationParametersConverter(
        Map<String, AzureActiveDirectoryCertificateSignedJwtAssertionFactory> factories) {
        this.factories = factories;
    }

    @Override
    public MultiValueMap<String, String> convert(T authorizationGrantRequest) {
        Assert.notNull(authorizationGrantRequest, "authorizationGrantRequest cannot be null");

        ClientRegistration registration = authorizationGrantRequest.getClientRegistration();
        ClientAuthenticationMethod method = registration.getClientAuthenticationMethod();
        if (!ClientAuthenticationMethod.PRIVATE_KEY_JWT.equals(registration.getClientAuthenticationMethod())) {
            return null;
        }

        try {
            return createParameters(registration);
        } catch (AzureActiveDirectoryAssertionException exception) {
            LOGGER.error("Failed to create parameters.", exception);
        }
        return null;
    }

    private MultiValueMap<String, String> createParameters(ClientRegistration registration) throws AzureActiveDirectoryAssertionException {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.set(OAuth2ParameterNames.CLIENT_ASSERTION_TYPE, CLIENT_ASSERTION_TYPE_VALUE);
        parameters.set(OAuth2ParameterNames.CLIENT_ASSERTION, createAssertion(registration));
        return parameters;
    }

    private String createAssertion(ClientRegistration registration) throws AzureActiveDirectoryAssertionException {
        return factories.get(registration.getRegistrationId()).createJwtAssertion();
    }
}
