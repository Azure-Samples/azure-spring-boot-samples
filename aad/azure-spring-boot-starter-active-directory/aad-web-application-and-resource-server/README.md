---
page_type: sample
languages:
- java
products:
- azure-active-directory
description: "OAuth 2.0 Sample project for Azure AD Spring Boot Starter client library"
urlFragment: "azure-spring-boot-sample-active-directory-webapp-and-resource-server"
---

# OAuth 2.0 Sample for Azure AD Spring Boot Starter client library for Java

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
cd azure-spring-boot-samples/aad/azure-spring-boot-starter-active-directory/aad-resource-server
mvn spring-boot:run
```

### Check the authentication and authorization.

See [Check the authentication and authorization of Web Application] or [Check the authentication and authorization of Resource Server] for more information about checking.

## Troubleshooting
## Next steps
## Contributing

<!-- LINKS -->
[Web application and Resource server in one application]: https://github.com/Azure/azure-sdk-for-java/blob/main/sdk/spring/azure-spring-boot-starter-active-directory#web-application-and-resource-server-in-one-application
[Configure web application]: https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/aad/azure-spring-boot-starter-active-directory/aad-web-application#configure-web-app
[Configure groups for sign in user]: https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/aad/azure-spring-boot-starter-active-directory/aad-web-application#configure-groups-for-sign-in-user
[Configure your middle-tier Web API]: https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/aad/azure-spring-boot-starter-active-directory/aad-resource-server-obo#configure-your-middle-tier-web-api-a
[Configure Web API]: https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/aad/azure-spring-boot-starter-active-directory/aad-resource-server#configure-web-api
[Check the authentication and authorization of Web Application]: https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/aad/azure-spring-boot-starter-active-directory/aad-web-application#check-the-authentication-and-authorization
[Check the authentication and authorization of Resource Server]: https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/aad/azure-spring-boot-starter-active-directory/aad-resource-server#check-the-authentication-and-authorization
