---
page_type: sample
languages:
- java
products:
- azure-event-hubs
description: "Azure Spring Cloud Stream Binder Sample project for Event Hub client library"
urlFragment: "spring-cloud-azure-stream-binder-eventhubs"
---

# Spring Cloud Azure Stream Binder for Event Hubs Sample shared library for Java

## Key concepts

This code sample demonstrates how to use the `Spring Cloud Stream Binder` for `Azure Event Hubs`.The 
sample app has two operating modes.
One way is to expose a Restful API to receive string message, another way is to automatically provide string messages.
These messages are published to one `Event Hub` instance and then consumed by one consumer 
endpoint from the same application.

## Getting started

Running this sample will be charged by Azure. You can check the usage and bill at 
[here][azure-account].

### Create Azure resources

1. Create [Azure Event Hubs][create-event-hubs].
    Please note `Basic` tier is unsupported. After creating the Azure Event Hub, you
    can create your own Consumer Group or use the default "$Default" Consumer Group.

2. Create [Azure Storage][create-azure-storage] for checkpoint usage.

### Configuration credential options

We have several ways to config the Spring Cloud Stream Binder for Azure
Event Hubs. You can choose anyone of them.

>[!Important]
>
>  When using the Restful API to send messages, the **Active profiles** must contain `manual`.

#### Method 1: Connection string based usage

1.  Update [application.yaml][application.yaml].

    ```yaml
    spring:
      cloud:
        azure:
          eventhubs:
            # Fill event hub namespace connection string copied from portal
            connection-string: [eventhub-namespace-connection-string] 
            # Fill checkpoint storage account name, access key and container 
            processor:
              checkpoint-store:
                container-name: [checkpoint-container]
                account-name: [checkpoint-storage-account]
                account-key: [checkpoint-access-key]
        stream:
          function:
            definition: consume;supply
          bindings:
            consume-in-0:
              destination: [eventhub-name]
              group: [consumer-group]
            supply-out-0:
              destination: [the-same-eventhub-name-as-above]
    ```
    
#### Using Batch Consuming
To enable [batch consuming][spring-cloud-stream-batch0-consumer] feature, you should add below configuration in the `batch` profile.
```yaml
spring:
  cloud:
    azure:
      eventhubs:
        connection-string:  [connection-string]
        processor:
          checkpoint-store:
            container-name: [container-name]
            account-name: [account-name]
            account-key: [account-key]
    stream:
      function:
        definition: consume;supply
      bindings:

        consume-in-0:
          content-type: text/plain # string message must specify this value
          destination: [event hub destination]
          group: [event hub group]
          consumer:
            batch-mode: true
        supply-out-0:
          destination: [same as above destination]
      eventhubs:
        bindings:
          consume-in-0:
            consumer:
              batch:
                max-size: 10 # The default value is 10
                max-wait-time: 1m # Optional, the default value is null
              checkpoint:
                mode: BATCH # BATCH | MANUAL

```
For checkpointing mode as BATCH, you can use below code to send messages and consume in batches, 
see the [BatchProducerAndConsumerConfiguration.java][BatchProducerAndConsumerConfiguration]
```java
    @Bean
    public Consumer<List<String>> consume() {
        return list -> list.forEach(event -> LOGGER.info("New event received: '{}'",event));
    }

    @Bean
    public Supplier<Message<String>> supply() {
        return () -> {
            LOGGER.info("Sending message, sequence " + i);
            return MessageBuilder.withPayload("\"Hello world"+ i++ +"\"").build();
        };
    }
```

