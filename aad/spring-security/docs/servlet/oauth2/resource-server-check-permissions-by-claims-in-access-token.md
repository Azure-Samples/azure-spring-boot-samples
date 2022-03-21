- [1. About](#1-about)
- [2. Get sample applications](#2-get-sample-applications)
- [3. Create resources in Azure](#3-create-resources-in-azure)
    * [3.1. Create a tenant](#31-create-a-tenant)
    * [3.2. Add a new user](#32-add-a-new-user)
    * [3.3. Register client-1](#33-register-client-1)
    * [3.4. Add a client secret for client-1](#34-add-a-client-secret-for-client-1)
    * [3.5. Add a redirect URI for client-1](#35-add-a-redirect-uri-for-client-1)
    * [3.6. Register resource-server-1](#36-register-resource-server-1)
    * [3.7. Expose apis for resource-server-1](#37-expose-apis-for-resource-server-1)
    * [3.8. Set accessTokenAcceptedVersion to 2 for resource-server-1](#38-set-accesstokenacceptedversion-to-2-for-resource-server-1)
    * [3.9. Create roles for resource-server-1](#39-create-roles-for-resource-server-1)
    * [3.10. Assign user-1 to resource-server-1-role-1](#310-assign-user-1-to-resource-server-1-role-1)
- [4. Run sample applications](#4-run-sample-applications)
- [5. Homework](#5-homework)







# 1. About

This section shows this scenario:
1. User sign in client and client get [access token] by [OAuth 2.0 authorization code flow].
2. Client check whether current signed-in user has permission to specific method.
3. Client access resource-server by [access token].
4. Resource server validate the [access token] by validating the signature, and checking these claims: **aud**, **nbf** and **exp**.
5. Resource server check whether current signed-in user has permission to specific method.

# 2. Get sample applications
Get samples applications from in GitHub: [resource-server-check-permissions-by-claims-in-access-token].

# 3. Create resources in Azure

## 3.1. Create a tenant
Read [document about creating an Azure AD tenant], create a new tenant. Get the tenant-id: **${TENANT_ID}**.  
> After creating a new tenant, You can refer to [README.md](../../../servlet/oauth2/resource-server-check-permissions-by-claims-in-access-token/README.md) if you want to start the sample without the knowledge of step by step.  

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

## 3.7. Expose apis for resource-server-1
Read [document about exposing an api], expose 2 scopes for resource-server-1: **resource-server-1.scope-1** and **resource-server-1.scope-2**, choose **Admins and users** for **Who can consent** option.

## 3.8. Set accessTokenAcceptedVersion to 2 for resource-server-1
Read [document about Application manifest], set `accessTokenAcceptedVersion` to `2`.

## 3.9. Create roles for resource-server-1
Read [document about declaring roles for an application], create 2 roles for resource-server-1: **resource-server-1-role-1** and **resource-server-1-role-2**.

## 3.10. Assign user-1 to resource-server-1-role-1
Read [document about assigning users and groups to roles], assign **user-1** to **resource-server-2-role-1**.

# 4. Run sample applications
1. Open sample application: [client], fill the placeholders in **application.yml** and **CheckPermissionByScopeController.java**, then run the application.
2. Open sample application: [resource-server], fill the placeholders in **application.yml**, then run the application.
3. Open browser(for example: [Edge]), close all [InPrivate window], and open a new [InPrivate window].
4. Access **http://localhost:8080**, it will redirect to Microsoft login page. Input username and password (update password if it requests you to), it will return permission request page. click **Accept**, then it will return **Hello, this is client-1.**. This means we log in successfully.
5. Access **http://localhost:8080/scope/resource-server-1-scope-1**, it will return **Hi, this is resource-server-1. You can access my endpoint: /scope/resource-server-1-scope-1**.
6. Access **http://localhost:8080/scope/resource-server-1-scope-2**, it will return 403, which means user do not have authority to access this endpoint.
7. Access **http://localhost:8080/resource-server-1**, it will return **Hello, this is resource-server-1.**, which means [client] can access [resource-server].
8. Access **http://localhost:8080/resource-server-1/scope/resource-server-1-scope-1**, it will return **Hi, this is resource-server-1. You can access my endpoint: /scope/resource-server-1-scope-1**.
9. Access **http://localhost:8080/resource-server-1/scope/resource-server-1-scope-2**, it will return 500. Check the log of [client], it show that [resource-server] returned 403 to [client]. which means [client] doesn't have authority to access this endpoint of [resource-server].
10. Access **http://localhost:8080/resource-server-1/role/resource-server-1-role-1**, it will return **Hi, this is resource-server-1. You can access my endpoint: /role/resource-server-1-role-1**.
11. Access **http://localhost:8080/resource-server-1/role/resource-server-1-role-2**, it will return 500. Check the log of [client], it show that [resource-server] returned 403 to [client]. which means [client] doesn't have authority to access this endpoint of [resource-server].

# 5. Homework
1. Try to use groups for authorization.
2. List all claims that can be used for authorization.




[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[OAuth2]: https://oauth.net/2/
[Spring Security]: https://spring.io/projects/spring-security
[OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[access token]: https://docs.microsoft.com/azure/active-directory/develop/access-tokens
[resource-server-check-permissions-by-claims-in-access-token]: ../../../servlet/oauth2/resource-server-check-permissions-by-claims-in-access-token
[document about creating an Azure AD tenant]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-create-new-tenant#create-a-new-azure-ad-tenant
[document about registering an application]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app
[document about adding users]: https://docs.microsoft.com/azure/active-directory/fundamentals/add-users-azure-active-directory
[document about adding a client secret]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-client-secret
[document about adding a redirect URI]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-redirect-uri
[document about exposing an api]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-expose-web-apis
[document about Application manifest]: https://docs.microsoft.com/azure/active-directory/develop/reference-app-manifest#accesstokenacceptedversion-attribute
[document about configuring a client application to access a web API]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-access-web-apis
[document about assigning users and groups to roles]: https://docs.microsoft.com/azure/active-directory/develop/howto-add-app-roles-in-azure-ad-apps#assign-users-and-groups-to-roles
[document about declaring roles for an application]: https://docs.microsoft.com/azure/active-directory/develop/howto-add-app-roles-in-azure-ad-apps#declare-roles-for-an-application
[document about assigning users and groups to roles]: https://docs.microsoft.com/azure/active-directory/develop/howto-add-app-roles-in-azure-ad-apps#assign-users-and-groups-to-roles
[client]: ../../../servlet/oauth2/resource-server-check-permissions-by-claims-in-access-token/client
[resource-server]: ../../../servlet/oauth2/resource-server-check-permissions-by-claims-in-access-token/resource-server
[Edge]: https://www.microsoft.com/edge?r=1
[InPrivate window]: https://support.microsoft.com/microsoft-edge/browse-inprivate-in-microsoft-edge-cd2c9a48-0bc4-b98e-5e46-ac40c84e27e2
