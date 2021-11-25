# Spring Cloud Azure Stream Binder for Multiple Service Bus Namespaces Code Sample shared library for Java

## Key concepts
This code sample demonstrates how to use the Spring Cloud Stream Binder for 
multiple Azure Service Bus namespaces. In this sample you will bind to two Service Bus namespaces separately through
a queue binder and a topic binder..The sample app has two operating modes. One way is to expose a Restful API to receive string message,
another way is to automatically provide string messages. These messages are published to a service bus.
The sample will also consume messages from the same service bus.

## Getting started

Running this sample will be charged by Azure. You can check the usage
and bill at [this link][azure-account].



### Create Azure resources

1.  Create a queue and a topic in different Service Bus namespaces.
    Please see [how to create][create-service-bus].

1.  **[Optional]** if you want to use service principal, please follow 
    [create service principal from Azure CLI][create-sp-using-azure-cli] to create one.

1.  **[Optional]** if you want to use managed identity, please follow
    [create managed identity][create-managed-identity] to set up managed identity. 

## Examples

1.  Update stream binding related properties in
    [application.yaml][application.yaml]. If you choose to use 
    service principal or managed identity, update the [application-sp.yaml][application-sp.yaml] or 
    [application-mi.yaml][application-mi.yaml] respectively.

    ```yaml
    spring:
      cloud:
        stream:
          #To specify which functional bean to bind to the external destination(s) exposed by the bindings
          function:
            definition: consume1;supply1;consume2;supply2
          bindings:
            consume1-in-0:
              destination: ${AZURE_SERVICEBUS_TOPIC_NAME}
              group: ${AZURE_SERVICEBUS_TOPIC_SUBSCRIPTION_NAME}
            supply1-out-0:
              destination: ${AZURE_SERVICEBUS_TOPIC_NAME}
            consume2-in-0:
              binder: servicebus-2
              destination: ${AZURE_SERVICEBUS_QUEUE_NAME}
            supply2-out-0:
              binder: servicebus-2
              destination: ${AZURE_SERVICEBUS_QUEUE_NAME}
          binders:
            servicebus-1:
              type: servicebus
              default-candidate: true
              environment:
                spring:
                  cloud:
                    azure:
                      servicebus:
                        connection-string: ${AZURE_SERVICEBUS_CONNECTION_STRING_1}
            servicebus-2:
              type: servicebus
              default-candidate: false
              environment:
                spring:
                  cloud:
                    azure:
                      servicebus:
                        connection-string: ${AZURE_SERVICEBUS_CONNECTION_STRING_2}
          servicebus:
            bindings:
              consume1-in-0:
                consumer:
                  checkpoint-mode: MANUAL
              supply1-out-0:
                producer:
                  entity-type: topic
              consume2-in-0:
                consumer:
                  checkpoint-mode: MANUAL
              supply2-out-0:
                producer:
                  entity-type: queue
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

## Enhancement
### Configuration Options 

The binder provides the following configuration options:

##### Service Bus Producer Properties

It supports the following configurations with the format of `spring.cloud.stream.servicebus.bindings.<channelName>.producer`.

**_sync_**

Whether the producer should act in a synchronous manner with respect to writing messages into a stream. If true, the 
producer will wait for a response after a send operation.

Default: `false`

**_send-timeout_**

Effective only if `sync` is set to true. The amount of time to wait for a response after a send operation, in milliseconds.

Default: `10000`
 
##### Service Bus Consumer Properties

It supports the following configurations with the format of `spring.cloud.stream.servicebus.bindings.<channelName>.consumer`.

**_checkpoint-mode_**

The mode in which checkpoints are updated.

`RECORD`, checkpoints occur after each record successfully processed by user-defined message handler without any exception.

`MANUAL`, checkpoints occur on demand by the user via the `Checkpointer`. You can get `Checkpointer` by `Message.getHeaders.get(AzureHeaders.CHECKPOINTER)`callback.

Default: `RECORD`

**_prefetch-count_**

Prefetch count of underlying service bus client.

Default: `1`

**_maxConcurrentCalls_**

Controls the max concurrent calls of service bus message handler and the size of fixed thread pool that handles user's business logic

Default: `1`

**_maxConcurrentSessions_**

Controls the maximum number of concurrent sessions to process at any given time.

Default: `1`

**_concurrency_**

When `sessionsEnabled` is true, controls the maximum number of concurrent sessions to process at any given time.
When `sessionsEnabled` is false, controls the max concurrent calls of service bus message handler and the size of fixed thread pool that handles user's business logic.

Deprecated, replaced with `maxConcurrentSessions` when `sessionsEnabled` is true and `maxConcurrentCalls` when `sessionsEnabled` is false

Default: `1`

**_sessionsEnabled_**

Controls if is a session aware consumer. Set it to `true` if is a queue with sessions enabled.

Default: `false`

**_requeueRejected_**

Controls if is a message that trigger any exception in consumer will be force to DLQ. 
Set it to `true` if a message that trigger any exception in consumer will be force to DLQ.
Set it to `false` if a message that trigger any exception in consumer will be re-queued. 

Default: `false`

**_receiveMode_**

The modes for receiving messages.

`PEEK_LOCK`, received message is not deleted from the queue or subscription, instead it is temporarily locked to the receiver, making it invisible to other receivers.

`RECEIVE_AND_DELETE`, received message is removed from the queue or subscription and immediately deleted.

Default: `PEEK_LOCK`

**_enableAutoComplete_**

Enable auto-complete and auto-abandon of received messages.
'enableAutoComplete' is not needed in for RECEIVE_AND_DELETE mode.

Default: `false`
### Set message headers
The following table illustrates how Spring message headers are mapped to Service Bus message headers and properties.
When creat a message, developers can specify the header or property of a Service Bus message by below constants.

```java
    @Autowired
