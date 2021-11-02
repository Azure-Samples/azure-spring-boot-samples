# 1. About

Please refer to [Azure Active Directory OAuth2 samples] to get all samples about using [Azure Active Directory] and [OAuth2] to protect web application developed by [Spring Security].

This sample will demonstrate this scenario:
 - User sign in client and client get [access token] by [OAuth 2.0 authorization code flow].
 - Client access resource-server by [access token].
 - Resource server validate the [access token] by validating the signature, and checking these claims: `aud`, `nbf` and `exp`.

# 2. Create sample applications
You can follow the following steps to create sample applications, or you can use samples in GitHub: [sample-01-simplest-scenario].

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
  <artifactId>sample-01-client</artifactId>
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
package com.azure.spring.sample.active.directory.oauth2.sample01.client;

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
package com.azure.spring.sample.active.directory.oauth2.sample01.client.configuration;

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
package com.azure.spring.sample.active.directory.oauth2.sample01.client.configuration;

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
package com.azure.spring.sample.active.directory.oauth2.sample01.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, this is sample-01-client.";
    }
}
```

#### 2.1.2.5. ResourceServerController.java
```java
package com.azure.spring.sample.active.directory.oauth2.sample01.client.controller;

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

    @GetMapping("/resource-server")
    public String resourceServer1(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
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
            scope: openid, profile, api://<resource-server-1-client-id>/resource-server-1.scope-1, # Refs: https://docs.microsoft.com/azure/active-directory/develop/v2-permissions-and-consent#openid-connect-scopes
            redirect-uri: http://localhost:8080/login/oauth2/code/
```

## 2.2. Resource server

### 2.2.1. pom.xml

### 2.2.2. Java classes

### 2.2.3. application.yml

# 3. Create resources in Azure

# 4. Run sample applications

# 5. Homework





[Azure Active Directory OAuth2 samples]: ../README.md
[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[OAuth2]: https://oauth.net/2/
[Spring Security]: https://spring.io/projects/spring-security
[OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[access token]: https://docs.microsoft.com/en-us/azure/active-directory/develop/access-tokens
[sample-01-simplest-scenario]: ../sample-01-simplest-scenario