- [Preface](#preface)
    * [About](#about)
    * [Ask for help by GitHub issues](#ask-for-help-by-github-issues)
- [1. client](#1-client)
    * [1.1. Create sample project](#11-create-sample-project)
        + [1.1.1. Create a pom file in a new folder.](#111-create-a-pom-file-in-a-new-folder)
        + [1.1.2. Create Java classes.](#112-create-java-classes)
            - [1.1.2.1. ClientApplication](#1121-clientapplication)
            - [1.1.2.2. WebSecurityConfigure](#1122-websecurityconfigure)
            - [1.1.2.3. HomeController](#1123-homecontroller)
        + [1.1.3. Create application.yml.](#113-create-applicationyml)
    * [1.2. Create required resources in Azure.](#12-create-required-resources-in-azure)
        + [1.2.1. Get an Azure Active Directory tenant](#121-get-an-azure-active-directory-tenant)
        + [1.2.2. Register an application](#122-register-an-application)
        + [1.2.3. Add a redirect URI](#123-add-a-redirect-uri)
        + [1.2.4. Add a client secret](#124-add-a-client-secret)
        + [1.2.5. Get user account.](#125-get-user-account)
    * [1.3. Run the application](#13-run-the-application)

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

#### 1.1.2.1. ClientApplication
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

#### 1.1.2.2. WebSecurityConfigure
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

#### 1.1.2.3. HomeController
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
spring:
  security:
    oauth2:
      client:
        provider:
          azure-active-directory:
            issuer-uri: https://login.microsoftonline.com/<tenant-id>
        registration:
          client-1:
            provider: azure-active-directory
            client-id: <client-id>
            client-secret: <client-secret>
            scope: openid, profile, offline_access, api://client-1/client-1-scope-1
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