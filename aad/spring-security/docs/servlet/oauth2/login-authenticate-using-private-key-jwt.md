- [1. About](#1-about)
- [2. Get sample applications](#2-get-sample-applications)
- [3. Create resources in Azure](#3-create-resources-in-azure)
	* [3.1. Create a tenant](#31-create-a-tenant)
	* [3.2. Add a new user](#32-add-a-new-user)
	* [3.3. Register client-1](#33-register-client-1)
	* [3.4. Add a redirect URI for client-1](#34-add-a-redirect-uri-for-client-1)
	* [3.5. Add a certificate for client-1](#35-add-a-certificate-for-client-1)
		+ [3.5.1. Create certificate.pem and encrypted-private-key-and-certificate.pfx](#351-create-certificatepem-and-encrypted-private-key-and-certificatepfx)
		+ [3.5.2. Upload certificate](#352-upload-certificate)
- [4. Run sample applications](#4-run-sample-applications)
- [5. Homework](#5-homework)








# 1. About

This section shows the basic scenario:
1. Login by [OAuth 2.0 authorization code flow](https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow) and [request an access token with a certificate credential](https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-oauth2-auth-code-flow#request-an-access-token-with-a-certificate-credential).

# 2. Get sample applications
Get samples applications from in GitHub: [login-authenticate-using-private-key-jwt](../../../servlet/oauth2/login-authenticate-using-private-key-jwt).

# 3. Create resources in Azure

## 3.1. Create a tenant
Read [document about creating an Azure AD tenant](https://docs.microsoft.com/azure/active-directory/develop/quickstart-create-new-tenant#create-a-new-azure-ad-tenant), create a new tenant. Get the tenant-id: **${tenant-id}**.

## 3.2. Add a new user
Read [document about adding users](https://docs.microsoft.com/azure/active-directory/fundamentals/add-users-azure-active-directory), add a new user: **user-1@${tenant-name}.com**. Get the user's password.

## 3.3. Register client-1
Read [document about registering an application](https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app), register an application named **client-1**. Get the client-id: **${client-1-client-id}**.

## 3.4. Add a redirect URI for client-1
Read [document about adding a redirect URI](https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app#add-a-redirect-uri), add redirect URI: **http://localhost:8080/login/oauth2/code/**.

## 3.5. Add a certificate for client-1

### 3.5.1. Create certificate.pem and encrypted-private-key-and-certificate.pfx

Use this command to create **encrypted-private-key.pem**:
```shell
openssl genrsa \
-aes128 \
-passout pass:myPassword1 \
-out encrypted-private-key.pem \
2048
```

Use this command to create **certificate.pem**:
```shell
openssl req \
-new -x509 \
-sha256 \
-subj '/C=GB/ST=London/L=London/O=Global Security/OU=IT Department/CN=example.com' \
-days 365 \
-key encrypted-private-key.pem \
-passin pass:myPassword1 \
-out certificate.pem
```

Use this command to create **encrypted-private-key-and-certificate.pfx**:
```shell
openssl pkcs12 \
-inkey encrypted-private-key.pem \
-passin pass:myPassword1 \
-passout pass:myPassword1 \
-in certificate.pem \
-export \
-out encrypted-private-key-and-certificate.pfx
```

You can refer to [document about create self-signed certificate](https://docs.microsoft.com/en-us/azure/active-directory/develop/howto-create-self-signed-certificate) to get more information.

### 3.5.2. Upload certificate
Read [document about adding a certificate](https://docs.microsoft.com/en-us/azure/active-directory/develop/quickstart-register-app#add-a-certificate), upload certificate **certificate.pem**.

# 4. Run sample applications
 1. Open sample application: [login-authenticate-using-private-key-jwt](../../../servlet/oauth2/login-authenticate-using-private-key-jwt), fill the placeholders in **application.yml**, then run the application.
 2. Open browser(for example: [Edge](https://www.microsoft.com/edge?r=1)), close all [InPrivate window](https://support.microsoft.com/microsoft-edge/browse-inprivate-in-microsoft-edge-cd2c9a48-0bc4-b98e-5e46-ac40c84e27e2), and open a new InPrivate window.
 3. Access **http://localhost:8080**, it will redirect to Microsoft login page. Input username and password (update password if it requests you to), it will return permission request page. click **Accept**, then it will return **Hello, this is client-1.**. This means we log in successfully.
 4. Access **http://localhost:8080/**, it will return **Hello, this is client-1.**, which means login successfully.

# 5. Homework
 1. Read [rfc6749](https://datatracker.ietf.org/doc/html/rfc6749).
 2. Read [document about OAuth 2.0 and OpenID Connect protocols on the Microsoft identity platform](https://docs.microsoft.com/azure/active-directory/develop/active-directory-v2-protocols).
 3. Read [document about Microsoft identity platform and OpenID Connect protocol](https://docs.microsoft.com/azure/active-directory/develop/v2-protocols-oidc)
 4. Read [document about Microsoft identity platform and OAuth 2.0 authorization code flow](https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow).
 5. Read [document about application authentication certificate credentials](https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-certificate-credentials).
 6. . Investigate each item's purpose in the sample projects' **application.yml**.
 7. In [login-authenticate-using-private-key-jwt](../../../servlet/oauth2/login-authenticate-using-private-key-jwt)'s **application.yml**, the property **spring.security.oauth2.client.registration.scope** contains **openid**, **profile**. what will happen if we delete these scopes?



