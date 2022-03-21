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

This section will demonstrate this scenario:

![image](https://user-images.githubusercontent.com/13167207/148503627-700f287c-ea93-4957-b811-ad8f7f8c5ed3.png)

1. Client get [access token](https://docs.microsoft.com/azure/active-directory/develop/access-tokens) from [Azure Active Directory](https://azure.microsoft.com/services/active-directory/)
2. Client use the access token to access Gateway.
3. Gateway validate the access token. If the access token is valid, use the access token to access the ResourceServer. There are 2 ResourceServers, which ResourceServer to access depends on the request URL, it's configured in Gateway's application.yml. Gateway is implemented by [spring-cloud-gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
4. Gateway get the response from ResourceServer, then return to Client.

# 2. Get sample applications
Get samples applications from in GitHub: [spring-cloud-gateway](../../../../reactive/webflux/oauth2/spring-cloud-gateway).

# 3. Create resources in Azure

## 3.1. Create a tenant
Read [document about creating an Azure AD tenant](https://docs.microsoft.com/azure/active-directory/develop/quickstart-create-new-tenant#create-a-new-azure-ad-tenant), create a new tenant. Get the tenant-id: **${TENANT_ID}**.  
> After creating a new tenant, You can refer to [README.md](../../../../reactive/webflux/oauth2/spring-cloud-gateway/README.md) if you want to start the sample without the knowledge of step by step.

## 3.2. Add a new user
Read [document about adding users](https://docs.microsoft.com/azure/active-directory/fundamentals/add-users-azure-active-directory), add a new user: **user-1@${tenant-name}.com**. Get the user's password.

## 3.3. Register client-1
Read [document about registering an application](https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app), register an application named **client-1**. Get the client-id: **${CLIENT_1_CLIENT_ID}**.

## 3.4. Add a client secret for client-1
Read [document about adding a client secret](https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-client-secret), add a client secret. Get the client-secret value: **${CLIENT_1_CLIENT_SECRET}**.

## 3.5. Add a redirect URI for client-1
Read [document about adding a redirect URI](https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-redirect-uri), add 2 redirect URIs: **http://localhost:8080/login/oauth2/code/client-1-resource-server-1**, **http://localhost:8080/login/oauth2/code/client-1-resource-server-2**.

## 3.6. Register resource-server-1
Read [document about registering an application](https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app), register an application named **resource-server-1**. Get the client-id: **${RESOURCE_SERVER_1_CLIENT_ID}**.

## 3.7. Expose apis for resource-server-1
Read [document about exposing an api](https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-expose-web-apis), expose 2 scopes for resource-server-1: **resource-server-1.scope-1** and **resource-server-1.scope-2**, choose **Admins and users** for **Who can consent** option.

## 3.8. Set accessTokenAcceptedVersion to 2 for resource-server-1
Read [document about Application manifest](https://docs.microsoft.com/azure/active-directory/develop/reference-app-manifest#accesstokenacceptedversion-attribute), set `accessTokenAcceptedVersion` to `2`.

## 3.9. Register resource-server-2
Read [document about registering an application](https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app), register an application named **resource-server-2**. Get the client-id: **${RESOURCE_SERVER_2_CLIENT_ID}**.

## 3.10. Expose apis for resource-server-2
Read [document about exposing an api](https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-expose-web-apis), expose 2 scopes for resource-server-2: **resource-server-2.scope-1** and **resource-server-2.scope-2**, choose **Admins and users** for **Who can consent** option.

## 3.11. Set accessTokenAcceptedVersion to 2 for resource-server-2
Read [document about Application manifest](https://docs.microsoft.com/azure/active-directory/develop/reference-app-manifest#accesstokenacceptedversion-attribute), set `accessTokenAcceptedVersion` to `2`.

# 4. Run sample applications
1. Open sample application: [client](../../../../reactive/webflux/oauth2/spring-cloud-gateway/client), fill the placeholders in **application.yml**, then run the application.
2. Open sample application: [gateway](../../../../reactive/webflux/oauth2/spring-cloud-gateway/gateway), fill the placeholders in **application.yml**, then run the application.
3. Open sample application: [resource-server-1](../../../../reactive/webflux/oauth2/spring-cloud-gateway/resource-server-1), fill the placeholders in **application.yml**, then run the application.
4. Open sample application: [resource-server-2](../../../../reactive/webflux/oauth2/spring-cloud-gateway/resource-server-2), fill the placeholders in **application.yml**, then run the application.
5. Open browser(for example: [Edge](https://www.microsoft.com/edge?r=1)), close all [InPrivate window](https://support.microsoft.com/microsoft-edge/browse-inprivate-in-microsoft-edge-cd2c9a48-0bc4-b98e-5e46-ac40c84e27e2), and open a new InPrivate window.
6. Access **http://localhost:8080/**, it will return **Hello, this is client-1.**.
7. Access **http://localhost:8080/resource-server-1**, it will redirect to Microsoft consent page.
9. Input username and password (update password if it requests you to), it will return permission request page: let user permit **client-1** to access **resource-server-1**.
11. Click **Accept**, then it will return **Hello, resource-server-1.**. This means Client can access ResourceServer1 through Gateway successfully.
12. Access **http://localhost:8080/resource-server-2**, consent as before, it will return **Hello, this is resource-server-2.**.

# 5. Homework
1. Read [rfc6749](https://datatracker.ietf.org/doc/html/rfc6749).
