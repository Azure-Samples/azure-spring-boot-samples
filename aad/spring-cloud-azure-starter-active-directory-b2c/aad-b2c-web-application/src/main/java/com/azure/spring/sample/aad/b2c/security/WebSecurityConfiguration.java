// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.b2c.security;

import com.azure.spring.cloud.autoconfigure.aadb2c.AadB2cOidcLoginConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class WebSecurityConfiguration {

    private final AadB2cOidcLoginConfigurer configurer;

    public WebSecurityConfiguration(AadB2cOidcLoginConfigurer configurer) {
        this.configurer = configurer;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .apply(configurer);
        // @formatter:on
        return http.build();
    }
}
