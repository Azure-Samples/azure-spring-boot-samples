# Spring Cloud Azure Service Bus Integration Code Sample shared library for Java

## Key concepts

This code sample demonstrates how to use Spring Integration for Azure Service Bus with multiple destinations. It shows how to use Spring Integration for Azure Service Bus to send and receive messages from one queue in one Service Bus namespace and then forward them to another queue in another Service Bus namespace.


## Getting started

Running this sample will be charged by Azure. You can check the usage and bill at
[this link][azure-account].



### Create Azure resources

1. Create Azure Service Bus namespaces and two queue entities with name of `queue1` and `queue2`. Please see 
   [how to create][create-service-bus]. Note: this sample takes queue as example, it's also applied with Service Bus topic.

We have several ways to config the Spring Integration for Service
Bus. You can choose anyone of them.

#### Method 1: Connection string based usage

1. Update [application.yaml].
    ```yaml
    servicebus.producers[0]:
      entity-name: queue1
      entity-type: queue
      connection-string: ${AZURE_SERVICEBUS_CONNECTION_STRING_1}
    servicebus.producers[1]:
      entity-name: queue2
      entity-type: queue
      connection-string: ${AZURE_SERVICEBUS_CONNECTION_STRING_2}
    servicebus.processors[0]:
      entity-name: queue1
      entity-type: queue
      connection-string: ${AZURE_SERVICEBUS_CONNECTION_STRING_3}
    ``` 
#### Method 2: Service principal based usage

1.  Create a service principal for use in by your app. Please follow
    [create service principal from Azure CLI][create-sp-using-azure-cli].

1.  Add Role Assignment for Service Bus. See
    [Service principal for Azure resources with Service Bus][role-assignment]
    to add role assignment for Service Bus. Assign `Contributor` role for service bus.

1.  Update [application-sp.yaml][application-sp.yaml].
    > We should specify `spring.profiles.active=sp` to run the Spring Boot application.
    For App Service, please add a configuration entry for this.
    
#### Method 3: MSI credential based usage

##### Set up managed identity

Please follow [create managed identity][create-managed-identity] to set up managed identity.

##### Add Role Assignment for Service Bus

1.  See [Managed identities for Azure resources with Service Bus][role-assignment]
    to add role assignment for Service Bus. Assign `Contributor` role for managed identity.


##### Update MSI related properties

1.  Update [application-mi.yaml][application-mi.yaml].
    > We should specify `spring.profiles.active=mi` to run the Spring Boot application.
For App Service, please add a configuration entry for this.

## Examples
 
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
[create-managed-identity]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/create-managed-identity.md
[create-sp-using-azure-cli]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/create-sp-using-azure-cli.md

[application.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces/src/main/resources/application.yaml

[application-mi.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces/src/main/resources/application-mi.yaml
[application-sp.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces/src/main/resources/application-sp.yaml
[role-assignment]: https://docs.microsoft.com/azure/role-based-access-control/role-assignments-portal


