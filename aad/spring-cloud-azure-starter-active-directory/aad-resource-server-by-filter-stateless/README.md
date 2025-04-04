# Stateless authentication filter sample for Microsoft Entra ID Spring Boot Starter

## Key concepts

This demo project explains the usage of the stateless authentication filter `AadAppRoleStatelessAuthenticationFilter`.
This project is composed of a vue.js frontend and a simple backend with three endpoints
* `/public` (accessible by anyone)
* `/authorized` (role "UserRule" required)
* `/admin/demo` (role "admin" required).

## Getting started
The sample is composed of two layers: vue.js client and Spring Boot RESTful Web Service. You need to make some changes 
to get it working with your Microsoft Entra tenant on both sides.



### Register your application with your Microsoft Entra Tenant

Follow the guide [here](https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app).

### Configure appRoles

In order to use only the `id_token` for our authentication and authorization purposes we will use the
`appRoles` feature which Microsoft Entra ID provides. Follow the guide 
[Add app roles in your application](https://docs.microsoft.com/azure/active-directory/develop/howto-add-app-roles-in-azure-ad-apps)

For the test SPA provided with this example you should create the following roles in your manifest:

```
  "appRoles": [
    {
      "allowedMemberTypes": [
        "User"
      ],
      "description": "Full admin access",
      "displayName": "Admin",
      "id": "2fa848d0-8054-4e11-8c73-7af5f1171001",
      "isEnabled": true,
      "lang": null,
      "origin": "Application",
      "value": "Admin"
    },
    {
      "allowedMemberTypes": [
        "User"
      ],
      "description": "Normal user access",
      "displayName": "UserRule",
      "id": "f8ed78b5-fabc-488e-968b-baa48a570001",
      "isEnabled": true,
      "lang": null,
      "origin": "Application",
      "value": "UserRule"
    }
  ],
```

After you've created the roles, go to **Microsoft Entra ID** and select **Users** to add two new users named "Admin" and "UserRule". Then back to select **Enterprise applications** in the left-hand navigation pane, click on your created application and select **Users and groups**, finally assign the new roles to your new Users (assignment of roles to groups is not available in the free tier of Microsoft Entra ID).

Furthermore, enable the implicit flow in the manifest for the demo application 
(or if you have SPAs calling you):

```
"oauth2AllowImplicitFlow": "true",
```

## Running Sample With Terraform
Please refer to [README.md](terraform/README.md) if you want to start the sample with Terraform in just a few steps.

## Running Sample Step by Step

### Configure the sample

#### Configure application.properties

You have to activate the stateless app-role auth filter and configure the `client-id`of your application registration:

```properties
spring.cloud.azure.active-directory.enabled=true
spring.cloud.azure.active-directory.session-stateless=true
spring.cloud.azure.active-directory.client-id=xxxxxx-your-client-id-xxxxxx
spring.cloud.azure.active-directory.appIdUri=xxxxxx-your-appIDUri-xxxxxx
```

#### Configure Webapp

Add your `tenant-id` and `client-id` in `src/main/resources/static/index.html`:

```json
data: {
  clientId: 'xxxxxxxx-your-client-id-xxxxxxxxxxxx',
  tenantId: 'xxxxxxxx-your-tenant-id-xxxxxxxxxxxx',
  tokenType: 'id_token',
  token: null,
  log: null
},
``` 

### Run with Maven
```shell
cd azure-spring-boot-samples/aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter-stateless
mvn spring-boot:run
```

### Check the authentication and authorization
	
1. Access http://localhost:8080
2. Without logging in try the three endpoints (public, authorized and admin). While the public 
   endpoint should work without a token the other two will return a 403.
3. Insert your `client-id` and `tenant-id` and perform a log in. If successfull the token textarea
   should get populated. Also the token header and token payload field will be populated.   
4. Again access the three endpoints. Depending on your user and the assigned `appRoles` you should
   be able to call the authorized and admin endpoints.
   
#### Demo
![demoonstration video](docs/demo.webp "Demo Video")

## Deploy to Azure Spring Apps

Now that you have the Spring Boot application running locally, it's time to move it to production. [Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/overview) makes it easy to deploy Spring Boot applications to Azure without any code changes. The service manages the infrastructure of Spring applications so developers can focus on their code. Azure Spring Apps provides lifecycle management using comprehensive monitoring and diagnostics, configuration management, service discovery, CI/CD integration, blue-green deployments, and more. To deploy your application to Azure Spring Apps, see [Deploy your first application to Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/quickstart?tabs=Azure-CLI).

## Troubleshooting
## Next steps
## Contributing
<!-- LINKS -->
