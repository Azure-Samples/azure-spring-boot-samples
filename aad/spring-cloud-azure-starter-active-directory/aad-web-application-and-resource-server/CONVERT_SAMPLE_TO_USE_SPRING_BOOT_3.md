# 1 Convert sample to use Spring Boot 3

## 1.1 Update your infra

- [JDK17](https://www.oracle.com/java/technologies/downloads/).

## 1.2 Update the pom parent

Update the pom parent file:

Current for Spring Boot 2:
   ```xml
   <parent>
     <groupId>com.azure.spring</groupId>
     <artifactId>azure-spring-boot-samples-4.x</artifactId>
     <version>1.0.0</version>
     <relativePath>../../../azure-spring-boot-samples-4.x-pom.xml</relativePath>
   </parent>
   ```

Updated for Spring Boot 3 to the following:
   ```xml
   <parent>
     <groupId>com.azure.spring</groupId>
     <artifactId>azure-spring-boot-samples-6.x</artifactId>
     <version>1.0.0</version>
     <relativePath>../../../azure-spring-boot-samples-6.x-pom.xml</relativePath>
   </parent>
   ```

If you want to build from the root pom file *azure-spring-boot-samples-6.x-pom.xml*, add this sample as a submodule like below:

   ```xml
   <modules>
     <module>aad/spring-cloud-spring-cloud-azure-starter-active-directory/aad-web-application-and-resource-server</module>
   </modules>
   ```

## 1.3 Update dependencies

- Update thymeleaf extras dependency for Spring Security.

  Current package path for Spring Boot 2:

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

## 1.3 Update Java code

- Update class path for class `com.azure.spring.sample.aad.security.AadConditionalAccessFilter`.

  Current package path for Spring Boot 2:

    ```java
    import com.azure.spring.cloud.autoconfigure.aad.AadClientRegistrationRepository;
    import javax.servlet.FilterChain;
    import javax.servlet.ServletException;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    ```

  Updated for Spring Boot 3 to the following:

    ```java
    import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadClientRegistrationRepository;
    import jakarta.servlet.FilterChain;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    ```
  
- Update code for the class `com.azure.spring.sample.aad.security.AadWebApplicationAndResourceServerConfig`.

    Current for Spring Boot 2:

    ```java
    import com.azure.spring.cloud.autoconfigure.aad.AadResourceServerWebSecurityConfigurerAdapter;
    import com.azure.spring.cloud.autoconfigure.aad.AadWebSecurityConfigurerAdapter;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.annotation.Order;
    import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    
    import javax.servlet.Filter;
    
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class AadWebApplicationAndResourceServerConfig {
    
        @Order(1)
        @Configuration
        public static class ApiWebSecurityConfigurationAdapter extends AadResourceServerWebSecurityConfigurerAdapter {
            protected void configure(HttpSecurity http) throws Exception {
                super.configure(http);
                http.antMatcher("/api/**")
                    .authorizeRequests().anyRequest().authenticated();
            }
        }
    
        @Configuration
        public static class HtmlWebSecurityConfigurerAdapter extends AadWebSecurityConfigurerAdapter {
    
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
    }
    ```
    
    Updated for Spring Boot 3 to the following:
    
    ```java
    import jakarta.servlet.Filter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.core.annotation.Order;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.web.SecurityFilterChain;
    
    import static com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadResourceServerHttpSecurityConfigurer.aadResourceServer;
    import static com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadWebApplicationHttpSecurityConfigurer.aadWebApplication;
    
    @Configuration(proxyBeanMethods = false)
    @EnableWebSecurity
    @EnableMethodSecurity
    public class AadWebApplicationAndResourceServerConfig {
    
        @Order(1)
        @Configuration
        public static class ApiHttpSecurityConfiguration {
            @Bean
            public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
                http
                    .apply(aadResourceServer())
                    .and()
                    .securityMatcher("/api/**")
                    .authorizeHttpRequests()
                    .anyRequest().authenticated();
                return http.build();
            }
        }
    
        @Configuration
        public static class HtmlHttpSecurityConfiguration {
    
            @Bean
            public SecurityFilterChain htmlFilterChain(HttpSecurity http) throws Exception {
                // @formatter:off
                http
                    .apply(aadWebApplication())
                    .conditionalAccessFilter(conditionalAccessFilter())
                    .and()
                    .authorizeHttpRequests()
                    .requestMatchers("/login").permitAll()
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
    ```
