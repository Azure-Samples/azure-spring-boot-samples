// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.security;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Allows anonymous access to the client credentials demonstration endpoint while protecting other requests.
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity
public class AadResourceServerConfig {

    /**
     * Creates the resource server security filter chain.
     *
     * @param http the HTTP security builder
     * @return the configured security filter chain
     * @throws Exception if the security filter chain cannot be built
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.with(AadResourceServerHttpSecurityConfigurer.aadResourceServer(), Customizer.withDefaults())
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/webapiB/clientCredential").permitAll()
                .anyRequest().authenticated());
        return http.build();
    }
}
