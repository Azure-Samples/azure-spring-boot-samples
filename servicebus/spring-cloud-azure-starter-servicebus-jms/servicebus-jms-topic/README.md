# Sample for Spring JMS with Azure Service Bus Topic Spring Cloud client library for Java
## Key concepts

This sample project demonstrates how to use Spring JMS Topic for Azure Service Bus via Spring Boot Starter `spring-cloud-azure-starter-servicebus-jms`.

Running this sample will be charged by Azure. You can check the usage and bill at this [link](https://azure.microsoft.com/account/).

## Getting started

### Create Service Bus on Azure
1. Go to [Azure portal] and create the service by following this [link](https://docs.microsoft.com/azure/service-bus-messaging/service-bus-create-namespace-portal). 
2. Add a topic in the portal.
3. Add a subscription under the topic.

## Examples                                           
### Config the sample

1. Update [application.yml]
```yaml
spring:
  jms:
    listener:
      concurrency: [NUM_OF_CONSUMERS]
    servicebus:
      connection-string: [SERVICEBUS_NAMESPACE_CONNECTION_STRING]
      idle-timeout: 1800000
      topic-client-id: [TOPIC_CLIENT_ID] # user defined
topic: [TOPIC_NAME]
subscription: [SUBSCRIPTION_NAME]
```

2. [Optional] If you want to activate the JmsPoolConnectionFactory feature, set `spring.jms.servicebus.pool.enabled=true`

   > Note: CachingConnectionFactory is the default ConnectionFactory used.

### How to run
1. Run with Maven:
    ```
    cd azure-spring-boot-samples/servicebus/spring-cloud-azure-starter-servicebus-jms
    mvn spring-boot:run
    ```

2. Verify in your app's logs that the similar messages can be seen.
    ```
     Sending message: Hello World, 0
     Sending message: Hello World, 1
     Received message from queue: Hello World, 35
    ```
3. [Optional] Send a POST request to service bus queue.
    ```
    $ curl -X POST localhost:8080/queue?message=hello
    ```

4. [Optional] Verify in your app's logs that a similar message was posted:
    ```
    Sending message
    Received message from queue: hello
    ```
   
5. Delete the resources on [Azure Portal] to avoid extra charges.

## Troubleshooting
## Next steps

Please check the following table for reference links of detailed Service Bus usage. 

Type | Reference Link
--- | ---
`Topics` | [https://docs.microsoft.com/azure/service-bus-messaging/service-bus-java-how-to-use-topics-subscriptions](https://docs.microsoft.com/azure/service-bus-messaging/service-bus-java-how-to-use-topics-subscriptions)
`Subscriptions` | [https://docs.microsoft.com/azure/service-bus-messaging/service-bus-java-how-to-use-topics-subscriptions](https://docs.microsoft.com/azure/service-bus-messaging/service-bus-java-how-to-use-topics-subscriptions)

## Contributing

<!-- LINKS -->

[TopicSendController]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-topic/src/main/java/com/azure/spring/sample/jms/topic/TopicSendController.java

[TopicReceiveController]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-topic/src/main/java/com/azure/spring/sample/jms/topic/TopicReceiveController.java

[application.yml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-topic/src/main/resources/application.yml

[Azure portal]: https://portal.azure.com/