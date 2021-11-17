// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.config;

import com.azure.spring.cloud.autoconfigure.aad.implementation.webapi.AADResourceServerWebSecurityConfigurerAdapter;
import com.azure.spring.cloud.autoconfigure.aad.implementation.webapp.AADWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Order(1)
    @Configuration
    public static class ResourceServerSecurityConfiguration extends AADResourceServerWebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            http.antMatcher("/resource-server/**")
                .authorizeRequests()
                    .anyRequest().authenticated();
        }
    }

    @Configuration
    public static class ApplicationSecurityConfiguration extends AADWebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            super.configure(http);
            // @formatter:off
            http.authorizeRequests()
                    .anyRequest().authenticated();
            // @formatter:on
        }
    }
}
