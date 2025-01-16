// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.security;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadWebApplicationHttpSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.Filter;

@Profile("conditional-access")
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class AadWebApplicationConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.with(AadWebApplicationHttpSecurityConfigurer.aadWebApplication(), Customizer.withDefaults())
            .authorizeHttpRequests(requests -> requests
            .requestMatchers("/login").permitAll()
            .anyRequest().authenticated());

        return http.build();
    }

    /**
     * This method is only used for Microsoft Entra conditional access support and can be removed if this feature is not used.
     * {@inheritDoc}
     * @return the conditional access filter
     */
    @Bean
    public Filter conditionalAccessFilter() {
        return new AadConditionalAccessFilter();
    }
}
