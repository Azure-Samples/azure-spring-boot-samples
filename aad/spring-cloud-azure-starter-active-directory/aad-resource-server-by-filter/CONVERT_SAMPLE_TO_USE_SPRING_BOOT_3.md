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
     <module>aad/spring-cloud-spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter</module>
   </modules>
   ```

## 1.3 Update Java code

- Update package path for class `com.azure.spring.sample.aad.controller.TodoListController`.

    Current class path for Spring Boot 2:

    ```java
    import com.azure.spring.cloud.autoconfigure.aad.filter.UserPrincipal;
    import com.azure.spring.cloud.autoconfigure.aad.properties.AadAuthenticationProperties;
    ```

    Updated for Spring Boot 3 to the following:

    ```java
    import com.azure.spring.cloud.autoconfigure.implementation.aad.configuration.properties.AadAuthenticationProperties;
    import com.azure.spring.cloud.autoconfigure.implementation.aad.filter.UserPrincipal;
    ```

- Update code for the class `com.azure.spring.sample.aad.security.WebSecurityConfig`.

    Current for Spring Boot 2:
    
    ```java
    import com.azure.spring.cloud.autoconfigure.aad.filter.AadAuthenticationFilter;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
    import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
    
    @EnableGlobalMethodSecurity(securedEnabled = true,
            prePostEnabled = true)
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
        @Autowired
        private AadAuthenticationFilter aadAuthFilter;
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
    
            http.authorizeRequests()
                    .antMatchers("/home").permitAll()
                    .antMatchers("/api/**").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .and()
                .addFilterBefore(aadAuthFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
    ```
    
    Updated for Spring Boot 3 to the following:
    
    ```java
    import com.azure.spring.cloud.autoconfigure.implementation.aad.filter.AadAuthenticationFilter;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
    
    @Configuration(proxyBeanMethods = false)
    @EnableMethodSecurity(securedEnabled = true)
    public class WebSecurityConfig {
    
        @Autowired
        private AadAuthenticationFilter aadAuthFilter;
    
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            // @formatter:off
            http.authorizeHttpRequests()
                    .requestMatchers("/home").permitAll()
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().permitAll()
                    .and()
                .csrf()
                    .disable()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .and()
                .addFilterBefore(aadAuthFilter, UsernamePasswordAuthenticationFilter.class);
            // @formatter:on
            return http.build();
        }
    }
    ```
