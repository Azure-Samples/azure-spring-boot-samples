---
page_type: sample
languages:
- java
products:
- azure-service-bus
description: "Azure Spring Cloud Sample project for Service Bus Operation client library"
urlFragment: "azure-spring-cloud-sample-service-bus-operation"
---

# Spring Cloud Azure Service Bus Operation Sample shared library for Java

## Key concepts

This code sample demonstrates how to use 
[ServiceBusTopicOperation.java][servicebus-topic-operation] and
[ServiceBusQueueOperation.java][servicebus-queue-operation].

## Getting started

Running this sample will be charged by Azure. You can check the usage and bill at 
[this link][azure-account].



### Create Azure resources

1.  Create Azure Service Bus namespace, queue and topic.
    Please see [how to create][create-service-bus].

## Examples

1. Update [application.yaml][application.yaml].
    ```yaml
    spring:
      cloud:
        azure:
          servicebus:
            connection-string: [servicebus-namespace-connection-string]
    ```
1.  Update queue name in [QueueController.java][queue-controller], 
    topic name and subscription in [TopicController.java][topic-controller].


1.  Run the `mvn spring-boot:run` in the root of the code sample to get the app running.

1.  Send a POST request to service bus queue

        $ curl -X POST localhost:8080/queues?message=hello

1.  Verify in your app’s logs that a similar message was posted:

        New message received: 'hello'
        Message 'hello' successfully checkpointed

1.  Send a POST request to service bus topic

        $ curl -X POST localhost:8080/topics?message=hello

1.  Verify in your app’s logs that a similar message was posted:

        New message received: 'hello'
        Message 'hello' successfully checkpointed        

1.  Delete the resources on [Azure Portal][azure-portal] to avoid unexpected charges.

## Enhancement
### Set Service Bus message headers
The following table illustrates how Spring message headers are mapped to Service Bus message headers and properties.
When creat a message, developers can specify the header or property of a Service Bus message by below constants.

```java
@Autowired
ServiceBusTopicOperation topicOperation;

@PostMapping("/topics")
public String send(@RequestParam("message") String message) {
    this.topicOperation.sendAsync(TOPIC_NAME,
                                  MessageBuilder.withPayload(message)
                                                .setHeader(SESSION_ID, "group1")
                                                .build());
    return message;
}
```

For some Service Bus headers that can be mapped to multiple Spring header constants, the priority of different Spring headers is listed.

Service Bus Message Headers and Properties | Spring Message Header Constants | Type | Priority Number (Descending priority)
:---|:---|:---|:---
ContentType | org.springframework.messaging.MessageHeaders.CONTENT_TYPE | String | N/A
CorrelationId | com.azure.spring.integration.servicebus.converter.ServiceBusMessageHeaders.CORRELATION_ID | String | N/A
**MessageId** | com.azure.spring.integration.servicebus.converter.ServiceBusMessageHeaders.MESSAGE_ID | String | 1
**MessageId** | com.azure.spring.integration.core.AzureHeaders.RAW_ID | String | 2
**MessageId** | org.springframework.messaging.MessageHeaders.ID | UUID | 3
PartitionKey | com.azure.spring.integration.servicebus.converter.ServiceBusMessageHeaders.PARTITION_KEY | String | N/A
ReplyTo | org.springframework.messaging.MessageHeaders.REPLY_CHANNEL | String | N/A
ReplyToSessionId | com.azure.spring.integration.servicebus.converter.ServiceBusMessageHeaders.REPLY_TO_SESSION_ID | String | N/A
**ScheduledEnqueueTimeUtc** | com.azure.spring.integration.core.AzureHeaders.SCHEDULED_ENQUEUE_MESSAGE | Integer | 1
**ScheduledEnqueueTimeUtc** | com.azure.spring.integration.servicebus.converter.ServiceBusMessageHeaders.SCHEDULED_ENQUEUE_TIME | Instant | 2
SessionID | com.azure.spring.integration.servicebus.converter.ServiceBusMessageHeaders.SESSION_ID | String | N/A
TimeToLive | com.azure.spring.integration.servicebus.converter.ServiceBusMessageHeaders.TIME_TO_LIVE | Duration | N/A
To | com.azure.spring.integration.servicebus.converter.ServiceBusMessageHeaders.TO | String | N/A

## Troubleshooting

## Next steps

## Contributing


<!-- LINKS -->

[azure-account]: https://azure.microsoft.com/account/
[azure-portal]: https://ms.portal.azure.com/
[create-service-bus]: https://docs.microsoft.com/azure/service-bus-messaging/service-bus-create-namespace-portal
[queue-controller]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/servicebus/azure-spring-cloud-starter-servicebus/servicebus-operation/src/main/java/com/azure/spring/sample/servicebus/operation/QueueController.java 
[topic-controller]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/servicebus/azure-spring-cloud-starter-servicebus/servicebus-operation/src/main/java/com/azure/spring/sample/servicebus/operation/TopicController.java 

[servicebus-queue-operation]: https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-boot_3.6.0/sdk/spring/azure-spring-integration-servicebus/src/main/java/com/azure/spring/integration/servicebus/queue/ServiceBusQueueOperation.java
[servicebus-topic-operation]: https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-boot_3.6.0/sdk/spring/azure-spring-integration-servicebus/src/main/java/com/azure/spring/integration/servicebus/topic/ServiceBusTopicOperation.java
[application.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/servicebus/azure-spring-cloud-starter-servicebus/servicebus-operation/src/main/resources/application.yaml
