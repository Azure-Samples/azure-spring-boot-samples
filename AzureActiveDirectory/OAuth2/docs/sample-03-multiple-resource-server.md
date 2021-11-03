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
            - [2.1.2.6. ResourceServer2Controller.java](#2126-resourceserver2controllerjava)
            - [2.1.2.7. ResourceServerAllController.java](#2127-resourceserverallcontrollerjava)
        + [2.1.3. application.yml](#213-applicationyml)
    * [2.2. Resource server 1](#22-resource-server-1)
        + [2.2.1. pom.xml](#221-pomxml)
        + [2.2.2. Java classes](#222-java-classes)
        + [2.2.2.1. ResourceServerApplication.java](#2221-resourceserverapplicationjava)
            - [2.2.2.2. WebSecurityConfiguration.java](#2222-websecurityconfigurationjava)
            - [2.2.2.3. ApplicationConfiguration.java](#2223-applicationconfigurationjava)
            - [2.2.2.4. HomeController.java](#2224-homecontrollerjava)
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
- [4. Run sample applications](#4-run-sample-applications)
- [5. Homework](#5-homework)




# 1. About

Please refer to [Azure Active Directory OAuth2 samples] to get all samples about using [Azure Active Directory] and [OAuth2] to protect web application developed by [Spring Security].

In [Azure Active Directory]'s [access token], the **aud** claim only have one value, not a list. Which means one [access token] can only been used for one resource server. And one **OAuth2AuthorizedClient** can only hold one access token. So, if one client want to access multiple resource servers, it must configure multiple **ClientRegistration**s.

This sample will demonstrate this scenario:
- One client application access multiple resource servers.
- Consent scopes when request for specific endpoint.
- Consent all scopes when request for a specific endpoint.

# 2. Create sample applications
You can follow the following steps to create sample applications, or you can use samples in GitHub: [sample-03-multiple-resource-server].

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
  <artifactId>sample-03-client</artifactId>
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
package com.azure.spring.sample.active.directory.oauth2.sample03.client;

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
package com.azure.spring.sample.active.directory.oauth2.sample03.client.configuration;

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
package com.azure.spring.sample.active.directory.oauth2.sample03.client.configuration;

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
package com.azure.spring.sample.active.directory.oauth2.sample03.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, this is sample-03-client.";
    }
}
```

#### 2.1.2.5. ResourceServer1Controller.java
```java
package com.azure.spring.sample.active.directory.oauth2.sample03.client.controller;

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

    @GetMapping("/resource-server-1/hello")
    public String hello(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
        return webClient
            .get()
            .uri("http://localhost:8081")
            .attributes(oauth2AuthorizedClient(client1ResourceServer1))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
```

#### 2.1.2.6. ResourceServer2Controller.java
```java
package com.azure.spring.sample.active.directory.oauth2.sample03.client.controller;

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

    @GetMapping("/resource-server-2/hello")
    public String hello(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-2") OAuth2AuthorizedClient client1ResourceServer2) {
        return webClient
            .get()
            .uri("http://localhost:8081")
            .attributes(oauth2AuthorizedClient(client1ResourceServer2))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
```

#### 2.1.2.7. ResourceServerAllController.java
```java
package com.azure.spring.sample.active.directory.oauth2.sample03.client.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceServerAllController {

    @GetMapping("/client/resource-server-all/hello")
    public String resourceServerAll(
        @RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1,
        @RegisteredOAuth2AuthorizedClient("client-1-resource-server-2") OAuth2AuthorizedClient client1ResourceServer2) {
        return "Hi, this is client-1. You can see this response means you already consented the permissions "
            + "configured for client registration. "
            + "Scopes in client1ResourceServer1: " + client1ResourceServer1.getAccessToken().getScopes()
            + "Scopes in client1ResourceServer2: " + client1ResourceServer2.getAccessToken().getScopes();
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
# 5. <resource-server-2-client-id>

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
            client-name: client-1
            client-id: <client-1-client-id>
            client-secret: <client-1-client-secret>
            scope: openid, profile, api://<resource-server-1-client-id>/resource-server-1.scope-1 # Refs: https://docs.microsoft.com/azure/active-directory/develop/v2-permissions-and-consent#openid-connect-scopes
            redirect-uri: http://localhost:8080/login/oauth2/code/
          client-1-resource-server-2:
            provider: azure-active-directory
            client-name: client-1-resource-server-2
            client-id: <client-1-client-id>
            client-secret: <client-1-client-secret>
            scope: openid, profile, api://<resource-server-2-client-id>/resource-server-2.scope-1 # Refs: https://docs.microsoft.com/azure/active-directory/develop/v2-permissions-and-consent#openid-connect-scopes
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

  <groupId>com.azure.spring.sample.active.directory.oauth2</groupId>
  <artifactId>sample-03-resource-server-1</artifactId>
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

### 2.2.2.1. ResourceServerApplication.java
```java
package com.azure.spring.sample.active.directory.oauth2.sample03.resource.server1;

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
package com.azure.spring.sample.active.directory.oauth2.sample03.resource.server1.configuration;

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

#### 2.2.2.3. ApplicationConfiguration.java
```java
package com.azure.spring.sample.active.directory.oauth2.sample03.resource.server1.configuration;

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

#### 2.2.2.4. HomeController.java
```java
package com.azure.spring.sample.active.directory.oauth2.sample03.resource.server1.controller;

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

  <groupId>com.azure.spring.sample.active.directory.oauth2</groupId>
  <artifactId>sample-03-resource-server-2</artifactId>
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
package com.azure.spring.sample.active.directory.oauth2.sample03.resource.server2;

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
package com.azure.spring.sample.active.directory.oauth2.sample03.resource.server2.configuration;

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
package com.azure.spring.sample.active.directory.oauth2.sample03.resource.server2.configuration;

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
package com.azure.spring.sample.active.directory.oauth2.sample03.resource.server2.controller;

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

# 4. Run sample applications

# 5. Homework





[Azure Active Directory OAuth2 samples]: ../README.md
[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[OAuth2]: https://oauth.net/2/
[Spring Security]: https://spring.io/projects/spring-security
[OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[access token]: https://docs.microsoft.com/en-us/azure/active-directory/develop/access-tokens
[sample-03-multiple-resource-server]: ../sample-03-multiple-resource-server