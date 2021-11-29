---
page_type: sample
languages:
- java
products:
- azure-service-bus
description: "Azure Spring Cloud Sample project for Messaging Service Bus client library"
urlFragment: "azure-spring-cloud-sample-messaging-service-bus"
---

# Spring Cloud Azure Messaging Service Bus Sample shared library for Java

## Key concepts

This code sample demonstrates how to use [AzureMessageListener.java][annotation-azure-message-listener] to listen to messages from Service Bus Topic.

## Getting started

Running this sample will be charged by Azure. You can check the usage and bill at 
[this link][azure-account].



### Create Azure resources

1.  Create [Azure Service Bus Namespace][create-service-bus-namespace].
    Please note `Basic` tier is unsupported.
    
1.  Create [Azure Service Bus Topic][create-service-bus-topic] and named `topic`. After creating the Azure Service Bus Topic,
    you can create the subscription [Azure Service Bus Topic subscription][create-subscription] to the topic and named `sub` .


### Include the package
Because dependency `azure-spring-cloud-starter-servicebus` does not introduce the dependency about messaging, we need to add
dependency `azure-spring-cloud-messaging`.

[//]: # ({x-version-update-start;com.azure.spring:azure-spring-cloud-messaging;dependency})
```xml
<dependency>
    <groupId>com.azure.spring</groupId>
    <artifactId>azure-spring-cloud-messaging</artifactId>
    <version>2.10.0</version> <!-- {x-version-update;com.azure.spring:azure-spring-cloud-messaging;dependency} -->
</dependency>
```
[//]: # ({x-version-update-end})

## Examples

1. Update [application.yaml][application.yaml].
    ```yaml
    spring:
      cloud:
        azure:
          servicebus:
            connection-string: [servicebus-namespace-connection-string]
    ```

1.  Run the `mvn spring-boot:run` in the root of the code sample to get the app running.

1.  Send a POST request

        $ curl -X POST http://localhost:8080/messages?message=hello

1.  Verify in your app’s logs that a similar message was posted:

        New service bus topic message received: 'hello'

1.  Delete the resources on [Azure Portal][azure-portal] to avoid unexpected charges.

## Enhancement
### Set Service Bus message headers
The following table illustrates how Spring message headers are mapped to Service Bus message headers and properties.
When creat a message, developers can specify the header or property of a Service Bus message by below constants.

```java
@Autowired
ServiceBusTopicOperation topicOperation;

@PostMapping("/messages")
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
[create-service-bus-namespace]: https://docs.microsoft.com/azure/service-bus-messaging/service-bus-quickstart-topics-subscriptions-portal#create-a-namespace-in-the-azure-portal
[create-service-bus-topic]: https://docs.microsoft.com/azure/service-bus-messaging/service-bus-quickstart-topics-subscriptions-portal#create-a-topic-using-the-azure-portal
[create-subscription]: https://docs.microsoft.com/azure/service-bus-messaging/service-bus-quickstart-topics-subscriptions-portal#create-subscriptions-to-the-topic
[annotation-azure-message-listener]: https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-boot_3.6.0/sdk/spring/azure-spring-cloud-messaging/src/main/java/com/azure/spring/messaging/annotation/AzureMessageListener.java
[application.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/servicebus/azure-spring-cloud-starter-servicebus/servicebus-operation/src/main/resources/application.yaml