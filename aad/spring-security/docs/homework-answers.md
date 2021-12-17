- [1. 01-basic-scenario](#1-01-basic-scenario)
    * [1.1. Question 9](#11-question-9)
- [2. 02-check-permissions-by-claims-in-access-token](#2-02-check-permissions-by-claims-in-access-token)
    * [2.1. Question 1](#21-question-1)
- [3. 03-multiple-resource-server](#3-03-multiple-resource-server)
    * [3.1. Question 1](#31-question-1)




# 1. 01-basic-scenario

## 1.1. Question 9
Question: 
9. In [client-access-resource-server/client]'s **application.yml**, the property **spring.security.oauth2.client.registration.scope** contains **openid** and **profile**. what will happen if we delete **openid** or **scope**?

Answer:
1. If **openid** been deleted, when sign in, it will have error like **AADSTS70011: The provided request must include a 'scope' input parameter. The provided value for the input parameter 'scope' is not valid**.
2. If **profile** been deleted, when sign in, [client-access-resource-server/client] will throw exception like **Missing attribute 'name' in attributes**.
3. If **offline_access** been deleted, user can sign in without any problem. And user can access **http://localhost:8080/resource-server** without any problem. But after one hour, user can not access **http://localhost:8080/resource-server** anymore, because access token has been expired. **offline_access** is used to get refresh token, and refresh token can be used to get another access token when current access token expired.
4. Please refer to [document about OpenID Connect scopes] to get more information.


# 2. 02-check-permissions-by-claims-in-access-token

## 2.1. Question 1
Question:
1. List all scopes that can be used for authorization.

Answer:
1. Claims: **scp**, **roles**, **wids**, **groups**.


# 3. 03-multiple-resource-server

## 3.1. Question 1
Question:
1. If there are 100 clients configured in application.yml, the permission request page will appear 100 times. Please investigate how to reduce the consent page.

Answer:
1. Option 1: Use incremental and dynamic user consent. Refer to [document about incremental and dynamic user consent].
2. Option 2: Use admin consent. Refer to [document about admin consent].
3. Option 3: Configure `pre-authorized client app (PCA)`. Refer to [document about add a scope].
4. Option 4: Permission request page is appeared when request for authorization code. When request for authorization code, scopes from multiple resource. When request for access token, scopes from single resource. Refer to [spring-security#10452] to get more information.








[client-access-resource-server/client]: ../servlet/oauth2/client-access-resource-server/client
[document about OpenID Connect scopes]: https://docs.microsoft.com/azure/active-directory/develop/v2-permissions-and-consent#openid-connect-scopes
[document about incremental and dynamic user consent]: https://docs.microsoft.com/azure/active-directory/develop/v2-permissions-and-consent#incremental-and-dynamic-user-consent
[document about admin consent]: https://docs.microsoft.com/azure/active-directory/develop/v2-permissions-and-consent#admin-consent
[document about add a scope]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-expose-web-apis#add-a-scope
[spring-security#10452]: https://github.com/spring-projects/spring-security/issues/10452

