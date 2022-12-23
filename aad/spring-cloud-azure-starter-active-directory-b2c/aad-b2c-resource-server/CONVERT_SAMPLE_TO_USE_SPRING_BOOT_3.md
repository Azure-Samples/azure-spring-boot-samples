# 1 Convert sample to use Spring Boot 3

## 1.1 Update your infra

- [JDK17](https://www.oracle.com/java/technologies/downloads/).

## 1.2 Update Java code

- Update code for the class `com.azure.spring.sample.aad.b2c.security.WebSecurityConfiguration`.

    Current for Spring Boot 2:

    ```java
    import com.azure.spring.cloud.autoconfigure.aad.AadJwtBearerTokenAuthenticationConverter;
    import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests((requests) -> requests.anyRequest().authenticated())
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(new AadJwtBearerTokenAuthenticationConverter());
        }
    }
    ```
    
    Updated for Spring Boot 3 to the following:
    
    ```java
    import com.azure.spring.cloud.autoconfigure.aad.implementation.jwt.AadJwtGrantedAuthoritiesConverter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
    import org.springframework.security.web.SecurityFilterChain;
    
    @Configuration(proxyBeanMethods = false)
    @EnableWebSecurity(debug = true)
    @EnableMethodSecurity
    public class WebSecurityConfiguration {
    
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            // @formatter:off
            JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
            jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new AadJwtGrantedAuthoritiesConverter());
            http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter);
            // @formatter:on
            return http.build();
        }
    }
    ```
