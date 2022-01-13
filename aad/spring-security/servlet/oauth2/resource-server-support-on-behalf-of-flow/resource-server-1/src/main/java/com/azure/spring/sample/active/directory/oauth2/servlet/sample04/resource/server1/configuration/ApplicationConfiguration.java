package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server1.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.JwtBearerOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.endpoint.DefaultJwtBearerTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Configuration
public class ApplicationConfiguration {

    private final OAuth2ResourceServerProperties.Jwt properties;

    public ApplicationConfiguration(OAuth2ResourceServerProperties properties) {
        this.properties = properties.getJwt();
    }

    @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
    String audience;

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withJwkSetUri(properties.getJwkSetUri()).build();
        nimbusJwtDecoder.setJwtValidator(jwtValidator());
        return nimbusJwtDecoder;
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
        ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientRepository authorizedClientRepository) {
        OAuth2AuthorizedClientProvider authorizedClientProvider =
            OAuth2AuthorizedClientProviderBuilder.builder()
                                                 .provider(jwtBearerOAuth2AuthorizedClientProvider())
                                                 .build();
        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
            new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

    @Bean
    public WebClient webClient(ClientRegistrationRepository clientRegistrationRepository,
                                      OAuth2AuthorizedClientRepository authorizedClientRepository) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction function =
            new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                authorizedClientRepository);
        return WebClient.builder()
                        .apply(function.oauth2Configuration())
                        .build();
    }

    private JwtBearerOAuth2AuthorizedClientProvider jwtBearerOAuth2AuthorizedClientProvider() {
        JwtBearerOAuth2AuthorizedClientProvider provider = new JwtBearerOAuth2AuthorizedClientProvider();
        provider.setAccessTokenResponseClient(oAuth2AccessTokenResponseClient());
        return provider;
    }

    private DefaultJwtBearerTokenResponseClient oAuth2AccessTokenResponseClient() {
        DefaultJwtBearerTokenResponseClient client = new DefaultJwtBearerTokenResponseClient();
        client.setRequestEntityConverter(new AzureADJwtBearerGrantRequestEntityConverter());
        return client;
    }

    private OAuth2TokenValidator<Jwt> jwtValidator() {
        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
        String issuerUri = properties.getIssuerUri();
        if (StringUtils.hasText(issuerUri)) {
            validators.add(new JwtIssuerValidator(issuerUri));
        }
        if (StringUtils.hasText(audience)) {
            validators.add(new JwtClaimValidator<>(JwtClaimNames.AUD, audiencePredicate(audience)));
        }
        validators.add(new JwtTimestampValidator());
        return new DelegatingOAuth2TokenValidator<>(validators);
    }

    Predicate<Object> audiencePredicate(String audience) {
        return aud -> {
            if (aud == null) {
                return false;
            } else if (aud instanceof String) {
                return aud.equals(audience);
            } else if (aud instanceof List) {
                return ((List<?>) aud).contains(audience);
            } else {
                return false;
            }
        };
    }

}
