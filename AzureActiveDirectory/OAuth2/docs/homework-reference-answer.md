# sample-01-simplest-scenario
9. In [sample-01-client]'s **application.yml**, the property **spring.security.oauth2.client.registration.scope** contains **openid** and **profile**. what will happen if we delete **openid** or **scope**?
 - If **openid** been deleted, when sign in, it will have error like **AADSTS70011: The provided request must include a 'scope' input parameter. The provided value for the input parameter 'scope' is not valid**.
 - If **profile** been deleted, when sign in, [sample-01-client] will throw exception like **Missing attribute 'name' in attributes**.
 - If **offline_access** been deleted, user can sign in without any problem. And user can access **http://localhost:8080/resource-server** without any problem. But after one hour, user can not access **http://localhost:8080/resource-server** anymore, because access token has been expired. **offline_access** is used to get refresh token, and refresh token can be used to get another access token when current access token expired.
 - Please refer to [document about OpenID Connect scopes] to get more information.





[sample-01-client]: ../sample-01-simplest-scenario/sample-01-client
[document about OpenID Connect scopes]: https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-permissions-and-consent#openid-connect-scopes


