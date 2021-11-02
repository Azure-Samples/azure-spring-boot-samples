
# 1. About
Samples in this folder will demonstrate how to use [Azure Active Directory] and [OAuth2] to protect web application developed by [Spring Security].

# 2. Ask for help
If you have any question about these samples, please ask by [creating a new github issue].

# 3. Homeworks
Each sample has homework, you can get the reference answer in [homework reference answer].

# 4. Sample list

## 4.1. Sample 1: Simplest scenario
This sample will demonstrate the simplest scenario:
1. User sign in client and client get [access token] by [OAuth 2.0 authorization code flow].
2. Client access resource-server by [access token].
3. Resource server validate the [access token] by validating the signature, and checking these claims: **aud**, **nbf** and **exp**.



[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[OAuth2]: https://oauth.net/2/
[Spring Security]: https://spring.io/projects/spring-security
[creating a new github issue]: https://github.com/Azure-Samples/azure-spring-boot-samples/issues/new
[homework reference answer]: ./homework-reference-answer.md
[OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[access token]: https://docs.microsoft.com/en-us/azure/active-directory/develop/access-tokens