private Sinks.Many<Message<String>> many;

@PostMapping("/messages")
public ResponseEntity<String> sendMessage(@RequestParam String message) {
    many.emitNext(MessageBuilder.withPayload(message)
    .setHeader(SESSION_ID, "group1")
    .build(),
    Sinks.EmitFailureHandler.FAIL_FAST);
    return ResponseEntity.ok("Sent!");
    }
```

For some Service Bus headers that can be mapped to multiple Spring header constants, the priority of different Spring headers is listed.

Service Bus Message Headers and Properties | Spring Message Header Constants | Type | Priority Number (Descending priority)
:---|:---|:---|:---
ContentType | org.springframework.messaging.MessageHeaders.CONTENT_TYPE | String | N/A
CorrelationId | com.azure.spring.servicebus.support.ServiceBusMessageHeaders.CORRELATION_ID | String | N/A
**MessageId** | com.azure.spring.servicebus.support.ServiceBusMessageHeaders.MESSAGE_ID | String | 1
**MessageId** | com.azure.spring.messaging.AzureHeaders.RAW_ID | String | 2
**MessageId** | org.springframework.messaging.MessageHeaders.ID | UUID | 3
PartitionKey | com.azure.spring.servicebus.support.ServiceBusMessageHeaders.PARTITION_KEY | String | N/A
ReplyTo | org.springframework.messaging.MessageHeaders.REPLY_CHANNEL | String | N/A
ReplyToSessionId | com.azure.spring.servicebus.support.ServiceBusMessageHeaders.REPLY_TO_SESSION_ID | String | N/A
**ScheduledEnqueueTimeUtc** | com.azure.spring.messaging.AzureHeaders.SCHEDULED_ENQUEUE_MESSAGE | Integer | 1
**ScheduledEnqueueTimeUtc** | com.azure.spring.servicebus.support.ServiceBusMessageHeaders.SCHEDULED_ENQUEUE_TIME | Instant | 2
SessionID | com.azure.spring.servicebus.support.ServiceBusMessageHeaders.SESSION_ID | String | N/A
TimeToLive | com.azure.spring.servicebus.support.ServiceBusMessageHeaders.TIME_TO_LIVE | Duration | N/A
To | com.azure.spring.servicebus.support.ServiceBusMessageHeaders.TO | String | N/A

## Troubleshooting

## Next steps

## Contributing


<!-- LINKS -->
[azure-account]: https://azure.microsoft.com/account/
[azure-portal]: https://ms.portal.azure.com/
[create-service-bus]: https://docs.microsoft.com/azure/service-bus-messaging/service-bus-create-namespace-portal
[create-sp-using-azure-cli]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/create-sp-using-azure-cli.md
[create-managed-identity]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/create-managed-identity.md
[deploy-spring-boot-application-to-app-service]: https://docs.microsoft.com/java/azure/spring-framework/deploy-spring-boot-java-app-with-maven-plugin?toc=%2Fazure%2Fapp-service%2Fcontainers%2Ftoc.json&view=azure-java-stable
[deploy-to-app-service-via-ftp]: https://docs.microsoft.com/azure/app-service/deploy-ftp
[managed-identities]: https://docs.microsoft.com/azure/active-directory/managed-identities-azure-resources/
[role-assignment]: https://docs.microsoft.com/azure/role-based-access-control/role-assignments-portal
[application.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-multibinders/src/main/resources/application.yaml
[application-mi.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-multibinders/src/main/resources/application-mi.yaml
[application-sp.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-multibinders/src/main/resources/application-sp.yaml



