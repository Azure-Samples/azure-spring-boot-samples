// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.security;

import com.azure.spring.cloud.autoconfigure.aad.AadWebSecurityConfigurerAdapter;
import com.azure.spring.cloud.autoconfigure.aad.implementation.oauth2.OAuth2ClientAuthenticationJwkResolver;
import com.azure.spring.cloud.autoconfigure.aad.properties.AadAuthenticationProperties;
import jakarta.servlet.Filter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;


@Profile("conditional-access")
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AadWebApplicationConfig {

    @Bean
    public SecurityFilterChain htmlFilterChain(AadAuthenticationProperties properties,
                                               HttpSecurity httpSecurity,
                                               ClientRegistrationRepository repo,
                                               RestTemplateBuilder restTemplateBuilder,
                                               OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService,
                                               ObjectProvider<OAuth2ClientAuthenticationJwkResolver> jwkResolvers) throws Exception {
        AadWebSecurityConfigurerAdapter configurerAdapter = new AadWebSecurityConfigurerAdapter(
            properties, httpSecurity, repo, restTemplateBuilder, oidcUserService, jwkResolvers) {
            @Override
            protected void configure(HttpSecurity http) throws Exception {
                super.configure(http);
                // @formatter:off
                http.authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .anyRequest().authenticated();
                // @formatter:on
            }

            /**
             * This method is only used for AAD conditional access support and can be removed if this feature is not used.
             * {@inheritDoc}
             * @return the conditional access filter
             */
            @Override
            protected Filter conditionalAccessFilter() {
                return new AadConditionalAccessFilter();
            }
        };
        return configurerAdapter.build();
    }
}
