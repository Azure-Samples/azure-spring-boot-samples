// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.security;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static com.azure.spring.cloud.autoconfigure.aad.AadResourceServerHttpSecurityConfigurer.aadResourceServer;
import static com.azure.spring.cloud.autoconfigure.aad.AadWebApplicationHttpSecurityConfigurer.aadWebApplication;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AadWebApplicationAndResourceServerConfig {

    @Order(1)
    @Configuration
    public static class ApiHttpSecurityConfigurationAdapter {
        @Bean
        public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
            http
                .apply(aadResourceServer())
                    .and()
                .antMatcher("/api/**")
                .authorizeRequests().anyRequest().authenticated();
            return http.build();
        }
    }

    @Configuration
    public static class HtmlHttpSecurityConfigurerAdapter {

        @Bean
        public SecurityFilterChain htmlFilterChain(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .apply(aadWebApplication())
                    .conditionalAccessFilter(conditionalAccessFilter())
                    .and()
                .authorizeRequests()
                    .antMatchers("/login").permitAll()
                    .anyRequest().authenticated();
            // @formatter:on
            return http.build();
        }

        /**
         * This method is only used for AAD conditional access support and can be removed if this feature is not used.
         * {@inheritDoc}
         * @return the conditional access filter
         */
        private Filter conditionalAccessFilter() {
            return new AadConditionalAccessFilter();
        }
    }
}
