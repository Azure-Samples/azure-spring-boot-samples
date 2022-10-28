// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static com.azure.spring.cloud.autoconfigure.aad.AadWebApplicationHttpSecurityConfigurer.aadWebApplication;


@Profile("conditional-access")
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AadWebApplicationConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .apply(aadWebApplication())
                .conditionalAccessFilter(new AadConditionalAccessFilter())
                .and()
            .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated();
        // @formatter:on
        return http.build();
    }
}
