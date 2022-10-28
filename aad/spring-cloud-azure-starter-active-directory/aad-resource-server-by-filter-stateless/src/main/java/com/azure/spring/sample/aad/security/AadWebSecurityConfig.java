// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.security;

import com.azure.spring.cloud.autoconfigure.aad.filter.AadAppRoleStatelessAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AadWebSecurityConfig {

    @Autowired
    private AadAppRoleStatelessAuthenticationFilter aadAuthFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);

        http.authorizeRequests()
            .antMatchers("/admin/**").hasRole("Admin")
            .antMatchers("/", "/index.html", "/public").permitAll()
            .anyRequest().authenticated();

        http.addFilterBefore(aadAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
