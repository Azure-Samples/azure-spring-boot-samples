package com.azure.sample.active.directory.resource.server.configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .oauth2ResourceServer()
                .jwt()
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                    .and()
                .and();
        // @formatter:on
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthoritiesConverter(claimToAuthorityPrefixMap()));
        return converter;
    }

    private Map<String, String> claimToAuthorityPrefixMap() {
        Map<String, String> claimToAuthorityPrefixMap = new HashMap<>();
        claimToAuthorityPrefixMap.put("scope", "SCOPE_");
        claimToAuthorityPrefixMap.put("scp", "SCOPE_");
        claimToAuthorityPrefixMap.put("roles", "ROLE_");
        return claimToAuthorityPrefixMap;
    }
}
