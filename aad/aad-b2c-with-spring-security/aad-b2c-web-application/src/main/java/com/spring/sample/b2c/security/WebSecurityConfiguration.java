// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.spring.sample.b2c.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests()
                    .regexMatchers("/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("${your-tenant-authorization-server-base-uri}/oauth2/v2.0/logout?post_logout_redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Flogin&p=${your-sign-up-or-sign-in-flow-name}")
                    .and()
                .oauth2Login();
        // @formatter:off
    }
}
