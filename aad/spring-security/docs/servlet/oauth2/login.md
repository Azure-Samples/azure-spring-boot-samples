- [1. About](#1-about)
- [2. Get sample applications](#2-get-sample-applications)
- [3. Create resources in Azure](#3-create-resources-in-azure)
	* [3.1. Create a tenant](#31-create-a-tenant)
	* [3.2. Add a new user](#32-add-a-new-user)
	* [3.3. Register client-1](#33-register-client-1)
	* [3.4. Add a client secret for client-1](#34-add-a-client-secret-for-client-1)
	* [3.5. Add a redirect URI for client-1](#35-add-a-redirect-uri-for-client-1)
- [4. Run sample applications](#4-run-sample-applications)
- [5. Homework](#5-homework)








# 1. About

This section shows the basic scenario:
1. Login by [OAuth 2.0 authorization code flow].

# 2. Get sample applications
Get samples applications from in GitHub: [login].

# 3. Create resources in Azure

## 3.1. Create a tenant
Read [document about creating an Azure AD tenant], create a new tenant. Get the tenant-id: **${TENANT_ID}**.  
> After creating a new tenant, You can refer to [README] if you want to start the sample without the knowledge of step by step.  

## 3.2. Add a new user
Read [document about adding users], add a new user: **user-1@${tenant-name}.com**. Get the user's password.

## 3.3. Register client-1
Read [document about registering an application], register an application named **client-1**. Get the client-id: **${CLIENT_1_CLIENT_ID}**.

## 3.4. Add a client secret for client-1
Read [document about adding a client secret], add a client secret. Get the client-secret value: **${CLIENT_1_CLIENT_SECRET}**.

## 3.5. Add a redirect URI for client-1
Read [document about adding a redirect URI], add redirect URI: **http://localhost:8080/login/oauth2/code/**.



# 4. Run sample applications
 1. Open sample application: [login], fill the placeholders in **application.yml**, then run the application.
 2. Open browser(for example: [Edge]), close all [InPrivate window], and open a new [InPrivate window].
 3. Access **http://localhost:8080**, it will redirect to Microsoft login page. Input username and password (update password if it requests you to), it will return permission request page. click **Accept**, then it will return **Hello, this is client-1.**. This means we log in successfully.
 4. Access **http://localhost:8080/**, it will return **Hello, this is client-1.**, which means login successfully.

# 5. Homework
 1. Read [rfc6749].
 2. Read [document about OAuth 2.0 and OpenID Connect protocols on the Microsoft identity platform].
 3. Read [document about Microsoft identity platform and OpenID Connect protocol]
 4. Read [document about Microsoft identity platform and OAuth 2.0 authorization code flow].
 5. . Investigate each item's purpose in the sample projects' **application.yml**.
 6. In [login]'s **application.yml**, the property **spring.security.oauth2.client.registration.scope** contains **openid**, **profile**. what will happen if we delete these scopes?






[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[OAuth2]: https://oauth.net/2/
[Spring Security]: https://spring.io/projects/spring-security
[OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[access token]: https://docs.microsoft.com/azure/active-directory/develop/access-tokens
[document about creating an Azure AD tenant]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-create-new-tenant#create-a-new-azure-ad-tenant
[document about registering an application]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app
[document about adding users]: https://docs.microsoft.com/azure/active-directory/fundamentals/add-users-azure-active-directory
[document about adding a client secret]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-client-secret
[document about adding a redirect URI]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-redirect-uri
[document about registering an application]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app
[document about exposing an api]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-expose-web-apis
[document about Application manifest]: https://docs.microsoft.com/azure/active-directory/develop/reference-app-manifest#accesstokenacceptedversion-attribute
[login]: ../../../servlet/oauth2/login
[README]: ../../../servlet/oauth2/login/README.md
[Edge]: https://www.microsoft.com/edge?r=1
[InPrivate window]: https://support.microsoft.com/microsoft-edge/browse-inprivate-in-microsoft-edge-cd2c9a48-0bc4-b98e-5e46-ac40c84e27e2
[rfc6749]: https://datatracker.ietf.org/doc/html/rfc6749
[document about OAuth 2.0 and OpenID Connect protocols on the Microsoft identity platform]: https://docs.microsoft.com/azure/active-directory/develop/active-directory-v2-protocols
[document about Microsoft identity platform and OpenID Connect protocol]: https://docs.microsoft.com/azure/active-directory/develop/v2-protocols-oidc
[document about Microsoft identity platform and OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[document about Microsoft identity platform ID tokens]: https://docs.microsoft.com/azure/active-directory/develop/id-tokens
[document about Microsoft identity platform access tokens]: https://docs.microsoft.com/azure/active-directory/develop/access-tokens
[document about Microsoft identity platform refresh tokens]: https://docs.microsoft.com/azure/active-directory/develop/refresh-tokens
