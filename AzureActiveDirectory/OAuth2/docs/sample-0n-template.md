- [1. About](#1-about)
- [2. Create sample applications](#2-create-sample-applications)
    * [2.1. Client](#21-client)
        + [2.1.1. pom.xml](#211-pomxml)
        + [2.1.2. Java classes](#212-java-classes)
        + [2.1.3. application.yml](#213-applicationyml)
    * [2.2. Resource server](#22-resource-server)
        + [2.2.1. pom.xml](#221-pomxml)
        + [2.2.2. Java classes](#222-java-classes)
        + [2.2.3. application.yml](#223-applicationyml)
- [3. Create resources in Azure](#3-create-resources-in-azure)
- [4. Run sample applications](#4-run-sample-applications)
- [5. Homework](#5-homework)



# 1. About

Please refer to [Azure Active Directory OAuth2 samples] to get all samples about using [Azure Active Directory] and [OAuth2] to protect web application developed by [Spring Security].

This sample will demonstrate this scenario:
- User sign in client and client get [access token] by [OAuth 2.0 authorization code flow].
- Client access resource-server by [access token].
- Resource server validate the [access token] by validating the signature, and checking these claims: `aud`, `nbf` and `exp`.

# 2. Create sample applications
You can follow the following steps to create sample applications, or you can use samples in GitHub: [sample-n-xxx].

## 2.1. Client

### 2.1.1. pom.xml

### 2.1.2. Java classes

### 2.1.3. application.yml

## 2.2. Resource server

### 2.2.1. pom.xml

### 2.2.2. Java classes

### 2.2.3. application.yml

# 3. Create resources in Azure

# 4. Run sample applications

# 5. Homework





[Azure Active Directory OAuth2 samples]: ../README.md
[Azure Active Directory]: https://azure.microsoft.com/services/active-directory/
[OAuth2]: https://oauth.net/2/
[Spring Security]: https://spring.io/projects/spring-security
[OAuth 2.0 authorization code flow]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth2-auth-code-flow
[access token]: https://docs.microsoft.com/en-us/azure/active-directory/develop/access-tokens
[sample-0n-xxx]: ../sample-0n-xxx