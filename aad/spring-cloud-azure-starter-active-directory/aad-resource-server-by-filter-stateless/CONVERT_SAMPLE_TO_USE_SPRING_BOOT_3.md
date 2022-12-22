# 1 Convert sample to use Spring Boot 3

## 1.1 Update your infra

- [JDK17](https://www.oracle.com/java/technologies/downloads/).

## 1.2 Update Java code

- Update code for the class `com.azure.spring.sample.aad.security.AadWebSecurityConfig`.

    Current for Spring Boot 2:

    ```java
    import com.azure.spring.cloud.autoconfigure.aad.filter.AadAppRoleStatelessAuthenticationFilter;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class AadWebSecurityConfig extends WebSecurityConfigurerAdapter {
    
        @Autowired
        private AadAppRoleStatelessAuthenticationFilter aadAuthFilter;
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
    
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    
            http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("Admin")
                .antMatchers("/", "/index.html", "/public").permitAll()
                .anyRequest().authenticated();
    
            http.addFilterBefore(aadAuthFilter, UsernamePasswordAuthenticationFilter.class);
    
        }
    }
    ```
    
    Updated for Spring Boot 3 to the following:
    
    ```java
    import com.azure.spring.cloud.autoconfigure.implementation.aad.filter.AadAppRoleStatelessAuthenticationFilter;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    
    @Configuration(proxyBeanMethods = false)
    @EnableWebSecurity
    @EnableMethodSecurity
    public class AadWebSecurityConfig {
    
        @Autowired
        private AadAppRoleStatelessAuthenticationFilter aadAuthFilter;
    
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable();
    
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    
            http.authorizeHttpRequests()
                    .requestMatchers("/admin/**").hasRole("Admin")
                    .requestMatchers("/", "/index.html", "/public").permitAll()
                    .anyRequest().authenticated();
    
            http.addFilterBefore(aadAuthFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }
    }
    ```