For checkpointing mode as MANUAL, you can use below code to send messages and consume/checkpoint in batches.
```java
    @Bean
    public Consumer<Message<List<String>>> consume() {
        return message -> {
            for (int i = 0; i < message.getPayload().size(); i++) {
                LOGGER.info("New message received: '{}', partition key: {}, sequence number: {}, offset: {}, enqueued time: {}",
                    message.getPayload().get(i),
                    ((List<Object>) message.getHeaders().get(EventHubHeaders.PARTITION_KEY)).get(i),
                    ((List<Object>) message.getHeaders().get(EventHubHeaders.SEQUENCE_NUMBER)).get(i),
                    ((List<Object>) message.getHeaders().get(EventHubHeaders.OFFSET)).get(i),
                    ((List<Object>) message.getHeaders().get(EventHubHeaders.ENQUEUED_TIME)).get(i));
            }
        
            Checkpointer checkpointer = (Checkpointer) message.getHeaders().get(CHECKPOINTER);
            checkpointer.success()
                        .doOnSuccess(success -> LOGGER.info("Message '{}' successfully checkpointed", message.getPayload()))
                        .doOnError(error -> LOGGER.error("Exception found", error))
                        .subscribe();
        };
    }

    @Bean
    public Supplier<Message<String>> supply() {
        return () -> {
            LOGGER.info("Sending message, sequence " + i);
            return MessageBuilder.withPayload("\"Hello world"+ i++ +"\"").build();
        };
    }
```
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
            tenant-id: [ tenant-id ]
          credential:
            client-id: [ client-id ]
            client-secret: [ client-secret ]
          resource-group: ${resource-group]
          eventhubs:
            resource:
              resource-group: [ resource-group ]
            namespace: [eventhub-namespace]
            processor:
              checkpoint-store:
                container-name: [ container-name ]
                account-name: [ account-name ]
        stream:
          function:
            definition: consume;supply
          bindings:
            consume-in-0:
              destination: [eventhub-name]
              group: [consumer-group]
            supply-out-0:
              destination: [the-same-eventhub-name-as-above]
          eventhubs:
            bindings:
              consume-in-0:
                consumer:
                  checkpoint-mode: MANUAL
          default:
            producer:
              errorChannelEnabled: true
          poller:
            initial-delay: 0
            fixed-delay: 1000
       
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
    #     Uncomment below configurations if you want to enable auto creating resources.
    #      auto-create-resources: true
    #      environment: Azure
    #      region: [ region ]
          eventhubs:
            namespace: [eventhub-namespace]
            processor:
              checkpoint-store:
                container-name: [ container-name ]
                account-name: [ account-name ]
        stream:
          function:
            definition: consume;supply
          bindings:
            consume-in-0:
              destination: [eventhub-name]
              group: [consumer-group]
            supply-out-0:
              destination: [the-same-eventhub-name-as-above]
    
          eventhubs:
            bindings:
              consume-in-0:
                consumer:
                  checkpoint-mode: MANUAL
          default:
            producer:
              errorChannelEnabled: true
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

1.  Run the `mvn spring-boot:run` in the root of the code sample to get the app running.

1.  Send a POST request
    
        $ ### Send messages through imperative.  
        $ curl -X POST http://localhost:8080/messages/imperative/staticalDestination?message=hello
        $ curl -X POST http://localhost:8080/messages/imperative/dynamicDestination?message=hello
    
        $ ### Send messages through reactive.
        $ curl -X POST http://localhost:8080/messages/reactive?message=hello
    
    or when the app runs on App Service or VM

        $ ### Send messages through imperative.
        $ curl -d -X POST https://[your-app-URL]/messages/imperative/staticalDestination?message=hello
        $ curl -d -X POST https://[your-app-URL]/messages/imperative/dynamicDestination?message=hello
    
        $ ### Send messages through reactive.
        $ curl -d -X POST https://[your-app-URL]/messages/reactive?message=hello

1.  Verify in your app’s logs that a similar message was posted:

        New message received: 'hello', partition key: 2002572479, sequence number: 4, offset: 768, enqueued time: 2021-06-03T01:47:36.859Z
        Message 'hello' successfully checkpointed

1.  Delete the resources on [Azure Portal][azure-portal] to avoid unexpected charges.

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

[role-assignment]: https://docs.microsoft.com/azure/role-based-access-control/role-assignments-portal
[application-mi.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-binder/src/spring-cloud-azure_4.0/resources/application-mi.yaml
[application.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-binder/src/spring-cloud-azure_4.0/resources/application.yaml
[application-sp.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-binder/src/spring-cloud-azure_4.0/resources/application-sp.yaml
[StreamBridge]: https://docs.spring.io/spring-cloud-stream/docs/3.1.3/reference/html/spring-cloud-stream.html#_sending_arbitrary_data_to_an_output_e_g_foreign_event_driven_sources
[spring-cloud-stream-batch0-consumer]: https://docs.spring.io/spring-cloud-stream/docs/3.1.4/reference/html/spring-cloud-stream.html#_batch_consumers
[BatchProducerAndConsumerConfiguration]: ./src/spring-cloud-azure_4.0/java/com/azure/spring/sample/eventhubs/binder/BatchProducerAndConsumerConfiguration.java

[deploy-spring-boot-application-to-app-service]: https://docs.microsoft.com/java/azure/spring-framework/deploy-spring-boot-java-app-with-maven-plugin?toc=%2Fazure%2Fapp-service%2Fcontainers%2Ftoc.json&view=azure-java-stable