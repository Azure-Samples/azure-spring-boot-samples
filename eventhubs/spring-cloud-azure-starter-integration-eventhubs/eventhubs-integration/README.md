---
page_type: sample
languages:
- java
products:
- azure-event-hubs
description: "Azure Spring Cloud Sample project for Event Hub Integration client library"
urlFragment: "azure-spring-integration-eventhubs-sample"
---

# Spring Cloud Azure Event Hubs Integration Code Sample shared library for Java

## Key concepts

This sample demonstrates how to use Spring Integration for Azure
Event Hubs.


## Getting started

Running this sample will be charged by Azure. You can check the usage and bill at
[this link][azure-account].


### Create Azure resources

1. Create [Azure Event Hubs][create-event-hubs].
    After creating the Azure Event Hubs, you
    can create your own Consumer Group or use the default "$Default" Consumer Group.

2. Create [Azure Storage][create-azure-storage] for checkpoint use.

### Configuration credential options

We have several ways to config the Spring Integration for Service
Bus. You can choose anyone of them.

>[!Important]
>
>  When using the Restful API to send messages, the **Active profiles** must contain `manual`.
> 
#### Method 1: Connection string based usage

1. Update [application.yaml]. If you choose to use
   service principal or managed identity, update the `application-sp.yaml` or
   `application-mi.yaml` respectively.
    ```yaml
    spring:
        cloud:
            azure:
                eventhubs:
                    connection-string: [connection-string]
                    processor:
                        checkpoint-store:
                          container-name: [container-name]
                          account-name: [account-name]
                          account-key: [account-key]
    ```

1.  Update event hub name and consumer group in
    [ReceiveController][receive-controller] and [SendController][send-controller].

#### Method 2: Service principal based usage

1. Create a service principal for use in by your app. Please follow
    [create service principal from Azure CLI][create-sp-using-azure-cli].

2. Add Role Assignment for Event Hubs. See
    [Service principal for Azure resources with Event Hubs][role-assignment]
    to add role assignment for Event Hubs. Assign `Contributor` role for event hubs.

3. Update [application-sp.yaml][application-sp.yaml].
    ```yaml
    spring:
      cloud:
        azure:
          profile:
            tenant-id: [tenant-id]
          credential:
            client-id: [client-id]
            client-secret: [client-secret]
          eventhubs:
            resource:
              resource-group: [resource-group]
            namespace: [namespace]
            processor:
              checkpoint-store:
                container-name: [container-name]
                account-name: [account-name]
    ```
    > We should specify `spring.profiles.active=sp` to run the Spring Boot application.
    For App Service, please add a configuration entry for this.
#### Method 3: MSI credential based usage

##### Set up managed identity

Please follow [create managed identity][create-managed-identity] to set up managed identity.

##### Add Role Assignment for Event Hubs

1.  See [Managed identities for Azure resources with Event Hubs][role-assignment]
    to add role assignment for Event Hubs. Assign `Contributor` role for managed identity.


##### Update MSI related properties

1.  Update [application-mi.yaml][application-mi.yaml].
    ```yaml
    spring:
      cloud:
        azure:
          credential:
            managed-identity-client-id: [ managed-identity-client-id ]
          profile:
            tenant-id: [ tenant-id ]
          #        msi-enabled: true
          #        client-id: [the-id-of-managed-identity]
          #        resource-group: [ resource-group ]
          #        subscription-id: [ subscription-id ]
          eventhubs:
            namespace: [eventhub-namespace]
            processor:
              checkpoint-store:
                container-name: [container-name]
                account-name: [account-name]
    ```
    > We should specify `spring.profiles.active=mi` to run the Spring Boot application.
    For App Service, please add a configuration entry for this.

##### Redeploy Application

If you update the `spring.cloud.azure.credential.managed-identity-client-id`
property after deploying the app, or update the role assignment for
services, please try to redeploy the app again.

> You can follow
> [Deploy a Spring Boot JAR file to Azure App Service][deploy-spring-boot-application-to-app-service]
> to deploy this application to App Service

## Examples

1. Run the `mvn spring-boot:run` in the root of the code sample to get the app running.

2. Send a POST request

        $ ~~curl -X POST http://localhost:8080/messages?message=hello~~

3. Verify in your app’s logs that a similar message was posted:

        New message received: 'hello'
        Message 'hello' successfully checkpointed

4. Delete the resources on [Azure Portal][azure-portal] to avoid unexpected charges.


## Troubleshooting

## Next steps

## Contributing

[azure-account]: https://azure.microsoft.com/account/
[azure-portal]: https://ms.portal.azure.com/
[create-event-hubs]: https://docs.microsoft.com/azure/event-hubs/
[create-azure-storage]: https://docs.microsoft.com/azure/storage/
[create-managed-identity]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/create-managed-identity.md
[create-sp-using-azure-cli]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/create-sp-using-azure-cli.md
[eventhub-operation]: https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-boot_3.6.0/sdk/spring/azure-spring-integration-eventhubs/src/spring-cloud-azure_4.0/java/com/azure/spring/integration/eventhub/api/EventHubOperation.java

[receive-controller]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/eventhubs/spring-cloud-azure-starter-integration-eventhubs/eventhubs-integration/src/spring-cloud-azure_4.0/java/com/azure/spring/sample/eventhubs/ReceiveController.java
[send-controller]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/eventhubs/spring-cloud-azure-starter-integration-eventhubs/eventhubs-integration/src/spring-cloud-azure_4.0/java/com/azure/spring/sample/eventhubs/SendController.java
[application.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/eventhubs/spring-cloud-azure-starter-integration-eventhubs/eventhubs-integration/src/spring-cloud-azure_4.0/resources/application.yaml

[deploy-spring-boot-application-to-app-service]: https://docs.microsoft.com/java/azure/spring-framework/deploy-spring-boot-java-app-with-maven-plugin?toc=%2Fazure%2Fapp-service%2Fcontainers%2Ftoc.json&view=azure-java-stable
