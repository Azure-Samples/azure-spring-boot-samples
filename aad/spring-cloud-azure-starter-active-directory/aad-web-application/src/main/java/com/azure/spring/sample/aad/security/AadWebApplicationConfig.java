// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.security;

import com.azure.spring.cloud.autoconfigure.aad.AadWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.servlet.Filter;

@Profile("conditional-access")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AadWebApplicationConfig extends AadWebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // @formatter:off
        http.authorizeRequests()
            .antMatchers("/login").permitAll()
            .anyRequest().authenticated();
        // @formatter:on
    }

    /**
     * This method is only used for AAD conditional access support and can be removed if this feature is not used.
     * {@inheritDoc}
     * @return the conditional access filter
     */
    @Override
    protected Filter conditionalAccessFilter() {
        return new AadConditionalAccessFilter();
    }
}
