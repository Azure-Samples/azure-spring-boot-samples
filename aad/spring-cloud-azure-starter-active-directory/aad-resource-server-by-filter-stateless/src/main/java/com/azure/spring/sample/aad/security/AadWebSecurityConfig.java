// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.security;

import com.azure.spring.cloud.autoconfigure.implementation.aad.filter.AadAppRoleStatelessAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class AadWebSecurityConfig {

    @Autowired
    private AadAppRoleStatelessAuthenticationFilter aadAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(CsrfConfigurer::disable)
            .sessionManagement(configurer ->
                configurer.sessionCreationPolicy(SessionCreationPolicy.NEVER))
            .authorizeHttpRequests(request -> request
                .requestMatchers("/admin/**").hasRole("Admin")
                .requestMatchers("/", "/index.html", "/public").permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(aadAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
