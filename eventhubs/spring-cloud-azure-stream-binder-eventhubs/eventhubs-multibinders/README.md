# Spring Cloud Azure Stream Binder for Multiple Event Hubs Namespace Code Sample shared library for Java

## Key concepts
This sample demonstrates how to use the `Spring Cloud Stream Binder`
for multiple `Azure Event Hubs` namespaces. In this sample you will bind to
two Event Hubs namespaces separately through two binders. The sample app has two operating modes.
One way is to expose a Restful API to receive string message, another way is to automatically provide string messages.
These messages are published to one event hub. The sample will also consume messages from the same 
event hub.

## Getting started

Running this sample will be charged by Azure. You can check the usage
and bill at [this link][azure-account].



### Create Azure resources

1. Create two Event Hubs in different Event Hub namespace. Please refer to 
    [Azure Event Hubs][create-event-hubs].

2. Create [Azure Storage][create-azure-storage] for checkpoint use.

### Configuration credential options

We have several ways to config the Spring Cloud Stream Binder for Azure
Event Hubs. You can choose anyone of them.

>[!Important]
>
>  When using the Restful API to send messages, the **Active profiles** must contain `manual`.

#### Method 1: Connection string based usage

1.  Update stream binding related properties in
    [application.yaml][application.yaml].

    ```yaml
    spring:
      profiles:
        active: manual
      cloud:
        stream:
          function:
            definition: consume1;supply1;consume2;supply2
          bindings:
            consume1-in-0:
              destination: ${AZURE_EVENTHUB_1_NAME}
              group:  ${AZURE_EVENTHUB_CONSUMER_GROUP}
            supply1-out-0:
              destination: ${THE_SAME_EVENTHUB_1_NAME_AS_ABOVE]
            consume2-in-0:
              binder: eventhub-2
              destination: ${AZURE_EVENTHUB_2_NAME}
              group: ${AZURE_EVENTHUB_CONSUMER_GROUP}
            supply2-out-0:
              binder: eventhub-2
              destination: ${THE_SAME_EVENTHUB_2_NAME_AS_ABOVE}
          binders:
            eventhub-1:
              type: eventhubs
              default-candidate: true
              environment:
                spring:
                  cloud:
                    azure:
                      eventhubs:
                        connection-string: ${CONNECTION_STRING_OF_FIRST_EVENTHUB_NAMESPACE}
                        processor:
                          checkpoint-store:
                            container-name: ${AZURE_STORAGE_CONTAINER_NAME}
                            account-name: ${AZURE_STORAGE_ACCOUNT_NAME}
                            account-key:  ${AZURE_STORAGE_ACCOUNT_KEY}
            eventhub-2:
              type: eventhubs
              default-candidate: false
              environment:
                spring:
                  cloud:
                    azure:
                      eventhubs:
                        connection-string: ${CONNECTION_STRING_OF_SECOND_EVENTHUB_NAMESPACE}
                        processor:
                          checkpoint-store:
                            container-name: ${AZURE_STORAGE_CONTAINER_2_NAME}
                            account-name: ${AZURE_STORAGE_ACCOUNT_2_NAME}
                            account-key:  ${AZURE_STORAGE_ACCOUNT_2_KEY}
          eventhubs:
            bindings:
              consume1-in-0:
                consumer:
                  checkpoint:
                    mode: MANUAL
              consume2-in-0:
                consumer:
                  checkpoint:
                    mode: MANUAL
          poller:
            initial-delay: 0
            fixed-delay: 1000
    ```
> The **defaultCandidate** configuration item:
Whether the binder configuration is a candidate for being considered a
default binder, or can be used only when explicitly referenced. This
allows adding binder configurations without interfering with the default
processing.

