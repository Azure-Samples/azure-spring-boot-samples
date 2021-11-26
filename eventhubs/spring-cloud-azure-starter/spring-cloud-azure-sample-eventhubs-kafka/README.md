# Spring Cloud Azure Sample Stream Event Hubs Kafka

## Key concepts

This code sample demonstrates how to use the Spring Cloud Azure Starter and Spring Cloud Starter Stream Kafka for Azure Event Hubs. The sample app exposes a RESTful API to receive
string message. Then message is sent through Azure Event Hubs to a bean `consumer`
which simply logs the message.

## Getting started


Running this sample will be charged by Azure. You can check the usage and bill at
[this link][azure-account].


### Create Azure resources

1. Create a service principal for use in by your app. Please follow 
   [create service principal from Azure CLI][create-sp-using-azure-cli]. 
   The credential is not required since Spring Cloud Azure support https://docs.microsoft.com/en-us/azure/developer/java/sdk/identity,
   you only need to log in with az cli / vs code or Intellij Azure Toolkit, then credential information will be left out of properties

3. Create [Azure Event Hubs][create-event-hubs]. Please notice that Event Hubs for Kafka is only supported for [Standard and Dedicated tier namespaces](https://azure.microsoft.com/pricing/details/event-hubs/).

## Examples

### Use Event Hubs connection string

The simplest way to connect to Event Hubs for Kafka is with the connection string. 

1. Update
    [application.yaml][application.yaml]
    file
    
    ```yaml
    spring:
      cloud:
        azure:
          eventhubs:
            connection-string: ${AZURE_EVENTHUBS_CONNECTION_STRING}
        stream:
          function:
            definition: consume;supply
          bindings:
            consume-in-0:
              destination: sample-eventhubs-kafka
              group: $Default
            supply-out-0:
              destination: sample-eventhubs-kafka
    ```

2. Run the `mvn spring-boot:run` in the root of the code sample to get the app running.

3. Send a POST request

        $ curl -X POST http://localhost:8080/messages?message=hello

4. Verify in your app’s logs that a similar message was posted:

    `New message received: hello`

5. Delete the resources on [Azure Portal][azure-portal] to avoid unexpected charges.

### Use Azure Resource Manager to retrieve connection string

If you don't want to configure connection string in your application, it's also possible to use Azure Resource Manager to retrieve the connection string. And you could use credentials stored in Azure CLI or other local development tool, like Visual Studio Code or Intellij IDEA to authenticate with Azure Resource Manager. Or Managed Identity if your application is deployed to Azure Cloud. Just make sure the principal have sufficient permission to read resource metadata.

1. Update
   [application.yaml][application.yaml]
   file

    ```yaml
    spring:
      cloud:
        azure:
          profile:
            subscription-id: ${AZURE_SUBSCRIPTION_ID}
          eventhubs:
            namespace: ${AZURE_EVENTHUBS_NAMESPACE}
            resource:
              resource-group: ${AZURE_EVENTHUBS_RESOURCE_GROUP}
        stream:
          function:
            definition: consume;supply
          bindings:
            consume-in-0:
              destination: sample-eventhubs-kafka
              group: $Default
            supply-out-0:
              destination: sample-eventhubs-kafka
    ```
2. Add the Azure Resource Manager dependency
```xml
<dependency>
  <groupId>com.azure.spring</groupId>
  <artifactId>spring-cloud-azure-resourcemanager</artifactId>
</dependency>
```
3. Repeat steps 2 to 5 of the first example.

## Troubleshooting

- Meet with  `Creating topics with default partitions/replication factor are only supported in CreateTopicRequest version 4+` error.
  
  ```text
  o.s.c.s.b.k.p.KafkaTopicProvisioner      : Failed to create topics
    org.apache.kafka.common.errors.UnsupportedVersionException: Creating topics with default partitions/replication factor are only supported in CreateTopicRequest version 4+. The following topics need values for partitions and replicas
  ```

  When this error is found, add this configuration item `spring.cloud.stream.kafka.binder.replicationFactor`, with the value set to at least 1. For more information, see [Spring Cloud Stream Kafka Binder Reference Guide](https://docs.spring.io/spring-cloud-stream-binder-kafka/docs/current/reference/html/spring-cloud-stream-binder-kafka.html).

## Next steps

## Contributing

<!-- LINKS -->
[azure-account]: https://azure.microsoft.com/account/
[azure-portal]: https://ms.portal.azure.com/
[create-event-hubs]: https://docs.microsoft.com/azure/event-hubs/
[create-sp-using-azure-cli]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/create-sp-using-azure-cli.md
[application.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/eventhubs/azure-spring-cloud-starter-eventhubs-kafka/eventhubs-kafka/src/main/resources/application.yaml
