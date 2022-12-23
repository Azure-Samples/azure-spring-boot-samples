# 1 Convert sample to use Spring Boot 3

## 1.1 Update your infra

- [JDK17](https://www.oracle.com/java/technologies/downloads/).

## 1.2 Update Java code

- Update code for the class `com.azure.spring.sample.aad.b2c.security.WebSecurityConfiguration`.

    Current for Spring Boot 2:

    ```java
    import com.azure.spring.cloud.autoconfigure.aadb2c.AadB2cOidcLoginConfigurer;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    
    @EnableWebSecurity
    public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
        private final AadB2cOidcLoginConfigurer configurer;
    
        public WebSecurityConfiguration(AadB2cOidcLoginConfigurer configurer) {
            this.configurer = configurer;
        }
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http.authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                .apply(configurer);
            // @formatter:on
        }
    }
    ```
    
    Updated for Spring Boot 3 to the following:
    
    ```java
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
            http.authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .apply(configurer);
            // @formatter:on
            return http.build();
        }
    }
    ```

## 1.3 Update dependencies

- Update `thymeleaf-extras-springsecurity` dependency in the `pom.xml`

  Current for Spring Boot 2:

    ```xml
    <dependency>
      <groupId>org.thymeleaf.extras</groupId>
      <artifactId>thymeleaf-extras-springsecurity5</artifactId>
    </dependency>
    ```

  Updated for Spring Boot 3 to the following:

    ```xml
    <dependency>
      <groupId>org.thymeleaf.extras</groupId>
      <artifactId>thymeleaf-extras-springsecurity6</artifactId>
    </dependency>
    ```