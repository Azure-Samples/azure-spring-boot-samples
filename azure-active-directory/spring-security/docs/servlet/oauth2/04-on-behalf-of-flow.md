- [1. About](#1-about)
- [2. Create sample applications](#2-create-sample-applications)
    * [2.1. Client](#21-client)
        + [2.1.1. pom.xml](#211-pomxml)
        + [2.1.2. Java classes](#212-java-classes)
            - [2.1.2.1. ClientApplication.java](#2121-clientapplicationjava)
            - [2.1.2.2. WebSecurityConfiguration.java](#2122-websecurityconfigurationjava)
            - [2.1.2.3. ApplicationConfiguration.java](#2123-applicationconfigurationjava)
            - [2.1.2.4. HomeController.java](#2124-homecontrollerjava)
            - [2.1.2.5. ResourceServer1Controller.java](#2125-resourceserver1controllerjava)
        + [2.1.3. application.yml](#213-applicationyml)
    * [2.2. Resource server 1](#22-resource-server-1)
        + [2.2.1. pom.xml](#221-pomxml)
        + [2.2.2. Java classes](#222-java-classes)
            - [2.2.2.1. ResourceServerApplication.java](#2221-resourceserverapplicationjava)
            - [2.2.2.2. WebSecurityConfiguration.java](#2222-websecurityconfigurationjava)
            - [2.2.2.3. AzureADJwtBearerGrantRequestEntityConverter.java](#2223-azureadjwtbearergrantrequestentityconverterjava)
            - [2.2.2.4. ApplicationConfiguration.java](#2224-applicationconfigurationjava)
            - [2.2.2.5. HomeController.java](#2225-homecontrollerjava)
            - [2.2.2.6. ResourceServer2Controller.java](#2226-resourceserver2controllerjava)
        + [2.2.3. application.yml](#223-applicationyml)
    * [2.3. Resource server 2](#23-resource-server-2)
        + [2.3.1. pom.xml](#231-pomxml)
        + [2.3.2. Java classes](#232-java-classes)
            - [2.3.2.1. ResourceServerApplication.java](#2321-resourceserverapplicationjava)
            - [2.3.2.2. WebSecurityConfiguration.java](#2322-websecurityconfigurationjava)
            - [2.3.2.3. ApplicationConfiguration.java](#2323-applicationconfigurationjava)
            - [2.3.2.4. HomeController.java](#2324-homecontrollerjava)
        + [2.3.3. application.yml](#233-applicationyml)
- [3. Create resources in Azure](#3-create-resources-in-azure)
    * [3.1. Create a tenant](#31-create-a-tenant)
    * [3.2. Add a new user](#32-add-a-new-user)
    * [3.3. Register client-1](#33-register-client-1)
    * [3.4. Add a client secret for client-1](#34-add-a-client-secret-for-client-1)
    * [3.5. Add a redirect URI for client-1](#35-add-a-redirect-uri-for-client-1)
    * [3.6. Register resource-server-1](#36-register-resource-server-1)
    * [3.7. Add a client secret for resource-server-1](#37-add-a-client-secret-for-resource-server-1)
    * [3.8. Add a redirect URI for resource-server-1](#38-add-a-redirect-uri-for-resource-server-1)
    * [3.9. Expose apis for resource-server-1](#39-expose-apis-for-resource-server-1)
    * [3.10. Register resource-server-2](#310-register-resource-server-2)
    * [3.11. Expose apis for resource-server-2](#311-expose-apis-for-resource-server-2)
    * [3.12. Authorize resource-server-1 to access resource-server-2](#312-authorize-resource-server-1-to-access-resource-server-2)
- [4. Run sample applications](#4-run-sample-applications)
- [5. Homework](#5-homework)







# 1. About

This sample will demonstrate this scenario:
- User sign in client and client get [access token] by [OAuth 2.0 authorization code flow].
- Client access resource-server-1 by [access token].
- resource-server-1 validate the [access token] by validating the signature, and checking these claims: `aud`, `nbf` and `exp`.
- resource-server-1 use the access token to get a new access token by [on behalf of flow]. 
- resource-server-1 use the new access token to access resource-server-2.
- resource-server-2 validate the [access token] by validating the signature, and checking these claims: `aud`, `nbf` and `exp`.

# 2. Create sample applications
You can follow the following steps to create sample applications, or you can use samples in GitHub: [04-on-behalf-of-flow].

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

  <groupId>com.azure.spring</groupId>
  <artifactId>azure-active-directory-spring-security-servlet-oauth2-04-client</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId> <!-- Require this because this project used WebClient。 -->
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
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.client;

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
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.client.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
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
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.client.configuration;

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
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, this is client-1.";
    }
}
```

#### 2.1.2.5. ResourceServer1Controller.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.client.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class ResourceServer1Controller {

    private final WebClient webClient;

    public ResourceServer1Controller(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/resource-server-1")
    public String hello(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
        return webClient
            .get()
            .uri("http://localhost:8081")
            .attributes(oauth2AuthorizedClient(client1ResourceServer1))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    @GetMapping("/resource-server-1/resource-server-2")
    public String resourceServer2hello(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
        return webClient
            .get()
            .uri("http://localhost:8081/resource-server-2")
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

## 2.2. Resource server 1

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

  <groupId>com.azure.spring</groupId>
  <artifactId>azure-active-directory-spring-security-servlet-oauth2-04-resource-server-1</artifactId>
  <version>1.0.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-webflux</artifactId> <!-- Require this because this project used WebClient。 -->
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-oauth2-client</artifactId>
    </dependency>
  </dependencies>

</project>
```

### 2.2.2. Java classes

#### 2.2.2.1. ResourceServerApplication.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server1;

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
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server1.configuration;

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
            .oauth2ResourceServer()
                .jwt()
                .and();
        // @formatter:on
    }
}
```

#### 2.2.2.3. AzureADJwtBearerGrantRequestEntityConverter.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server1.configuration;

import org.springframework.security.oauth2.client.endpoint.JwtBearerGrantRequest;
import org.springframework.security.oauth2.client.endpoint.JwtBearerGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

public class AzureADJwtBearerGrantRequestEntityConverter extends JwtBearerGrantRequestEntityConverter {

    @Override
    protected MultiValueMap<String, String> createParameters(JwtBearerGrantRequest jwtBearerGrantRequest) {
        MultiValueMap<String, String> parameters = super.createParameters(jwtBearerGrantRequest);
        parameters.add("requested_token_use", "on_behalf_of");
        return parameters;
    }

}
```

#### 2.2.2.4. ApplicationConfiguration.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server1.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.JwtBearerOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.endpoint.DefaultJwtBearerTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
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
import org.springframework.web.reactive.function.client.WebClient;

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

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
        ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientRepository authorizedClientRepository) {
        OAuth2AuthorizedClientProvider authorizedClientProvider =
            OAuth2AuthorizedClientProviderBuilder.builder()
                                                 .provider(jwtBearerOAuth2AuthorizedClientProvider())
                                                 .build();
        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
            new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

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

    private JwtBearerOAuth2AuthorizedClientProvider jwtBearerOAuth2AuthorizedClientProvider() {
        JwtBearerOAuth2AuthorizedClientProvider provider = new JwtBearerOAuth2AuthorizedClientProvider();
        provider.setAccessTokenResponseClient(oAuth2AccessTokenResponseClient());
        return provider;
    }

    private DefaultJwtBearerTokenResponseClient oAuth2AccessTokenResponseClient() {
        DefaultJwtBearerTokenResponseClient client = new DefaultJwtBearerTokenResponseClient();
        client.setRequestEntityConverter(new AzureADJwtBearerGrantRequestEntityConverter());
        return client;
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

#### 2.2.2.5. HomeController.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server1.controller;

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

#### 2.2.2.6. ResourceServer2Controller.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server1.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class ResourceServer2Controller {

    private final WebClient webClient;

    public ResourceServer2Controller(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/resource-server-2")
    public String resourceServer2(@RegisteredOAuth2AuthorizedClient("resource-server-1-resource-server-2")
                                      OAuth2AuthorizedClient resourceServer1ResourceServer2) {
        return webClient
            .get()
            .uri("http://localhost:8082/")
            .attributes(oauth2AuthorizedClient(resourceServer1ResourceServer2))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
```

### 2.2.3. application.yml
```yaml
# Please fill these placeholders before run this application:
# 1. <tenant-id>
# 2. <resource-server-1-client-id>
# 3. <resource-server-1-client-secret>
# 4. <resource-server-2-client-id>

server:
  port: 8081
spring:
  security:
    oauth2:
      client:
        provider:
          azure-active-directory:
            issuer-uri: https://login.microsoftonline.com/<tenant-id>/v2.0 # Refs: https://docs.spring.io/spring-security/site/docs/current/reference/html5/#webflux-oauth2-login-openid-provider-configuration
        registration:
          resource-server-1-resource-server-2:
            provider: azure-active-directory
            client-id: <resource-server-1-client-id>
            client-secret: <resource-server-1-client-secret>
            authorization-grant-type: urn:ietf:params:oauth:grant-type:jwt-bearer
            scope: api://<resource-server-2-client-id>/resource-server-2.scope-1
      resourceserver:
        jwt:
          jwk-set-uri: https://login.microsoftonline.com/<tenant-id>/discovery/v2.0/keys
          issuer-uri: https://login.microsoftonline.com/<tenant-id>/v2.0
          audience: <resource-server-1-client-id>
```

## 2.3. Resource server 2

### 2.3.1. pom.xml
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

  <groupId>com.azure.spring</groupId>
  <artifactId>azure-active-directory-spring-security-servlet-oauth2-04-resource-server-2</artifactId>
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

### 2.3.2. Java classes

#### 2.3.2.1. ResourceServerApplication.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }
}
```

#### 2.3.2.2. WebSecurityConfiguration.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server2.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .oauth2ResourceServer()
                .jwt()
                .and();
        // @formatter:on
    }
}
```

#### 2.3.2.3. ApplicationConfiguration.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server2.configuration;

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

    @Value("${spring.security.oauth2.resourceserver.jwt.audience}")
    String audience;

    private final OAuth2ResourceServerProperties.Jwt properties;

    public ApplicationConfiguration(OAuth2ResourceServerProperties properties) {
        this.properties = properties.getJwt();
    }

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

#### 2.3.2.4. HomeController.java
```java
package com.azure.spring.sample.active.directory.oauth2.servlet.sample04.resource.server2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, this is resource-server-2.";
    }
}
```

### 2.3.3. application.yml
```yaml
# Please fill these placeholders before run this application:
# 1. <tenant-id>
# 2. <resource-server-2-client-id>

server:
  port: 8082
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jwk-set-uri: https://login.microsoftonline.com/<tenant-id>/discovery/v2.0/keys
          issuer-uri: https://login.microsoftonline.com/<tenant-id>/v2.0
          audience: <resource-server-2-client-id>
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

## 3.7. Add a client secret for resource-server-1
Read [document about adding a client secret], add a client secret. Get the client-secret value: **<resource-server-1-client-secret>**.

## 3.8. Add a redirect URI for resource-server-1
Read [document about adding a redirect URI], add redirect URI: **http://localhost:8080/login/oauth2/code/**.

## 3.9. Expose apis for resource-server-1
Read [document about exposing an api], expose 2 scopes for resource-server-1: **resource-server-1.scope-1** and **resource-server-1.scope-2**, choose **Admins and users** for **Who can consent** option.

## 3.10. Register resource-server-2
Read [document about registering an application], register an application named **resource-server-2**. Get the client-id: **<resource-server-2-client-id>**.

## 3.11. Expose apis for resource-server-2
Read [document about exposing an api], expose 2 scopes for resource-server-2: **resource-server-2.scope-1** and **resource-server-2.scope-2**, choose **Admins and users** for **Who can consent** option.

## 3.12. Authorize resource-server-1 to access resource-server-2
Read [MS docs about exposing an api], pre-authorize resource-server-1 to access resource-server-2.


# 4. Run sample applications
1. Fill these placeholders in **application.yml**, then run [client].
2. Fill these placeholders in **application.yml**, then run [resource-server-1].
3. Fill these placeholders in **application.yml**, then run [resource-server-2].
4. Open browser(for example: [Edge]), close all [InPrivate window], and open a new [InPrivate window].
5. Access **http://localhost:8080**, it will return login page.
6. Input username and password, it will return **Hello, this is client-1.**, which means user log in successfully.
7. Access **http://localhost:8080/resource-server-1**, it will return **Hello, this is resource-server-1.**, which means [client] can access [resource-server-1].
8. Access **http://localhost:8080/resource-server-1/resource-server-2**, it will return **Hello, this is resource-server-2.**, which means [resource-server-1] can access [resource-server-2].

# 5. Homework





[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[OAuth2]: https://oauth.net/2/
[Spring Security]: https://spring.io/projects/spring-security
[OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[access token]: https://docs.microsoft.com/azure/active-directory/develop/access-tokens
[on behalf of flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-on-behalf-of-flow
[04-on-behalf-of-flow]: ../../../servlet/oauth2/04-on-behalf-of-flow
[document about creating an Azure AD tenant]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-create-new-tenant#create-a-new-azure-ad-tenant
[document about registering an application]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app
[document about adding users]: https://docs.microsoft.com/azure/active-directory/fundamentals/add-users-azure-active-directory
[document about adding a client secret]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-client-secret
[document about adding a redirect URI]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-redirect-uri
[document about exposing an api]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-expose-web-apis
[client]: ../../../servlet/oauth2/04-on-behalf-of-flow/client
[resource-server-1]: ../../../servlet/oauth2/04-on-behalf-of-flow/resource-server-1
[resource-server-2]: ../../../servlet/oauth2/04-on-behalf-of-flow/resource-server-2
[Edge]: https://www.microsoft.com/edge?r=1
[InPrivate window]: https://support.microsoft.com/microsoft-edge/browse-inprivate-in-microsoft-edge-cd2c9a48-0bc4-b98e-5e46-ac40c84e27e2


