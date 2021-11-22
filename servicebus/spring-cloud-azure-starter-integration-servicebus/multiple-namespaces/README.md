# Spring Cloud Azure Service Bus Integration Code Sample shared library for Java

## Key concepts

This code sample demonstrates how to use Spring Integration for Azure Service Bus with multiple destinations. It shows how to use Spring Integration for Azure Service Bus to send and receive messages from one queue in one Service Bus namespace and then forward them to another queue in another Service Bus namespace.


## Getting started

Running this sample will be charged by Azure. You can check the usage and bill at
[this link][azure-account].



### Create Azure resources

1. Create Azure Service Bus namespaces and two queue entities with name of `queue1` and `queue2`. Please see 
   [how to create][create-service-bus]. Note: this sample takes queue as example, it's also applied with Service Bus topic.

## Examples

1. Update [application.yaml]. If you choose to use service principal or managed identity, update the `application-sp.yaml` or
 `application-mi.yaml` respectively.
    ```yaml
    servicebus.producers[0]:
      entity-name: queue1
      entity-type: queue
      connection-string: [connection-string-for-queue1-send]
    servicebus.producers[1]:
      entity-name: queue2
      entity-type: queue
      connection-string: [connection-string-for-queue2-send]
    servicebus.processors[0]:
      entity-name: queue1
      entity-type: queue
      connection-string: [connection-string-for-queue1-receive]
    ``` 
    
1.  Run the `mvn spring-boot:run` in the root of the code sample to get the app running.

1. Send a POST request to service bus queue

        $ curl -X POST http://localhost:8080/queues?message=hello

1.  Verify in your app’s logs that a similar message was posted:

        Message was sent successfully for queue1.
        New message received: 'hello'
        Message 'hello' successfully checkpointed
        Message was sent successfully for queue2.

1.  Delete the resources on [Azure Portal][azure-portal] to avoid unexpected charges.

## Troubleshooting

## Next steps

## Contributing

[azure-account]: https://azure.microsoft.com/account/
[azure-portal]: https://ms.portal.azure.com/
[create-service-bus]: https://docs.microsoft.com/azure/service-bus-messaging/service-bus-create-namespace-portal
[create-managed-identity]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/create-managed-identity.md
[create-sp-using-azure-cli]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/create-sp-using-azure-cli.md

[application.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces/src/main/resources/application.yaml



