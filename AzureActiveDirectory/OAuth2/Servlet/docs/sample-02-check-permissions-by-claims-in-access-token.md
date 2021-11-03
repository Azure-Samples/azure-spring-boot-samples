- [1. About](#1-about)
- [2. Create sample applications](#2-create-sample-applications)
    * [2.1. Client](#21-client)
        + [2.1.1. pom.xml](#211-pomxml)
        + [2.1.2. Java classes](#212-java-classes)
            - [2.1.2.1. ClientApplication.java](#2121-clientapplicationjava)
            - [2.1.2.2. WebSecurityConfiguration.java](#2122-websecurityconfigurationjava)
            - [2.1.2.3. ApplicationConfiguration.java](#2123-applicationconfigurationjava)
            - [2.1.2.4. HomeController.java](#2124-homecontrollerjava)
            - [2.1.2.5. CheckPermissionByScopeController.java](#2125-checkpermissionbyscopecontrollerjava)
            - [2.1.2.6. ResourceServerController.java](#2126-resourceservercontrollerjava)
        + [2.1.3. application.yml](#213-applicationyml)
    * [2.2. Resource server](#22-resource-server)
        + [2.2.1. pom.xml](#221-pomxml)
        + [2.2.2. Java classes](#222-java-classes)
            - [2.2.2.1. ResourceServerApplication.java](#2221-resourceserverapplicationjava)
            - [2.2.2.2. WebSecurityConfiguration.java](#2222-websecurityconfigurationjava)
            - [2.2.2.3. ApplicationConfiguration.java](#2223-applicationconfigurationjava)
            - [2.2.2.4. HomeController.java](#2224-homecontrollerjava)
            - [2.2.2.5. CheckPermissionByScopeController.java](#2225-checkpermissionbyscopecontrollerjava)
            - [2.2.2.6. CheckPermissionByRoleController.java](#2226-checkpermissionbyrolecontrollerjava)
        + [2.2.3. application.yml](#223-applicationyml)
- [3. Create resources in Azure](#3-create-resources-in-azure)
    * [3.1. Create a tenant](#31-create-a-tenant)
    * [3.2. Add a new user](#32-add-a-new-user)
    * [3.3. Register client-1](#33-register-client-1)
    * [3.4. Add a client secret for client-1](#34-add-a-client-secret-for-client-1)
    * [3.5. Add a redirect URI for client-1](#35-add-a-redirect-uri-for-client-1)
    * [3.6. Register resource-server-1](#36-register-resource-server-1)
    * [3.7. Expose apis for resource-server-1](#37-expose-apis-for-resource-server-1)
    * [3.8. Create roles for resource-server-1](#38-create-roles-for-resource-server-1)
    * [3.9. Assign user-1 to resource-server-1-role-1](#39-assign-user-1-to-resource-server-1-role-1)
- [4. Run sample applications](#4-run-sample-applications)
- [5. Homework](#5-homework)







# 1. About

Please refer to [Azure Active Directory OAuth2 samples] to get all samples about using [Azure Active Directory] and [OAuth2] to protect web application developed by [Spring Security].

This sample will demonstrate this scenario:
1. User sign in client and client get [access token] by [OAuth 2.0 authorization code flow].
2. Client check whether current signed-in user has permission to specific method.
3. Client access resource-server by [access token].
4. Resource server validate the [access token] by validating the signature, and checking these claims: **aud**, **nbf** and **exp**.
5. Resource server check whether current signed-in user has permission to specific method.

# 2. Create sample applications
You can follow the following steps to create sample applications, or you can use samples in GitHub: [sample-02-check-permissions-by-claims-in-access-token].

## 2.1. Client

### 2.1.1. pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.5.4</version>
    <relativePath/>
  </parent>

  <groupId>com.azure.spring.sample.active.directory.oauth2</groupId>
  <artifactId>sample-02-client</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-oauth2-client</artifactId>
    </dependency>
  </dependencies>

</project>
```

### 2.1.2. Java classes

#### 2.1.2.1. ClientApplication.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
```

#### 2.1.2.2. WebSecurityConfiguration.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.client.configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .oauth2Login()
                .and();
        // @formatter:on
    }
}
```

#### 2.1.2.3. ApplicationConfiguration.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public static WebClient webClient(ClientRegistrationRepository clientRegistrationRepository,
                                      OAuth2AuthorizedClientRepository authorizedClientRepository) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction function =
            new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                authorizedClientRepository);
        return WebClient.builder()
                        .apply(function.oauth2Configuration())
                        .build();
    }
}
```

#### 2.1.2.4. HomeController.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, this is sample-02-client.";
    }
}
```

#### 2.1.2.5. CheckPermissionByScopeController.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.client.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class CheckPermissionByScopeController {

    private final WebClient webClient;

    public CheckPermissionByScopeController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/scope/resource-server-1-scope-1")
    @PreAuthorize("hasAuthority('SCOPE_api://<resource-server-1-client-id>/resource-server-1.scope-1')")
    public String resourceServer1Scope1(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
        return webClient
            .get()
            .uri("http://localhost:8081/scope/resource-server-1-scope-1")
            .attributes(oauth2AuthorizedClient(client1ResourceServer1))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    @GetMapping("/scope/resource-server-1-scope-2")
    @PreAuthorize("hasAuthority('SCOPE_api://<resource-server-1-client-id>/resource-server-1.scope-2')")
    public String resourceServer1Scope2(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
        return webClient
            .get()
            .uri("http://localhost:8081/scope/resource-server-1-scope-2")
            .attributes(oauth2AuthorizedClient(client1ResourceServer1))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
```

#### 2.1.2.6. ResourceServerController.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.client.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class ResourceServerController {

    private final WebClient webClient;

    public ResourceServerController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/resource-server-1")
    public String hello(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
        return webClient
            .get()
            .uri("http://localhost:8081/")
            .attributes(oauth2AuthorizedClient(client1ResourceServer1))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    @GetMapping("/resource-server-1/scope/resource-server-1-scope-1")
    public String resourceServer1Scope1(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
        return webClient
            .get()
            .uri("http://localhost:8081/scope/resource-server-1-scope-1")
            .attributes(oauth2AuthorizedClient(client1ResourceServer1))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    @GetMapping("/resource-server-1/scope/resource-server-1-scope-2")
    public String resourceServer1Scope2(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
        return webClient
            .get()
            .uri("http://localhost:8081/scope/resource-server-1-scope-2")
            .attributes(oauth2AuthorizedClient(client1ResourceServer1))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    @GetMapping("/resource-server-1/role/resource-server-1-role-1")
    public String resourceServer1Role1(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
        return webClient
            .get()
            .uri("http://localhost:8081/role/resource-server-1-role-1")
            .attributes(oauth2AuthorizedClient(client1ResourceServer1))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    @GetMapping("/resource-server-1/role/resource-server-1-role-2")
    public String resourceServer1Role2(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
        return webClient
            .get()
            .uri("http://localhost:8081/role/resource-server-1-role-2")
            .attributes(oauth2AuthorizedClient(client1ResourceServer1))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
```

### 2.1.3. application.yml
```yaml
# Please fill these placeholders before run this application:
# 1. <tenant-id>
# 2. <client-1-client-id>
# 3. <client-1-client-secret>
# 4. <resource-server-1-client-id>

server:
  port: 8080
spring:
  security:
    oauth2:
      client:
        provider: # Refs: https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2login-common-oauth2-provider
          azure-active-directory:
            issuer-uri: https://login.microsoftonline.com/<tenant-id>/v2.0 # Refs: https://docs.spring.io/spring-security/site/docs/current/reference/html5/#webflux-oauth2-login-openid-provider-configuration
            user-name-attribute: name
        registration:
          client-1-resource-server-1:
            provider: azure-active-directory
            client-id: <client-1-client-id>
            client-secret: <client-1-client-secret>
            scope: openid, profile, offline_access, api://<resource-server-1-client-id>/resource-server-1.scope-1, # Refs: https://docs.microsoft.com/azure/active-directory/develop/v2-permissions-and-consent#openid-connect-scopes
            redirect-uri: http://localhost:8080/login/oauth2/code/
```

## 2.2. Resource server

### 2.2.1. pom.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.5.4</version>
    <relativePath/>
  </parent>

  <groupId>com.azure.spring.sample.active.directory.oauth2</groupId>
  <artifactId>sample-02-resource-server</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>
  </dependencies>

</project>
```

### 2.2.2. Java classes

#### 2.2.2.1. ResourceServerApplication.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.resource.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }
}
```

#### 2.2.2.2. WebSecurityConfiguration.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.resource.server.configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.DelegatingJwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;

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
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return converter;
    }

    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter scp = new JwtGrantedAuthoritiesConverter();
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("roles");
        roles.setAuthorityPrefix("ROLE_");
        return new DelegatingJwtGrantedAuthoritiesConverter(scp, roles);
    }
}
```

#### 2.2.2.3. ApplicationConfiguration.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.resource.server.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Configuration
public class ApplicationConfiguration {

    private final OAuth2ResourceServerProperties.Jwt properties;

    public ApplicationConfiguration(OAuth2ResourceServerProperties properties) {
        this.properties = properties.getJwt();
    }

    @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
    String audience;

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withJwkSetUri(properties.getJwkSetUri()).build();
        nimbusJwtDecoder.setJwtValidator(jwtValidator());
        return nimbusJwtDecoder;
    }

    private OAuth2TokenValidator<Jwt> jwtValidator() {
        List<OAuth2TokenValidator<Jwt>> validators = new ArrayList<>();
        String issuerUri = properties.getIssuerUri();
        if (StringUtils.hasText(issuerUri)) {
            validators.add(new JwtIssuerValidator(issuerUri));
        }
        if (StringUtils.hasText(audience)) {
            validators.add(new JwtClaimValidator<>(JwtClaimNames.AUD, audiencePredicate(audience)));
        }
        validators.add(new JwtTimestampValidator());
        return new DelegatingOAuth2TokenValidator<>(validators);
    }

    Predicate<Object> audiencePredicate(String audience) {
        return aud -> {
            if (aud == null) {
                return false;
            } else if (aud instanceof String) {
                return aud.equals(audience);
            } else if (aud instanceof List) {
                return ((List<?>) aud).contains(audience);
            } else {
                return false;
            }
        };
    }

}
```

#### 2.2.2.4. HomeController.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.resource.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, this is resource-server-1.";
    }
}
```

#### 2.2.2.5. CheckPermissionByScopeController.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.resource.server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckPermissionByScopeController {

    @GetMapping("/scope/resource-server-1-scope-1")
    @PreAuthorize("hasAuthority('SCOPE_resource-server-1.scope-1')")
    public String resourceServer1Scope1() {
        return "Hi, this is resource-server-1. You can access my endpoint: /scope/resource-server-1-scope-1";
    }

    @GetMapping("/scope/resource-server-1-scope-2")
    @PreAuthorize("hasAuthority('SCOPE_resource-server-1.scope-2')")
    public String resourceServer1Scope2() {
        return "Hi, this is resource-server-1. You can access my endpoint: /scope/resource-server-1-scope-2";
    }
}
```

#### 2.2.2.6. CheckPermissionByRoleController.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample02.resource.server.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckPermissionByRoleController {

    @GetMapping("/role/resource-server-1-role-1")
    @PreAuthorize("hasAuthority('ROLE_resource-server-1.role-1')")
    public String resourceServer1Role1() {
        return "Hi, this is resource-server-1. You can access my endpoint: /role/resource-server-1-role-1";
    }

    @GetMapping("/role/resource-server-1-role-2")
    @PreAuthorize("hasAuthority('ROLE_resource-server-1.role-2')")
    public String resourceServer1Role2() {
        return "Hi, this is resource-server-1. You can access my endpoint: /role/resource-server-1-role-2";
    }
}
```

### 2.2.3. application.yml
```yaml
# Please fill these placeholders before run this application:
# 1. <tenant-id>
# 2. <resource-server-1-client-id>

server:
  port: 8081
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: https://login.microsoftonline.com/<tenant-id>/discovery/v2.0/keys
          issuer-uri: https://login.microsoftonline.com/<tenant-id>/v2.0
          audience: <resource-server-1-client-id>
```

# 3. Create resources in Azure

## 3.1. Create a tenant
Read [document about creating an Azure AD tenant], create a new tenant. Get the tenant-id: **<tenant-id>**.

## 3.2. Add a new user
Read [document about adding users], add a new user: **user-1@<tenant-name>.com**. Get the user's password.

## 3.3. Register client-1
Read [document about registering an application], register an application named **client-1**. Get the client-id: **<client-1-client-id>**.

## 3.4. Add a client secret for client-1
Read [document about adding a client secret], add a client secret. Get the client-secret value: **<client-1-client-secret>**.

## 3.5. Add a redirect URI for client-1
Read [document about adding a redirect URI], add redirect URI: **http://localhost:8080/login/oauth2/code/**.

## 3.6. Register resource-server-1
Read [document about registering an application], register an application named **resource-server-1**. Get the client-id: **<resource-server-1-client-id>**.

## 3.7. Expose apis for resource-server-1
Read [document about exposing an api], expose 2 scopes for resource-server-1: **resource-server-1.scope-1** and **resource-server-1.scope-2**, choose **Admins and users** for **Who can consent** option.

## 3.8. Create roles for resource-server-1
Read [document about declaring roles for an application], create 2 roles for resource-server-1: **resource-server-1-role-1** and **resource-server-1-role-2**.

## 3.9. Assign user-1 to resource-server-1-role-1
Read [document about assigning users and groups to roles], assign **user-1** to **resource-server-2-role-1**.

# 4. Run sample applications
1. Fill these placeholders in **application.yml** and **CheckPermissionByScopeController.java**, then run [sample-02-client].
2. Fill these placeholders in **application.yml**, then run [sample-02-resource-server].
3. Open browser(for example: [Edge]), close all [InPrivate window], and open a new [InPrivate window].
4. Access **http://localhost:8080**, it will redirect to Microsoft login page. Input username and password, it will return permission request page. click **Accept**, then it will return **Hello, this is sample-02-client.**. This means we log in successfully.
5. Access **http://localhost:8080/scope/resource-server-1-scope-1**, it will return **Hi, this is resource-server-1. You can access my endpoint: /scope/resource-server-1-scope-1**.
6. Access **http://localhost:8080/scope/resource-server-1-scope-2**, it will return 403, which means user do not have authority to access this endpoint.
7. Access **http://localhost:8080/resource-server-1**, it will return **Hello, this is resource-server-1.**, which means [sample-02-client] can access [sample-02-resource-server].
8. Access **http://localhost:8080/resource-server-1/scope/resource-server-1-scope-1**, it will return **Hi, this is resource-server-1. You can access my endpoint: /scope/resource-server-1-scope-1**.
9. Access **http://localhost:8080/resource-server-1/scope/resource-server-1-scope-2**, it will return 500. Check the log of [sample-02-client], it show that [sample-02-resource-server] returned 403 to [sample-02-client]. which means [sample-02-client] doesn't have authority to access this endpoint of [sample-02-resource-server].
10. Access **http://localhost:8080/resource-server-1/role/resource-server-1-role-1**, it will return **Hi, this is resource-server-1. You can access my endpoint: /role/resource-server-1-role-1**.
11. Access **http://localhost:8080/resource-server-1/role/resource-server-1-role-2**, it will return 500. Check the log of [sample-02-client], it show that [sample-02-resource-server] returned 403 to [sample-02-client]. which means [sample-02-client] doesn't have authority to access this endpoint of [sample-02-resource-server].

# 5. Homework
1. Try to use groups for authorization.
2. List all claims that can be used for authorization.




[Azure Active Directory OAuth2 samples]: ../README.md
[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[OAuth2]: https://oauth.net/2/
[Spring Security]: https://spring.io/projects/spring-security
[OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[access token]: https://docs.microsoft.com/en-us/azure/active-directory/develop/access-tokens
[sample-02-check-permissions-by-claims-in-access-token]: ../sample-02-check-permissions-by-claims-in-access-token
[document about creating an Azure AD tenant]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-create-new-tenant#create-a-new-azure-ad-tenant
[document about registering an application]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app
[document about adding users]: https://docs.microsoft.com/azure/active-directory/fundamentals/add-users-azure-active-directory
[document about adding a client secret]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-client-secret
[document about adding a redirect URI]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-redirect-uri
[document about exposing an api]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-expose-web-apis
[document about configuring a client application to access a web API]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-access-web-apis
[document about assigning users and groups to roles]: https://docs.microsoft.com/azure/active-directory/develop/howto-add-app-roles-in-azure-ad-apps#assign-users-and-groups-to-roles
[document about declaring roles for an application]: https://docs.microsoft.com/azure/active-directory/develop/howto-add-app-roles-in-azure-ad-apps#declare-roles-for-an-application
[document about assigning users and groups to roles]: https://docs.microsoft.com/azure/active-directory/develop/howto-add-app-roles-in-azure-ad-apps#assign-users-and-groups-to-roles
[sample-02-client]: ../sample-02-check-permissions-by-claims-in-access-token/sample-02-client
[sample-02-resource-server]: ../sample-02-check-permissions-by-claims-in-access-token/sample-02-resource-server
[Edge]: https://www.microsoft.com/edge?r=1
[InPrivate window]: https://support.microsoft.com/microsoft-edge/browse-inprivate-in-microsoft-edge-cd2c9a48-0bc4-b98e-5e46-ac40c84e27e2
