- [1. About](#1-about)
- [2. Get sample applications](#2-get-sample-applications)
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
    * [3.10. Set accessTokenAcceptedVersion to 2 for resource-server-1](#310-set-accesstokenacceptedversion-to-2-for-resource-server-1)
    * [3.11. Register resource-server-2](#311-register-resource-server-2)
    * [3.12. Expose apis for resource-server-2](#312-expose-apis-for-resource-server-2)
    * [3.13. Set accessTokenAcceptedVersion to 2 for resource-server-1](#313-set-accesstokenacceptedversion-to-2-for-resource-server-1)
    * [3.14. Authorize resource-server-1 to access resource-server-2](#314-authorize-resource-server-1-to-access-resource-server-2)
- [4. Run sample applications](#4-run-sample-applications)
- [5. Homework](#5-homework)







# 1. About

This section shows this scenario:
1. User sign in client and client get [access token] by [OAuth 2.0 authorization code flow].
2. Client access resource-server-1 by [access token].
3. resource-server-1 validate the [access token] by validating the signature, and checking these claims: `aud`, `nbf` and `exp`.
4. resource-server-1 use the access token to get a new access token by [on behalf of flow].
5. resource-server-1 use the new access token to access resource-server-2.
6. resource-server-2 validate the [access token] by validating the signature, and checking these claims: `aud`, `nbf` and `exp`.

# 2. Get sample applications
Get samples applications from in GitHub: [resource-server-support-on-behalf-of-flow].

# 3. Create resources in Azure

## 3.1. Create a tenant
Read [document about creating an Azure AD tenant], create a new tenant. Get the tenant-id: **${TENANT_ID}**.  
> After creating a new tenant, You can refer to [README.md](../../../servlet/oauth2/resource-server-support-on-behalf-of-flow/README.md) if you want to start the sample without the knowledge of step by step.  

## 3.2. Add a new user
Read [document about adding users], add a new user: **user-1@${tenant-name}.com**. Get the user's password.

## 3.3. Register client-1
Read [document about registering an application], register an application named **client-1**. Get the client-id: **${CLIENT_1_CLIENT_ID}**.

## 3.4. Add a client secret for client-1
Read [document about adding a client secret], add a client secret. Get the client-secret value: **${CLIENT_1_CLIENT_SECRET}**.

## 3.5. Add a redirect URI for client-1
Read [document about adding a redirect URI], add redirect URI: **http://localhost:8080/login/oauth2/code/**.

## 3.6. Register resource-server-1
Read [document about registering an application], register an application named **resource-server-1**. Get the client-id: **${RESOURCE_SERVER_1_CLIENT_ID}**.

## 3.7. Add a client secret for resource-server-1
Read [document about adding a client secret], add a client secret. Get the client-secret value: **${RESOURCE_SERVER_1_CLIENT_SECRET}**.

## 3.8. Add a redirect URI for resource-server-1
Read [document about adding a redirect URI], add redirect URI: **http://localhost:8080/login/oauth2/code/**.

## 3.9. Expose apis for resource-server-1
Read [document about exposing an api], expose 2 scopes for resource-server-1: **resource-server-1.scope-1** and **resource-server-1.scope-2**, choose **Admins and users** for **Who can consent** option.

## 3.10. Set accessTokenAcceptedVersion to 2 for resource-server-1
Read [document about Application manifest], set `accessTokenAcceptedVersion` to `2`.

## 3.11. Register resource-server-2
Read [document about registering an application], register an application named **resource-server-2**. Get the client-id: **${RESOURCE_SERVER_2_CLIENT_ID}**.

## 3.12. Expose apis for resource-server-2
Read [document about exposing an api], expose 2 scopes for resource-server-2: **resource-server-2.scope-1** and **resource-server-2.scope-2**, choose **Admins and users** for **Who can consent** option.

## 3.13. Set accessTokenAcceptedVersion to 2 for resource-server-1
Read [document about Application manifest], set `accessTokenAcceptedVersion` to `2`.

## 3.14. Authorize resource-server-1 to access resource-server-2
Read [document about exposing an api], pre-authorize resource-server-1 to access resource-server-2.


# 4. Run sample applications
1. Open sample application: [client], fill the placeholders in **application.yml**, then run the application.
2. Open sample application: [resource-server-1], fill the placeholders in **application.yml**, then run the application.
3. Open sample application: [resource-server-2], fill the placeholders in **application.yml**, then run the application.
4. Open browser(for example: [Edge]), close all [InPrivate window], and open a new [InPrivate window].
5. Access **http://localhost:8080**, it will return login page.
6. Input username and password (update password if it requests you to), it will return **Hello, this is client-1.**, which means user log in successfully.
7. Access **http://localhost:8080/resource-server-1**, it will return **Hello, this is resource-server-1.**, which means [client] can access [resource-server-1].
8. Access **http://localhost:8080/resource-server-1/resource-server-2**, it will return **Hello, this is resource-server-2.**, which means [resource-server-1] can access [resource-server-2].

# 5. Homework





[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[OAuth2]: https://oauth.net/2/
[Spring Security]: https://spring.io/projects/spring-security
[OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[access token]: https://docs.microsoft.com/azure/active-directory/develop/access-tokens
[on behalf of flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-on-behalf-of-flow
[resource-server-support-on-behalf-of-flow]: ../../../servlet/oauth2/resource-server-support-on-behalf-of-flow
[document about creating an Azure AD tenant]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-create-new-tenant#create-a-new-azure-ad-tenant
[document about registering an application]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app
[document about adding users]: https://docs.microsoft.com/azure/active-directory/fundamentals/add-users-azure-active-directory
[document about adding a client secret]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-client-secret
[document about adding a redirect URI]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-redirect-uri
[document about exposing an api]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-expose-web-apis
[document about Application manifest]: https://docs.microsoft.com/azure/active-directory/develop/reference-app-manifest#accesstokenacceptedversion-attribute
[client]: ../../../servlet/oauth2/resource-server-support-on-behalf-of-flow/client
[resource-server-1]: ../../../servlet/oauth2/resource-server-support-on-behalf-of-flow/resource-server-1
[resource-server-2]: ../../../servlet/oauth2/resource-server-support-on-behalf-of-flow/resource-server-2
[Edge]: https://www.microsoft.com/edge?r=1
[InPrivate window]: https://support.microsoft.com/microsoft-edge/browse-inprivate-in-microsoft-edge-cd2c9a48-0bc4-b98e-5e46-ac40c84e27e2


