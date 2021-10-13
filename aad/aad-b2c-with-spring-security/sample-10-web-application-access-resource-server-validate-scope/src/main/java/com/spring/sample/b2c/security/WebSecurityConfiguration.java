package com.spring.sample.b2c.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security Configuration.
 */
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Value("${logout-success-url}")
  private String logoutSuccessUrl;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
          .authorizeRequests()
              .anyRequest().authenticated()
              .and()
          .logout()
              .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
              .logoutSuccessUrl(this.logoutSuccessUrl)
              .and()
          .oauth2Login();
    // @formatter:off
  }
}
