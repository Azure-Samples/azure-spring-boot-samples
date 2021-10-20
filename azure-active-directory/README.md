- [Preface](#preface)
    * [About](#about)
    * [Ask for help by GitHub issues](#ask-for-help-by-github-issues)
- [1. client](#1-client)
    * [1.1. Create sample project](#11-create-sample-project)
        + [1.1.1. Create a pom file in a new folder.](#111-create-a-pom-file-in-a-new-folder)
        + [1.1.2. Create Java classes.](#112-create-java-classes)
            - [1.1.2.1. ClientApplication.java](#1121-clientapplicationjava)
            - [1.1.2.2. WebSecurityConfigure.java](#1122-websecurityconfigurejava)
            - [1.1.2.3. HomeController.java](#1123-homecontrollerjava)
        + [1.1.3. Create application.yml.](#113-create-applicationyml)
    * [1.2. Create required resources in Azure.](#12-create-required-resources-in-azure)
        + [1.2.1. Get an Azure Active Directory tenant](#121-get-an-azure-active-directory-tenant)
        + [1.2.2. Register an application](#122-register-an-application)
        + [1.2.3. Add a redirect URI](#123-add-a-redirect-uri)
        + [1.2.4. Add a client secret](#124-add-a-client-secret)
        + [1.2.5. Get user account.](#125-get-user-account)
    * [1.3. Run the application](#13-run-the-application)
    * [1.4. Homework](#14-homework)
- [2. client-get-user-information](#2-client-get-user-information)
    * [2.1. Update sample project](#21-update-sample-project)
        + [2.1.1. Update pom.xml](#211-update-pomxml)
        + [2.1.2. Add new Java class](#212-add-new-java-class)
            - [2.1.2.1 UserInformationController.java](#2121-userinformationcontrollerjava)
        + [2.1.3. Update application.yml](#213-update-applicationyml)
    * [2.2. Create required resources in Azure.](#22-create-required-resources-in-azure)
    * [2.3. Run the application](#23-run-the-application)
    * [2.4. Homework](#24-homework)
- [3. resource-server](#3-resource-server)
    * [3.1. Create sample project](#31-create-sample-project)
        + [3.1.1. Create a pom file in a new folder](#311-create-a-pom-file-in-a-new-folder)
        + [3.1.2. Create Java classes](#312-create-java-classes)
            - [3.1.2.1. ResourceServerApplication.java](#3121-resourceserverapplicationjava)
            - [3.1.2.2. HomeController.java](#3122-homecontrollerjava)
            - [3.1.2.3. WebSecurityConfigure.java](#3123-websecurityconfigurejava)
        + [3.1.3. Create application.yml](#313-create-applicationyml)
    * [3.2. Create required resources in Azure.](#32-create-required-resources-in-azure)
    * [3.3. Run the application](#33-run-the-application)
    * [3.4. Homework](#34-homework)

# Preface

## About
Go through these samples one by one, you will learn how to protect your web service by [Azure Active Directory]. We will leverage [Spring Security] to achieve this.

## Ask for help by GitHub issues
If you have any question, please [create an issue].

# 1. client
This section will demonstrate how to use Azure Active Directory user account to log in a web service. You can choose one of the following options to get the sample project.
- Option 1: Use [01-client] project directly
- Option 2: Follow steps in [1.1. Create sample project](#11-create-sample-project) to create the sample project.

## 1.1. Create sample project

### 1.1.1. Create a pom file in a new folder.
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.5.4</version> <!-- {x-version-update;org.springframework.boot:spring-boot-starter-parent;external_dependency} -->
  </parent>

  <groupId>com.azure.sample.azure.active.directory</groupId>
  <artifactId>01-client</artifactId>
  <version>1.0.0</version>
  <packaging>jar</packaging>

  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-oauth2-client</artifactId>
    </dependency>
  </dependencies>

</project>
```

### 1.1.2. Create Java classes.

#### 1.1.2.1. ClientApplication.java
```java
package com.azure.sample.azure.active.directory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}
```

#### 1.1.2.2. WebSecurityConfigure.java
```java
package com.azure.sample.azure.active.directory.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .oauth2Login()
                .and();
        // @formatter:off
    }
}
```

#### 1.1.2.3. HomeController.java
```java
package com.azure.sample.azure.active.directory.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "hello";
    }
}
```

### 1.1.3. Create application.yml.
```yml
# Please read "/azure-active-directory/README.md" to fill the placeholders in this file: "<tenant-id>", "<client-id>", "<client-secret>".
server:
  port: 8080
spring:
  security:
    oauth2:
      client:
        provider: # Refs: https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2login-common-oauth2-provider
          azure-active-directory:
            issuer-uri: https://sts.windows.net/<tenant-id>/ # Refs: https://docs.spring.io/spring-security/site/docs/current/reference/html5/#webflux-oauth2-login-openid-provider-configuration
        registration:
          client-1:
            provider: azure-active-directory
            client-id: <client-id>
            client-secret: <client-secret>
            scope: openid, profile # Refs: https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-permissions-and-consent#openid-connect-scopes
            redirect-uri: http://localhost:8080/login/oauth2/code/
```
For more information about these configuration items, please refer to [Spring Security doc about CommonOAuth2Provider].

Next step, we need to fill these placeholders in application.yml: `<tenant-id>`, `client-id`, `<client-secret>`.

## 1.2. Create required resources in Azure.
After sample project created, we need some resources in Azure. And get some properties and replace the placeholder in `application.yml`.

### 1.2.1. Get an Azure Active Directory tenant
Read [MS docs about get an Azure AD tenant], get an Azure Active Directory tenant. Get the tenant-id and replace the placeholder(`<tenant-id>`) in `application.yml`.

### 1.2.2. Register an application
Read [MS docs about register an application], register an application. Here application is an OAuth2 client. Get the client-id and replace the placeholder(`<client-id>`) in `application.yml`.

### 1.2.3. Add a redirect URI
Read [MS docs about add a redirect URI], add redirect URI: `http://localhost:8080/login/oauth2/code/`.

### 1.2.4. Add a client secret
Read [MS docs about add a client secret], add a client secret. Get the client-secret and replace the placeholder(`<client-secret>`) in `application.yml`.

### 1.2.5. Get user account.
If you already have user account in this tenant, you can skip this step. Otherwise, read [MS docs about add users], create an account: `user-1@<tenant-name>.com`.

## 1.3. Run the application
Run the Spring Boot application. If you don't know how to run Spring Boot application, please refer to [Spring docs about running application].

Use web browser to access `http://localhost:8080/`, the web page will redirect to Azure Active Directory login page. Input username and password of `user-1@<tenant-name>.com`, then you can log in successfully and see "hello" in the web page.

## 1.4. Homework
 - Read [rfc6749] to learn OAuth 2.0 authorization framework

# 2. client-get-user-information

This section will demonstrate how to use get user information. You can choose one of the following options to get the sample project.
- Option 1: Use [02-client-get-user-information] project directly
- Option 2: Follow steps in [2.1. Update sample project](#11-create-sample-project) to create the sample project.

## 2.1. Update sample project
This project is build on top of [01-client]. The following steps will change [01-client] into [02-client-get-user-information].

### 2.1.1. Update pom.xml
Add the following dependency in pom.xml:
```xml
    <dependency>
      <groupId>com.fasterxml.jackson.datatype</groupId>
      <artifactId>jackson-datatype-jsr310</artifactId>
    </dependency>
```

### 2.1.2. Add new Java class

#### 2.1.2.1 UserInformationController.java
```java
package com.azure.sample.azure.active.directory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInformationController {

    ObjectMapper objectMapper;

    public UserInformationController() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @GetMapping(
        path = "/user-information",
        produces = MediaType.APPLICATION_JSON_VALUE)
    public String userInformation() throws JsonProcessingException {
        return objectMapper.writeValueAsString(SecurityContextHolder.getContext().getAuthentication());
    }
}
```

### 2.1.3. Update application.yml
Add the following configuration:
```yaml
spring:
  security:
    oauth2:
      client:
        provider:
          azure-active-directory:
            user-name-attribute: upn
```

## 2.2. Create required resources in Azure.
All resources required in this sample is already created in previous sample. So no need to create new resources.

## 2.3. Run the application
Run the Spring Boot application.

Use web browser to access `http://localhost:8080/user-information`. After log in, you can see a json response which will display the user information.

## 2.4. Homework
 - Investigate what is the property (`spring.security.oauth2.client.provider.azure-active-directory.user-name-attribute = upn`) is used for.

# 3. resource-server
This section will demonstrate how to use Azure Active Directory to protect a resource-server. You can choose one of the following options to get the sample project.
 - Option 1: Use [03-resource-server] directly.
 - Follow the steps in [3.1. Create sample project](#31-create-sample-project) to create the sample project.

## 3.1. Create sample project

### 3.1.1. Create a pom file in a new folder
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.5.4</version> <!-- {x-version-update;org.springframework.boot:spring-boot-starter-parent;external_dependency} -->
  </parent>

  <groupId>com.azure.sample.azure.active.directory</groupId>
  <artifactId>03-resource-server</artifactId>
  <version>1.0.0</version>
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

### 3.1.2. Create Java classes

#### 3.1.2.1. ResourceServerApplication.java
```java
package com.azure.sample.azure.active.directory.resource.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }
}
```

#### 3.1.2.2. HomeController.java
```java
package com.azure.sample.azure.active.directory.resource.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "hello";
    }
}
```

#### 3.1.2.3. WebSecurityConfigure.java
```java
package com.azure.sample.azure.active.directory.resource.server.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .oauth2ResourceServer()
                .jwt()
                .and();
        // @formatter:off
    }
}
```

### 3.1.3. Create application.yml
```yaml
# Please read "/azure-active-directory/README.md" to fill the placeholders in this file: "<tenant-id>".
server:
  port: 8081
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://sts.windows.net/<tenant-id>/
```

## 3.2. Create required resources in Azure.
All resources required in this sample is already created in previous sample. So no need to create new resources.

## 3.3. Run the application
Run the Spring Boot application.

Use web browser to access `http://localhost:8081`. It should return 401. Because now we do not have authority to access this resource-server. In the next section, we will use OAuth2 client to access this resource-server.

## 3.4. Homework

 - Read the [rfc6749#section-1.1], learn the roles in the OAuth 2.0 authorization framework.
 - Read the [rfc6749#section-1.2], learn the abstract protocol flow.






[create an issue]: https://github.com/Azure-Samples/azure-spring-boot-samples/issues/new
[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[01-client]: ./01-client
[Spring Security]: https://spring.io/projects/spring-security
[Spring Security doc about CommonOAuth2Provider]: https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2login-common-oauth2-provider
[MS docs about get an Azure AD tenant]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-create-new-tenant
[MS docs about register an application]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app
[MS docs about add a redirect URI]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-redirect-uri
[MS docs about add a client secret]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-client-secret
[MS docs about add users]: https://docs.microsoft.com/azure/active-directory/fundamentals/add-users-azure-active-directory
[Spring docs about running application]: https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.running-your-application
[rfc6749]: https://datatracker.ietf.org/doc/html/rfc6749
[02-client-get-user-information]: ./02-client-get-user-information
[03-resource-server]: ./03-resource-server
[rfc6749#section-1.1]: https://datatracker.ietf.org/doc/html/rfc6749#section-1.1
[rfc6749#section-1.2]: https://datatracker.ietf.org/doc/html/rfc6749#section-1.2