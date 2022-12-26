---
page_type: sample
languages:
- java
products:
- azure-active-directory
name: Developing Spring Boot Web Application Supports Login by Azure Active Directory Account and Expose REST API
description: This sample demonstrates how to develop a Spring Boot web application supports login by Azure AD account and expose REST API at the same time.
---

# Developing Spring Boot Web Application Supports Login by Azure Active Directory Account and Expose REST API

## Key concepts

This scenario supports `Web application` and `Resource server` in one application.

## Getting started

We assume that when used as a Resource server, it is called `WebApiC`; when used as a Web application, it is called `WebApp2`.

### Configure web app

See [Configure web application] for more information about web app.

### Configure groups for sign in user

See [Configure groups for sign in user] for more information about groups for sign in user.

### Configure web api

See [Configure your middle-tier Web API] or [Configure Web API] for more information about web api.

## Advanced features

### Run with Maven
```shell
cd azure-spring-boot-samples/aad/spring-cloud-azure-starter-active-directory/aad-web-application-and-resource-server
mvn spring-boot:run
```

### Check the authentication and authorization.

See [Check the authentication and authorization of Web Application] or [Check the authentication and authorization of Resource Server] for more information about checking.

## Troubleshooting
## Next steps
## Contributing

<!-- LINKS -->
[Web application and Resource server in one application]: https://github.com/Azure/azure-sdk-for-java/tree/3b84b480a4e0284916da8fe96d4027fdb7262dd1/sdk/spring/azure-spring-boot-starter-active-directory#web-application-and-resource-server-in-one-application
[Configure web application]: ../web-client-access-resource-server/aad-web-application/README.md#configure-web-app
[Configure groups for sign in user]: ../web-client-access-resource-server/aad-web-application/README.md#configure-groups-for-sign-in-user
[Configure your middle-tier Web API]: ../web-client-access-resource-server/aad-resource-server-obo#configure-your-middle-tier-web-api-a
[Configure Web API]: ../web-client-access-resource-server/aad-resource-server#configure-web-api
[Check the authentication and authorization of Web Application]: ../web-client-access-resource-server/aad-web-application/README.md#check-the-authentication-and-authorization
[Check the authentication and authorization of Resource Server]: ../web-client-access-resource-server/aad-resource-server/README.md#check-the-authentication-and-authorization
