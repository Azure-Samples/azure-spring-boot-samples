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
    * [3.9. Register resource-server-2](#39-register-resource-server-2)
    * [3.10. Expose apis for resource-server-2](#310-expose-apis-for-resource-server-2)
    * [3.11. Set accessTokenAcceptedVersion to 2 for resource-server-2](#311-set-accesstokenacceptedversion-to-2-for-resource-server-2)
- [4. Run sample applications](#4-run-sample-applications)
- [5. Homework](#5-homework)





# 1. About

In [Azure Active Directory]'s [access token], the **aud** claim is a single string, not a list of strings. It means one [access token] can only been accepted for one resource server. And one **OAuth2AuthorizedClient** can only hold one access token. So, if one application want to access multiple resource servers, it must configure multiple **ClientRegistration**s.

This section shows this scenario:
1. One client application access multiple resource servers.
2. Consent all scopes when request for a specific endpoint.

# 2. Get sample applications
Get samples applications from in GitHub: [client-access-multiple-resource-server].

# 3. Create resources in Azure

## 3.1. Create a tenant
Read [document about creating an Azure AD tenant], create a new tenant. Get the tenant-id: **${TENANT_ID}**.

> After creating a new tenant, You can refer to [README.md](../../../servlet/oauth2/client-access-multiple-resource-server/README.md) if you want to start the sample without the knowledge of step by step.

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

## 3.9. Register resource-server-2
Read [document about registering an application], register an application named **resource-server-2**. Get the client-id: **${RESOURCE_SERVER_2_CLIENT_ID}**.

## 3.10. Expose apis for resource-server-2
Read [document about exposing an api], expose 2 scopes for resource-server-2: **resource-server-2.scope-1** and **resource-server-2.scope-2**, choose **Admins and users** for **Who can consent** option.

## 3.11. Set accessTokenAcceptedVersion to 2 for resource-server-2
Read [document about Application manifest], set `accessTokenAcceptedVersion` to `2`.

# 4. Run sample applications
1. Open sample application: [client], fill the placeholders in **application.yml**, then run the application.
2. Open sample application: [resource-server-1], fill the placeholders in **application.yml**, then run the application.
3. Open sample application: [resource-server-2], fill the placeholders in **application.yml**, then run the application.
4. Open browser(for example: [Edge]), close all [InPrivate window], and open a new [InPrivate window].
5. Access **http://localhost:8080/resource-server-all**, it will return login page.
6. Click **client-1-resource-server-1**, it will redirect to Microsoft login page.
7. Input username and password (update password if it requests you to), it will return permission request page: let user permit **client-1** to access **resource-server-1**.
8. Click **Accept**, then it will return permission request page: let user permit **client-1** to access **resource-server-2**.
9. Click **Accept**, then it will return **Hello, this is client-1, ...**. This means user log in successfully.
10. Access **http://localhost:8080/resource-server-1**, it will return **Hello, this is resource-server-1.**, there is no permission request page anymore.
11. Access **http://localhost:8080/resource-server-2**, it will return **Hello, this is resource-server-2.**, there is no permission request page anymore.

# 5. Homework
1. If there are 100 clients configured in application.yml, the permission request page will appear 100 times. Please investigate how to reduce the consent page.




[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[OAuth2]: https://oauth.net/2/
[Spring Security]: https://spring.io/projects/spring-security
[OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[access token]: https://docs.microsoft.com/azure/active-directory/develop/access-tokens
[client-access-multiple-resource-server]: ../../../servlet/oauth2/client-access-multiple-resource-server
[document about creating an Azure AD tenant]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-create-new-tenant#create-a-new-azure-ad-tenant
[document about registering an application]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app
[document about adding users]: https://docs.microsoft.com/azure/active-directory/fundamentals/add-users-azure-active-directory
[document about adding a client secret]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-client-secret
[document about adding a redirect URI]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-redirect-uri
[document about exposing an api]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-expose-web-apis
[document about Application manifest]: https://docs.microsoft.com/azure/active-directory/develop/reference-app-manifest#accesstokenacceptedversion-attribute
[client]: ../../../servlet/oauth2/client-access-multiple-resource-server/client
[resource-server-1]: ../../../servlet/oauth2/client-access-multiple-resource-server/resource-server-1
[resource-server-2]: ../../../servlet/oauth2/client-access-multiple-resource-server/resource-server-2
[Edge]: https://www.microsoft.com/edge?r=1
[InPrivate window]: https://support.microsoft.com/microsoft-edge/browse-inprivate-in-microsoft-edge-cd2c9a48-0bc4-b98e-5e46-ac40c84e27e2

