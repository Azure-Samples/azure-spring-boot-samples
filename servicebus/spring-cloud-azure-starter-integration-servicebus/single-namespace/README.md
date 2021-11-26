# Spring Cloud Azure Service Bus Integration Code Sample shared library for Java

## Key concepts

This code sample demonstrates how to use Spring Integration for Azure Service Bus.


## Getting started

Running this sample will be charged by Azure. You can check the usage and bill at
[this link][azure-account].



### Create Azure resources

1. Create Azure Service Bus namespace, queue and topic. Please see 
   [how to create][create-service-bus].
### Configuration credential options

We have several ways to config the Spring Integration for Service
Bus. You can choose anyone of them.

#### Method 1: Connection string based usage

1.  Update [application.yaml][application.yaml].
    ```yaml
    spring:
      cloud:
        azure:
          servicebus:
            connection-string: ${AZURE_SERVICEBUS_BINDER_CONNECTION_STRING} 
    ```

#### Method 2: Service principal based usage

1.  Create a service principal for use in by your app. Please follow
    [create service principal from Azure CLI][create-sp-using-azure-cli].

1.  Add Role Assignment for Service Bus. See
    [Service principal for Azure resources with Service Bus][role-assignment]
    to add role assignment for Service Bus. Assign `Contributor` role for service bus.

1.  Update [application-sp.yaml][application-sp.yaml].
    ```yaml
    spring:
      cloud:
        azure:
          credential:
            client-id: ${AZURE_CLIENT_ID}
            client-secret: ${AZURE_CLIENT_SECRET}
          profile:
            tenant-id: ${AZURE_TENANT_ID}
          servicebus:
            namespace: ${AZURE_SERVICEBUS_BINDER_NAMESPACE}
    ```
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
    ```yaml
    spring:
      cloud:
        azure:
          credential:
            managed-identity-client-id: ${AZURE_MANAGED_IDENTITY_CLIENT_ID}
          profile:
            tenant-id: ${AZURE_TENANT_ID}
          servicebus:
            namespace: ${AZURE_SERVICEBUS_NAMESPACE}
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

1. Run the `mvn spring-boot:run` in the root of the code sample to get the app running.

2. Update queue name in 
   [QueueReceiveController.java][queue-receive-controller] and
   [QueueSendController.java][queue-send-controller],
   and update topic name and subscription in
   [TopicReceiveController.java][topic-receive-controller] and
   [TopicSendController.java][topic-send-controller].   
    
3. Run the `mvn spring-boot:run` in the root of the code sample to get the app running.

4. Send a POST request to service bus queue

        $ curl -X POST http://localhost:8080/queues?message=hello

5. Verify in your app’s logs that a similar message was posted:

        New message received: 'hello'
        Message 'hello' successfully checkpointed
6. Send a POST request to service bus topic

        $ curl -X POST http://localhost:8080/topics?message=hello

7. Verify in your app’s logs that a similar message was posted:

        New message received: 'hello'
        Message 'hello' successfully checkpointed

8. Delete the resources on [Azure Portal][azure-portal] to avoid unexpected charges.

## Enhancement
### Set Service Bus message headers
The following table illustrates how Spring message headers are mapped to Service Bus message headers and properties.
When creat a message, developers can specify the header or property of a Service Bus message by below constants.

```java
@MessagingGateway(defaultRequestChannel = OUTPUT_CHANNEL, defaultHeaders = @GatewayHeader(name = SESSION_ID,
value="group"))
public interface QueueOutboundGateway {
    void send(String text);
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

[azure-account]: https://azure.microsoft.com/account/
[azure-portal]: https://ms.portal.azure.com/
[create-service-bus]: https://docs.microsoft.com/azure/service-bus-messaging/service-bus-create-namespace-portal
[create-managed-identity]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/create-managed-identity.md
[create-sp-using-azure-cli]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/create-sp-using-azure-cli.md
[role-assignment]: https://docs.microsoft.com/azure/role-based-access-control/role-assignments-portal
[queue-receive-controller]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace/src/main/java/com/azure/spring/sample/servicebus/QueueReceiveController.java
[queue-send-controller]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace/src/main/java/com/azure/spring/sample/servicebus/QueueSendController.java
[topic-receive-controller]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace/src/main/java/com/azure/spring/sample/servicebus/TopicReceiveController.java
[topic-send-controller]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace/src/main/java/com/azure/spring/sample/servicebus/TopicSendController.java
[application.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace/src/main/resources/application.yaml
[application-mi.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace/src/main/resources/application-mi.yaml
[application-sp.yaml]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/spring-cloud-azure_4.0/servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace/src/main/resources/application-sp.yaml

[deploy-spring-boot-application-to-app-service]: https://docs.microsoft.com/java/azure/spring-framework/deploy-spring-boot-java-app-with-maven-plugin?toc=%2Fazure%2Fapp-service%2Fcontainers%2Ftoc.json&view=azure-java-stable


