- [1. About](#1-about)
- [2. Ask for help](#2-ask-for-help)
- [3. Homeworks](#3-homeworks)
- [4. Sample list](#4-sample-list)
    * [4.1. sample-01-basic-scenario](#41-sample-01-basic-scenario)
    * [4.2. sample-02-check-permissions-by-claims-in-access-token](#42-sample-02-check-permissions-by-claims-in-access-token)
    * [4.3. sample-03-multiple-resource-server](#43-sample-03-multiple-resource-server)
    * [4.4. sample-04-on-behalf-of-flow](#44-sample-04-on-behalf-of-flow)







# 1. About
[Azure Active Directory] (Azure AD) provides comprehensive OAuth 2 support. This section demonstrates how leverage [Spring Security] to integrate Azure AD's OAuth2 feature into your servlet based application.

# 2. Ask for help
If you have any question about these samples, please ask by [creating a new github issue].

# 3. Homeworks
Each sample has homework, you can get the answer in [homework answers].

# 4. Sample list

## 4.1. sample-01-basic-scenario
[sample-01-basic-scenario] will demonstrate the basic scenario:
1. User sign in client and client get [access token] by [OAuth 2.0 authorization code flow].
2. Client access resource-server by [access token].
3. Resource server validate the [access token] by validating the signature, and checking these claims: **aud**, **nbf** and **exp**.

## 4.2. sample-02-check-permissions-by-claims-in-access-token
[sample-02-check-permissions-by-claims-in-access-token] will demonstrate this scenario:
1. User sign in client and client get [access token] by [OAuth 2.0 authorization code flow].
2. Client check whether current signed-in user has permission to specific method.
3. Client access resource-server by [access token].
4. Resource server validate the [access token] by validating the signature, and checking these claims: **aud**, **nbf** and **exp**.
5. Resource server check whether current signed-in user has permission to specific method.

## 4.3. sample-03-multiple-resource-server
[sample-03-multiple-resource-server] will demonstrate this scenario:
1. One client application access multiple resource servers.
2. Consent scopes when request for specific endpoint.
3. Consent all scopes when request for a specific endpoint.

## 4.4. sample-04-on-behalf-of-flow
[sample-04-on-behalf-of-flow] will demonstrate this scenario:
- User sign in client and client get [access token] by [OAuth 2.0 authorization code flow].
- Client access resource-server-1 by [access token].
- resource-server-1 validate the [access token] by validating the signature, and checking these claims: `aud`, `nbf` and `exp`.
- resource-server-1 use the access token to get a new access token by [on behalf of flow].
- resource-server-1 use the new access token to access resource-server-2.
- resource-server-2 validate the [access token] by validating the signature, and checking these claims: `aud`, `nbf` and `exp`.




[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[OAuth2]: https://oauth.net/2/
[authorization server]: https://datatracker.ietf.org/doc/html/rfc6749#section-1.1
[Spring Security]: https://spring.io/projects/spring-security
[creating a new github issue]: https://github.com/Azure-Samples/azure-spring-boot-samples/issues/new
[homework answers]: homework-answers.md
[OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[access token]: https://docs.microsoft.com/azure/active-directory/develop/access-tokens
[on behalf of flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-on-behalf-of-flow
[sample-01-basic-scenario]: ./sample-01-basic-scenario.md
[sample-02-check-permissions-by-claims-in-access-token]: ./sample-02-check-permissions-by-claims-in-access-token.md
[sample-03-multiple-resource-server]: ./sample-03-multiple-resource-server.md
[sample-04-on-behalf-of-flow]: ./sample-04-on-behalf-of-flow.md


