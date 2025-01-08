// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.security;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer;
import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadWebApplicationHttpSecurityConfigurer;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class AadWebApplicationAndResourceServerConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain apiWebFilterChain(HttpSecurity http) throws Exception {
        http.with(AadResourceServerHttpSecurityConfigurer.aadResourceServer(), Customizer.withDefaults())
            .securityMatcher("/api/**").authorizeHttpRequests(requests -> requests
                .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public SecurityFilterChain htmlWebFilterChain(HttpSecurity http) throws Exception {
        http.with(AadWebApplicationHttpSecurityConfigurer.aadWebApplication(), Customizer.withDefaults())
            .authorizeHttpRequests(requests -> requests
            .requestMatchers("/login").permitAll()
            .anyRequest().authenticated());

        return http.build();
    }

    /**
     * This method is only used for Microsoft Entra conditional access support and can be removed if this feature is not
     * used. {@inheritDoc}
     *
     * @return the conditional access filter
     */
    @Bean
    public Filter conditionalAccessFilter() {
        return new AadConditionalAccessFilter();
    }
}
