package com.spring.sample.b2c.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

/**
 * Security Configuration.
 */
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
  String jwkSetUri;

  @Value("${spring.security.oauth2.resourceserver.jwt.valid-audience}")
  String validateAudience;

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer}")
  String issuer;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests((requests) -> requests.anyRequest().authenticated())
        .oauth2ResourceServer()
        .jwt();
  }

  @Bean
  JwtDecoder jwtDecoder() {
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
    jwtDecoder.setJwtValidator(createDefaultWithIssuer());
    return jwtDecoder;
  }

  public OAuth2TokenValidator<Jwt> createDefaultWithIssuer() {
    List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
    validators.add(new JwtTimestampValidator());
    validators.add(new JwtIssuerValidator(issuer));
    validators.add(new DelegatingOAuth2TokenValidator<>(
            new JwtClaimValidator(JwtClaimNames.AUD, aud ->
                    aud != null && ((ArrayList) aud).contains(this.validateAudience))));
    return new DelegatingOAuth2TokenValidator<>(validators);
  }


}
