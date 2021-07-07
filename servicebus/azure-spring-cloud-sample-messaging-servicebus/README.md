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
    <version>2.6.0</version> <!-- {x-version-update;com.azure.spring:azure-spring-cloud-messaging;dependency} -->
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
[application.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/servicebus/azure-spring-cloud-sample-servicebus-operation/src/main/resources/application.yaml