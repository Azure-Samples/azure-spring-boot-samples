# Azure OAuth 2.0 Sample for Microsoft Entra ID Spring Boot Starter Resource Server

## Key concepts
This sample illustrates how to protect a Java web API by restricting access to its resources to authorized accounts only.

1. Obtain the access token from the HTTP request header.
2. Use `JwtDecoder` to parse the access token into `Jwt`.
3. Verify `aud`, `iss`, `nbf`, `exp` claims in access token.
4. Extract information from JWT in `AadOAuth2AuthenticatedPrincipal` object after a successful verification.
5. Save the `AADOAuth2AuthenticatedPrincipal` into SecurityContext.

### Default authentication and method permissions

This sample uses the default Microsoft Entra ID resource server security filter chain. In Spring Cloud Azure versions that include [Azure/azure-sdk-for-java#51207], the default chain requires a valid bearer token for every request. Method-level permissions are checked in addition to that authentication requirement.

| Endpoint | Requirements with the SDK fix |
| --- | --- |
| `/webapiB` | A valid bearer token and `SCOPE_WebApiB.ExampleScope`. |
| `/user` | A valid bearer token and `SCOPE_User.Read`. |
| `/webapiB/clientCredential` | A valid bearer token, without an additional method-level scope or app-role check. |

The absence of `@PreAuthorize` on `/webapiB/clientCredential` does not make it an anonymous endpoint when using a fixed SDK.

**SDK version requirement:** The [parent POM](../pom.xml) currently pins Spring Cloud Azure `7.4.0`, which does not include this fix. Upgrade `version.spring.cloud.azure` to a released version containing the fix before running the default-authentication checks below. With `7.4.0`, an anonymous request can reach `/webapiB/clientCredential`; the method-level checks on the other two endpoints do not protect this unannotated method.

### Protocol diagram
![Aad resource server protocol diagram](docs/image-add-resource-server.png "Aad resource server protocol diagram")

## Getting started

### Configure Web API
1. In this section, you register your web API in App registrations in the Azure portal.
1. Search for and select your tenant in **Microsoft Entra ID**.
1. Under **Manage** In the same tenant, select **App registrations** -> **New registration**.![Protal manage](docs/image-protal-manage.png "Protal manage")
1. The registered application name is filled into `webapiB`(For better distinguish between [Resource Server] and [Resource Server Obo], this application is named **webapiB**), select **Accounts in this organizational directory only**, click the **register** button.![Register a web api](docs/image-register-a-web-api.png "Register a web api")
1. Under **webapiB** application, select **Certificates & secrets** -> **new client secret**, expires select **Never**, click the **add** button, remember to save the secrets here and use them later.![Creat secrets](docs/image-creat-secrets-api.png "Creat secrets")
1. Under **webapiB** application, select **API permissions** -> **Grant admin consent for ...**, then choose **Yes** for save.
1. Under **webapiB** application, select **Expose an API** -> **Add a scope**, Use the default Application ID URI, click **Save and continue** button.![Set application id url](docs/image-set-application-id-url.png "Set application id url")
1. Wait the page refresh finished. Then set the **Scope name** to `WebApiB.ExampleScope`.![Add a scope](docs/image-add-a-scope.png "Add a scope")
1. Finally, the api exposed in `webapiB`.![Finally, the API exposed in webAPI](docs/image-expose-api.png "Finally, the API exposed in webAPI")
1. Expose an API by adding `appRoles` , See [Example: Application app role] for more information about app roles setting.

    ```json
    {
        "allowedMemberTypes": [
          "Application"
        ],
        "description": "WebApiB ClientCredential Example Scope",
        "displayName": "WebApiB ClientCredential Example Scope",
        "id": "d2bec026-b75f-418d-9493-8462f54f25d9",
        "isEnabled": true, 
        "value": "WebApiB.ClientCredential.ExampleScope"
    }
    ```
   
See [Expose scoped permission to web api] for more information about web api.

## Examples
### Configure application.yml
```yaml
# If we configure the spring.cloud.azure.active-directory.credential.client-id or spring.cloud.azure.active-directory.app-id-uri, then will check the audience.
# In v2.0 tokens, this is always the client ID of the API, while in v1.0 tokens it can be the client ID or the resource URI used in the request.
# If you are using v1.0 tokens, configure both to properly complete the audience validation.

spring:
  cloud:
    azure:
      active-directory:
        enabled: true
        credential:
          client-id: ${AZURE_CLIENT_ID}
        app-id-uri: ${APP_ID_URI}
```

### Run with Maven
```shell
# Under sdk/spring project root directory
cd azure-spring-boot-samples/aad/spring-cloud-azure-starter-active-directory/web-client-access-resource-server/aad-resource-server
mvn spring-boot:run
```

### Access the Web API
We could use Postman to simulate a Web APP to send a request to a Web API.

### Check the authentication and authorization

#### Verify default authentication (fixed SDK required)

After upgrading the SDK as described above, send a request without an access token:

```shell
curl -i http://localhost:8082/webapiB/clientCredential
```

The request must be rejected before reaching the controller:

```text
HTTP/1.1 401
```

Then call the same endpoint with a valid access token for this API, obtained using the client credentials grant:

```shell
curl http://localhost:8082/webapiB/clientCredential -H "Authorization: Bearer <access-token>"
```

Expected response:

```text
Response from webApiB: clientCredential
```

This comparison verifies the default authentication requirement independently of method-level permissions. It does not verify an app-role policy, because this endpoint has no such policy.

#### Verify method permissions

- Web API B response successfully.

1. Get access-token:
```shell script
curl -H "Content-Type: application/x-www-form-urlencoded" -d 'grant_type=password&client_id=<web-apiB-client-id>&scope=<app-id-uri>/Obo.WebApiB.ExampleScope&client_secret=<web-apiB-client-secret>&username=<username>&password=<password>' 'https://login.microsoftonline.com/organizations/oauth2/v2.0/token'
```
2. Access endpoint by access-token:
```shell script
curl localhost:8082/webapiB -H "Authorization: Bearer <access-token>"
```
3. Verify response:
```text
Response from webApiB.
```

- Web API B response failed.

1. Get access-token:
```shell script
curl -H "Content-Type: application/x-www-form-urlencoded" -d 'grant_type=password&client_id=<web-apiB-client-id>&scope=User.Read&client_secret=<web-apiB-client-secret>&username=<username>&password=<password>' 'https://login.microsoftonline.com/organizations/oauth2/v2.0/token'
```
2. Access endpoint by access-token:
```shell script
curl localhost:8082/user -H "Authorization: Bearer <access-token>" -I
```
3. Verify response:
```text
error:401
```

## Troubleshooting

## Next steps
## Contributing
<!-- LINKS -->

[Expose scoped permission to web api]: https://docs.microsoft.com/azure/active-directory/develop/quickstart-configure-app-expose-web-apis
[Resource Server]: ../aad-resource-server
[Resource Server Obo]: ../aad-resource-server-obo
[resource server password credentials]: https://docs.microsoft.com/azure/active-directory/develop/v2-oauth-ropc
[Example: Application app role]: https://docs.microsoft.com/azure/active-directory/develop/howto-add-app-roles-in-azure-ad-apps#example-application-app-role
[Azure/azure-sdk-for-java#51207]: https://github.com/Azure/azure-sdk-for-java/pull/51207
