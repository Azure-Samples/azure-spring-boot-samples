package com.azure.spring.sample.reactive.servlet.oauth2.login.jwt.configuration;

import com.azure.spring.sample.reactive.servlet.oauth2.login.jwt.azure.activedirectory.AzureActiveDirectoryAssertionException;
import com.azure.spring.sample.reactive.servlet.oauth2.login.jwt.azure.activedirectory.AzureActiveDirectoryCertificateSignedJwtAssertionFactory;
import com.azure.spring.sample.reactive.servlet.oauth2.login.jwt.azure.activedirectory.AzureActiveDirectoryJwtClientAuthenticationParametersConverter;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final Pattern ISSUER_URI_PATTERN = Pattern.compile("https://login.microsoftonline.com/(.*?)/v2.0");

    private final Environment environment;
    private final ClientRegistrationRepository repository;

    public WebSecurityConfiguration(Environment environment, ClientRegistrationRepository repository) {
        this.environment = environment;
        this.repository = repository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.oauth2Login()
                .tokenEndpoint()
                    .accessTokenResponseClient(accessTokenResponseClient(Collections.singletonList("client-1"), repository))
                    .and()
                .and()
            .authorizeRequests()
                .anyRequest().authenticated();
        // @formatter:off
    }

    private DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient(
        List<String> registrationIds, ClientRegistrationRepository repository
    ) throws AzureActiveDirectoryAssertionException {
        OAuth2AuthorizationCodeGrantRequestEntityConverter converter =
            new OAuth2AuthorizationCodeGrantRequestEntityConverter();
        converter.addParametersConverter(
            new AzureActiveDirectoryJwtClientAuthenticationParametersConverter<>(createFactoryMap(registrationIds, repository)));
        DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();
        client.setRequestEntityConverter(converter);
        return client;
    }

    private Map<String, AzureActiveDirectoryCertificateSignedJwtAssertionFactory> createFactoryMap(
        List<String> registrationIds, ClientRegistrationRepository repository
    ) throws AzureActiveDirectoryAssertionException {
        Map<String, AzureActiveDirectoryCertificateSignedJwtAssertionFactory> factories = new HashMap<>();
        for (String registrationId: registrationIds) {
            AzureActiveDirectoryCertificateSignedJwtAssertionFactory factory = createFactory(registrationId, repository);
            if (factory != null) {
                factories.put(registrationId, factory);
            }
        }
        return factories;
    }

    private AzureActiveDirectoryCertificateSignedJwtAssertionFactory createFactory(
        String registrationId, ClientRegistrationRepository repository
    ) throws AzureActiveDirectoryAssertionException {
        String clientCertificatePath = environment.getProperty(
            String.format("spring.security.oauth2.client.registration.%s.client-certificate-path", registrationId));
        if (!StringUtils.hasText(clientCertificatePath)) {
            return null;
        }
        String clientCertificatePassword = environment.getProperty(
            String.format("spring.security.oauth2.client.registration.%s.client-certificate-password", registrationId));
        if (!StringUtils.hasText(clientCertificatePassword)) {
            return null;
        }
        ClientRegistration registration = repository.findByRegistrationId(registrationId);
        String tenantId = getTenantIdFromIssuerUri(registration.getProviderDetails().getIssuerUri());
        String clientId = registration.getClientId();
        return new AzureActiveDirectoryCertificateSignedJwtAssertionFactory(
            clientCertificatePath, clientCertificatePassword, tenantId, clientId);
    }

    static String getTenantIdFromIssuerUri(String issuerUri) {
        Matcher matcher = ISSUER_URI_PATTERN.matcher(issuerUri);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
