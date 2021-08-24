---
page_type: sample
languages:
- java
products:
- azure-active-directory-b2c
description: "Sample project for Azure AD B2C Spring Boot client library"
urlFragment: "aad-b2c-web-application"
---

# Sample for Azure AD B2C Spring Boot client library for Java

## Key concepts
This example illustrates how to use Azure AD B2C and Spring Security to work together as web application.

## Getting started

### Create your Azure Active Directory B2C tenant

Follow the guide of [AAD B2C tenant creation](https://docs.microsoft.com/azure/active-directory-b2c/tutorial-create-tenant).

### Register your Azure Active Directory B2C application

Follow the guide of [AAD B2C application registry](https://docs.microsoft.com/azure/active-directory-b2c/tutorial-register-applications).
Please ensure that your b2c application's `Redirect URL` is configured to `http://localhost:8080/login/oauth2/code/`.

### Create user flows

Follow the guide of [AAD B2C user flows creation](https://docs.microsoft.com/azure/active-directory-b2c/tutorial-create-user-flows).

### Enable forgot password
Follow the guide of [AAD B2C set up a password reset flow](https://docs.microsoft.com/en-us/azure/active-directory-b2c/add-password-reset-policy?pivots=b2c-user-flow)

## Examples
### Configure the sample

#### application.yml

1. Fill in `${your-tenant-authorization-server-base-uri}` from **Azure AD B2C** portal `App registrations` blade, select **Endpoints**, copy the base endpoint uri(Global cloud format may looks like
`https://{your-tenant-name}.b2clogin.com/{your-tenant-name}.onmicrosoft.com`, China Cloud looks like `https://{your-tenant-name}.b2clogin.cn/{your-tenant-name}.partner.onmschina.cn`). 
2. Fill in `${your-sign-up-or-sign-in-flow-name}` with your sign up or sign in flow name.
3. Select one registered instance under `Applications` from portal, and then:
    1. Fill in `${your-client-id}` from `Application ID`.
    2. Fill in `${your-client-secret}` from one of `Keys`.

```yaml
spring:
  security:
    oauth2:
      client:
        provider:
          sign-up-or-sign-in:
            authorization-uri: ${your-tenant-authorization-server-base-uri}/${your-sign-up-or-sign-in-flow-name}/oauth2/v2.0/authorize
            token-uri: ${your-tenant-authorization-server-base-uri}/oauth2/v2.0/token?p=${your-sign-up-or-sign-in-flow-name}
            jwk-set-uri: ${your-tenant-authorization-server-base-uri}/discovery/v2.0/keys?p=${your-sign-up-or-sign-in-flow-name}
        registration:
          sign-up-or-sign-in:
            provider: sign-up-or-sign-in
            client-id: ${your-client-id}
            client-secret: ${your-client-secret}
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/"
            scope: ${your-client-id}, openid, profile, offline_access
```

#### WebSecurityConfiguration.java

1. Fill in `${your-tenant-authorization-server-base-uri}` from **Azure AD B2C** portal `App registrations` blade, select **Endpoints**, copy the base endpoint uri(Global cloud format may looks like
`https://{your-tenant-name}.b2clogin.com/{your-tenant-name}.onmicrosoft.com`, China Cloud looks like `https://{your-tenant-name}.b2clogin.cn/{your-tenant-name}.partner.onmschina.cn`). 
2. Fill in `${your-sign-up-or-sign-in-flow-name}` with your sign up or sign in flow name.

```java
    http
        .authorizeRequests()
            .regexMatchers("/login").permitAll()
            .anyRequest().authenticated()
            .and()
        .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("${your-tenant-authorization-server-base-uri}/oauth2/v2.0/logout?post_logout_redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Flogin&p=${your-sign-up-or-sign-in-flow-name}")
            .and()
        .oauth2Login();
```

**NOTE**: This uri is to help clear the cache when exiting, and can be added in a suitable place

#### home.html
1. Fill in `${your-tenant-authorization-server-base-uri}` from **Azure AD B2C** portal `App registrations` blade, select **Endpoints**, copy the base endpoint uri(Global cloud format may looks like
`https://{your-tenant-name}.b2clogin.com/{your-tenant-name}.onmicrosoft.com`, China Cloud looks like `https://{your-tenant-name}.b2clogin.cn/{your-tenant-name}.partner.onmschina.cn`). 
2. Fill in `${your-client-id}` with your client id.
3. Fill in `${your-profile-edit-user-flow-name}` with your profile edit user flow name.
4. Fill in `${your-password-reset-user-flow-name}` with your password reset user flow name.

```html
    <form style="display:inline" method="post" action="${your-tenant-authorization-server-base-uri}/${your-profile-edit-user-flow-name}/oauth2/v2.0/authorize?response_type=code&client_id=${your-client-id}&scope=${your-client-id}%20openid%20profile%20offline_access&redirect_uri=http://localhost:8080/oauth2/authorization/sign-up-or-sign-in">
        <button class="btn btn-md btn-primary btn-block" type="submit">Profile edit</button>
    </form>

    <form style="display:inline" method="post" action="${your-tenant-authorization-server-base-uri}/${your-password-reset-user-flow-name}/oauth2/v2.0/authorize?response_type=code&client_id=${your-client-id}&scope=${your-client-id}%20openid%20profile%20offline_access&redirect_uri=http://localhost:8080/oauth2/authorization/sign-up-or-sign-in">
        <button class="btn btn-md btn-primary btn-block" type="submit">Password reset</button>
    </form>
```

**NOTE**: There is a redirect uri at the end of the uri : `http://localhost:8080/oauth2/authorization/sign-up-or-sign-in`, which is filled in according to the client name 
          you configured. You can also replace the `sign-up-or-sign-in` with a custom client name.
          **You also need to add the redirect uri to your b2c application to make it work.**

### Run with Maven
```
cd azure-spring-boot-samples/aad/aad-b2c-with-spring-security/aad-b2c-web-application
mvn spring-boot:run
```

### Validation

1. Access `http://localhost:8080/` as index page.
2. Sign up/in.
3. Profile edit.
4. Password reset.
5. Log out.
6. Sign in.

## Troubleshooting
- `Missing attribute 'name' in attributes `

  ```
  java.lang.IllegalArgumentException: Missing attribute 'name' in attributes
  	at org.springframework.security.oauth2.core.user.DefaultOAuth2User.<init>(DefaultOAuth2User.java:67) ~[spring-security-oauth2-core-5.3.6.RELEASE.jar:5.3.6.RELEASE]
  	at org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser.<init>(DefaultOidcUser.java:89) ~[spring-security-oauth2-core-5.3.6.RELEASE.jar:5.3.6.RELEASE]
  	at org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService.loadUser(OidcUserService.java:144) ~[spring-security-oauth2-client-5.3.6.RELEASE.jar:5.3.6.RELEASE]
  	at org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService.loadUser(OidcUserService.java:63) ~[spring-security-oauth2-client-5.3.6.RELEASE.jar:5.3.6.RELEASE]
  ```

  While running sample, if error occurs with logs above:

  - make sure that while creating user workflow by following this [guide](https://docs.microsoft.com/azure/active-directory-b2c/tutorial-create-user-flows), for **User attributes and claims** , attributes and claims for **Display Name** should be chosen.

### FAQ

#### Sign in with loops to B2C endpoint ?
This issue almost due to polluted cookies of `localhost`. Clean up cookies of `localhost` and try it again.

#### More identity providers from AAD B2C login ?
Follow the guide of [Set up Google account with AAD B2C](https://docs.microsoft.com/azure/active-directory-b2c/active-directory-b2c-setup-goog-app).
And also available for Amazon, Azure AD, FaceBook, Github, Linkedin and Twitter.

## Next steps
## Contributing
<!-- LINKS -->