>[!Important]
>
>  When using the Restful API to send messages, the **Active profiles** must contain `manual`.

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
            tenant-id: ${AZURE_TENANT_ID}
          credential:
            client-id: ${AZURE_CLIENT_ID}
            client-secret: ${AZURE_CLIENT_SECRET}
        stream:
          function:
            definition: consume1;supply1;consume2;supply2
          bindings:
            consume1-in-0:
              destination: ${AZURE_EVENTHUB_1_NAME}
              group: ${AZURE_EVENTHUB_CONSUMER_GROUP}
            supply1-out-0:
              destination: ${THE_SAME_EVENTHUB_1_NAME_AS_ABOVE]
            consume2-in-0:
              binder: eventhub-2
              destination: ${AZURE_EVENTHUB_2_NAME}
              group: ${AZURE_EVENTHUB_CONSUMER_GROUP}
            supply2-out-0:
              binder: eventhub-2
              destination: ${THE_SAME_EVENTHUB_2_NAME_AS_ABOVE}
          binders:
            eventhub-1:
              type: eventhub
              default-candidate: true
              environment:
                spring:
                  cloud:
                    azure:
                      eventhubs:
                        namespace: ${FIRST_EVENTHUB_NAMESPACE}
                        processor:
                          checkpoint-store:
                            container-name: ${AZURE_STORAGE_CONTAINER_NAME}
                            account-name: ${AZURE_STORAGE_ACCOUNT_NAME}
            eventhub-2:
              type: eventhub
              default-candidate: false
              environment:
                spring:
                  cloud:
                    azure:
                      eventhubs:
                        namespace: ${SECOND_EVENTHUB_NAMESPACE}
                        processor:
                          checkpoint-store:
                            container-name: ${AZURE_STORAGE_CONTAINER_NAME}
                            account-name: ${AZURE_STORAGE_ACCOUNT_NAME}
          eventhubs:
            bindings:
              consume1-in-0:
                consumer:
                  checkpoint:
                    mode: MANUAL
              consume2-in-0:
                consumer:
                  checkpoint:
                    mode: MANUAL
          poller:
            initial-delay: 0
            fixed-delay: 1000
    ```    
   > We should specify `spring.profiles.active=sp` to run the Spring Boot application.
   For App Service, please add a configuration entry for this.

#### Method 3: MSI credential based usage

1. Set up managed identity

Please follow [create managed identity][create-managed-identity] to set up managed identity.

2. Add Role Assignment for Event Hubs

See [Managed identities for Azure resources with Event Hubs][role-assignment]
    to add role assignment for Event Hubs. Assign `Contributor` role for managed identity.

3. Update [application-mi.yaml][application-mi.yaml].
    ```yaml
    spring:
      cloud:
        azure:
          credential:
            managed-identity-client-id: ${AZURE_MANAGED_IDENTITY_CLIENT_ID}
          profile:
            tenant-id: : ${AZURE_TENANT_ID}
        stream:
          function:
            definition: consume1;supply1;consume2;supply2
          bindings:
            consume1-in-0:
              destination:  ${AZURE_EVENTHUB_1_NAME}
              group: ${AZURE_EVENTHUB_CONSUMER_GROUP}
            supply1-out-0:
              destination: ${THE_SAME_EVENTHUB_1_NAME_AS_ABOVE}
            consume2-in-0:
              binder: eventhub-2
              destination: ${AZURE_EVENTHUB_2_NAME}
              group: ${AZURE_EVENTHUB_CONSUMER_GROUP}
            supply2-out-0:
              binder: eventhub-2
              destination: ${THE_SAME_EVENTHUB_2_NAME_AS_ABOVE}
    
          binders:
            eventhub-1:
              type: eventhubs
              default-candidate: true
              environment:
                spring:
                  cloud:
                    azure:
                      eventhubs:
                        namespace: ${FIRST_EVENTHUB_NAMESPACE}
                        processor:
                          checkpoint-store:
                            container-name: ${AZURE_STORAGE_CONTAINER_NAME}
                            account-name: ${AZURE_STORAGE_ACCOUNT_NAME}
            eventhub-2:
              type: eventhubs
              default-candidate: false
              environment:
                spring:
                  cloud:
                    azure:
                      eventhubs:
                        namespace: ${SECOND_EVENTHUB_NAMESPACE}
                        processor:
                          checkpoint-store:
                            container-name: ${AZURE_STORAGE_CONTAINER_NAME}
                            account-name: ${AZURE_STORAGE_ACCOUNT_NAME}
          eventhubs:
            bindings:
              consume1-in-0:
                consumer:
                  checkpoint:
                    mode: MANUAL
              consume2-in-0:
                consumer:
                  checkpoint:
                    mode: MANUAL
          poller:
            initial-delay: 0
            fixed-delay: 1000
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

1.  Run the `mvn clean spring-boot:run` in the root of the code sample
    to get the app running.

1.  Send a POST request to test the default binder

        $ curl -X POST http://localhost:8080/messages1?message=hello

1.  Verify in your app’s logs that a similar message was posted:

        [1] New message1 received: 'hello'
        [1] Message1 'hello' successfully checkpointed

1.  Send another POST request to test the other binder

        $ curl -X POST http://localhost:8080/messages2?message=hello

1.  Verify in your app’s logs that a similar message was posted:

        [2] New message2 received: 'hello'
        [2] Message2 'hello' successfully checkpointed

6.  Delete the resources on [Azure Portal][azure-portal]
    to avoid unexpected charges.

## Troubleshooting

## Next steps

## Contributing


<!-- LINKS -->
[azure-account]: https://azure.microsoft.com/account/
[azure-portal]: https://ms.portal.azure.com/
[create-event-hubs]: https://docs.microsoft.com/azure/event-hubs/
[create-azure-storage]: https://docs.microsoft.com/azure/storage/
[create-sp-using-azure-cli]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/create-sp-using-azure-cli.md
[create-managed-identity]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/create-managed-identity.md
[deploy-spring-boot-application-to-app-service]: https://docs.microsoft.com/java/azure/spring-framework/deploy-spring-boot-java-app-with-maven-plugin?toc=%2Fazure%2Fapp-service%2Fcontainers%2Ftoc.json&view=azure-java-stable
[deploy-to-app-service-via-ftp]: https://docs.microsoft.com/azure/app-service/deploy-ftp
[managed-identities]: https://docs.microsoft.com/azure/active-directory/managed-identities-azure-resources/
[role-assignment]: https://docs.microsoft.com/azure/role-based-access-control/role-assignments-portal
[application-mi.yaml]: src/main/resources/application-mi.yaml
[application.yaml]: src/main/resources/application.yaml
[application-sp.yaml]: src/main/resources/application-sp.yaml
[deploy-spring-boot-application-to-app-service]: https://docs.microsoft.com/java/azure/spring-framework/deploy-spring-boot-java-app-with-maven-plugin?toc=%2Fazure%2Fapp-service%2Fcontainers%2Ftoc.json&view=azure-java-stable