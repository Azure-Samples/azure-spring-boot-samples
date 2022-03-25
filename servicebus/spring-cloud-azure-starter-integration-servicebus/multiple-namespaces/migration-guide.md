# Guide for migrating the multiple-namespace sample with spring-cloud-azure-starter-integration-servicebus upgrading to 4.0.0 from 4.0.0-beta.2

This guide is intended to assist in the migration of the multiple-namespace sample `spring-cloud-azure-starter-integration-sample-servicebus-multiple-namespaces`
with the underlying library `spring-cloud-azure-starter-integration-servicebus` upgrading to 4.0.0 from 4.0.0-beta.2.

## Sample function changes
The [legacy sample](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-cloud-azure_4.0.0-beta.2/servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces) with `com.azure.spring:spring-cloud-azure-starter-integration-servicebus:4.0.0-beta.2`
builds an application that interacts with two queues in two different Azure Service Bus namespaces. Let's call them as queue1 in namespace1 and queue2 in namespace2.
The legacy sample sends messages to queue1 and listens to queue1 to receive the messages back. In addition, it forwards the messages received from queue1 to queue2.

The modern sample with `com.azure.spring:spring-cloud-azure-starter-integration-servicebus:4.0.0` still interacts with two queues in two different Azure Service Bus namespaces, calling them as queue1 in namespace1 and queue2 in namespace2.
However, it changes the functionalities of the messaging application. It now creates a source to send messages to queue1 and queue2 separately, and listens to both queue1 and queue2 to receive messages from them.
Meanwhile, messages received from queue1 will be forwarded to queue2, which means will be received from queue2 again.

## Role assignment changes

When using security principals instead of connection strings to connect to the two Azure Service Bus namespaces, in the legacy sample, the required roles are 
1. [Azure Service Bus Data Receiver](https://docs.microsoft.com/azure/role-based-access-control/built-in-roles#azure-service-bus-data-receiver) of queue1
1. [Azure Service Bus Data Sender](https://docs.microsoft.com/azure/role-based-access-control/built-in-roles#azure-service-bus-data-sender) of queue1
1. [Azure Service Bus Data Sender](https://docs.microsoft.com/azure/role-based-access-control/built-in-roles#azure-service-bus-data-sender) of queue2

In the modern sample, the required roles are
1. [Azure Service Bus Data Receiver](https://docs.microsoft.com/azure/role-based-access-control/built-in-roles#azure-service-bus-data-receiver) of queue1
1. [Azure Service Bus Data Sender](https://docs.microsoft.com/azure/role-based-access-control/built-in-roles#azure-service-bus-data-sender) of queue1
1. [Azure Service Bus Data Receiver](https://docs.microsoft.com/azure/role-based-access-control/built-in-roles#azure-service-bus-data-receiver) of queue2
1. [Azure Service Bus Data Sender](https://docs.microsoft.com/azure/role-based-access-control/built-in-roles#azure-service-bus-data-sender) of queue2

## Configuration changes
The modern sample refactors and simplifies the customized configuration, which changes the property prefix to `my.servicebus.namespaces[x].` from `servicebus.producers[x].` or `servicebus.processors[x].`.
In the modern sample, there is no need to distinguish the properties to be set is for a producer or processor when using the same destination.
This is due to only namespace related properties can be configured now. The processor related properties are moved from the application properties to `ServiceBusContainerProperties.java`,
which will be introduced in the [code changes](#code-changes) section.

The below table shows the property mapping relationships between the legacy and modern samples.

Legacy properties  | Modern properties | Comment
---|---|---|
servicebus.producers[0].entity-name | my.servicebus.namespaces[0].entity-name | The name of queue1 in namespace1.
servicebus.producers[0].entity-type | my.servicebus.namespaces[0].entity-type | The entity type of queue1, should be set as `queue`.
servicebus.producers[0].namespace | my.servicebus.namespaces[0].namespace | The name of namespace1.
servicebus.producers[0].connection-string | my.servicebus.namespaces[0].connection-string | The connection string of namespace1.
servicebus.producers[0].credential.client-id | my.servicebus.namespaces[0].credential.client-id | The client id of the security principal to connect to queue1.
servicebus.producers[0].credential.client-secret | my.servicebus.namespaces[0].credential.client-secret | The client secret of the security principal to connect to queue1.
servicebus.producers[0].profile.tenant-id | my.servicebus.namespaces[0].profile.tenant-id | The tenant id of the security principal to connect to queue1.
servicebus.producers[0].profile.managed-identity-client-id | my.servicebus.namespaces[0].credential.client-id | The client id of the managed identity to connect to queue1, only needed when using a user-assigned managed identity.
N/A | my.servicebus.namespaces[0].credential.managed-identity-enabled | Whether to enable using the managed identity for authentication.
servicebus.producers[1].entity-name | my.servicebus.namespaces[1].entity-name | The name of queue2 in namespace2.
servicebus.producers[1].entity-type | my.servicebus.namespaces[1].entity-type | The entity type of queue2, should be set as `queue`.
servicebus.producers[1].namespace | my.servicebus.namespaces[1].namespace | The name of namespace2.
servicebus.producers[1].connection-string | my.servicebus.namespaces[1].connection-string | The connection string of namespace2.
servicebus.producers[1].credential.client-id | my.servicebus.namespaces[1].credential.client-id | The client id of the security principal to connect to queue2.
servicebus.producers[1].credential.client-secret | my.servicebus.namespaces[1].credential.client-secret | The client secret of the security principal to connect to queue2.
servicebus.producers[1].profile.tenant-id | my.servicebus.namespaces[1].profile.tenant-id | The tenant id of the security principal to connect to queue2.
servicebus.producers[1].profile.managed-identity-client-id | my.servicebus.namespaces[1].credential.client-id | The client id of the managed identity to connect to queue2, only needed when using a user-assigned managed identity.
N/A | my.servicebus.namespaces[1].credential.managed-identity-enabled | Whether to enable using the managed identity for authentication.
servicebus.processors[0].entity-name  | my.servicebus.namespaces[0].entity-name | The name of queue1 in namespace1. Can be neglected since configured in the above properties.
servicebus.processors[0].entity-type | my.servicebus.namespaces[0].entity-type | The entity type of queue1, should be set as `queue`. Can be neglected since configured in the above properties.
servicebus.processors[0].namespace | my.servicebus.namespaces[0].namespace | The name of namespace1. Can be neglected since configured in the above properties.
servicebus.processors[0].connection-string | my.servicebus.namespaces[0].connection-string | The connection string of namespace1. Can be neglected since configured in the above properties.
servicebus.processors[0].credential.client-id | my.servicebus.namespaces[0].credential.client-id | The client id of the security principal to connect to queue1. Can be neglected since configured in the above properties.
servicebus.processors[0].credential.client-secret | my.servicebus.namespaces[0].credential.client-secret | The client secret of the security principal to connect to queue1. Can be neglected since configured in the above properties.
servicebus.processors[0].profile.tenant-id | my.servicebus.namespaces[0].profile.tenant-id | The tenant id of the security principal to connect to queue1. Can be neglected since configured in the above properties.
servicebus.processors[0].profile.managed-identity-client-id | my.servicebus.namespaces[0].credential.client-id | The client id of the managed identity to connect to queue1, only needed when using a user-assigned managed identity. Can be neglected since configured in the above properties
N/A | my.servicebus.namespaces[0].credential.managed-identity-enabled | Whether to enable using the managed identity for authentication. Can be neglected since configured in the above properties

### Configuration migration based on the usage of connection strings

When using connection string to connect to Azure Service Bus, the configuration should be changed to 

```yaml
my.servicebus.namespaces[0]:
  connection-string: ${AZURE_SERVICEBUS_CONNECTION_STRING_01}
  entity-type: queue
  entity-name: ${AZURE_SERVICEBUS_NAMESPACE_01_QUEUE_NAME}
my.servicebus.namespaces[1]:
  connection-string: ${AZURE_SERVICEBUS_CONNECTION_STRING_02}
  entity-type: queue
  entity-name: ${AZURE_SERVICEBUS_NAMESPACE_02_QUEUE_NAME}
```

### Configuration migration based on the usage of service principals

When using service principals to connect to Azure Service Bus, the configuration should be changed to 

```yaml
my.servicebus.namespaces[0]:
  namespace: ${AZURE_SERVICEBUS_NAMESPACE_01}
  credential:
    client-id: ${AZURE_CLIENT_ID_01}
    client-secret: ${AZURE_CLIENT_SECRET_01}
  profile:
    tenant-id: ${AZURE_TENANT_ID_01}
  entity-type: queue
  entity-name: ${AZURE_SERVICEBUS_NAMESPACE_01_QUEUE_NAME}
my.servicebus.namespaces[1]:
  namespace: ${AZURE_SERVICEBUS_NAMESPACE_02}
  credential:
    client-id: ${AZURE_CLIENT_ID_02}
    client-secret: ${AZURE_CLIENT_SECRET_02}
  profile:
    tenant-id: ${AZURE_TENANT_ID_02}
  entity-type: queue
  entity-name: ${AZURE_SERVICEBUS_NAMESPACE_02_QUEUE_NAME}
```

### Configuration migration based on the usage of managed identities

When using managed identities to connect to Azure Service Bus, the configuration should be changed to 

```yaml
my.servicebus.namespaces[0]:
  namespace: ${AZURE_SERVICEBUS_NAMESPACE_01}
  credential:
    client-id: ${AZURE_CLIENT_ID_01}
    managed-identity-enabled: true
  entity-type: queue
  entity-name: ${AZURE_SERVICEBUS_NAMESPACE_01_QUEUE_NAME}
my.servicebus.namespaces[1]:
  namespace: ${AZURE_SERVICEBUS_NAMESPACE_02}
  credential:
    client-id: ${AZURE_CLIENT_ID_02}
    managed-identity-enabled: true
  entity-type: queue
  entity-name: ${AZURE_SERVICEBUS_NAMESPACE_02_QUEUE_NAME}
```
## Code changes

This section introduces how to migrate the code of legacy samples.

### CustomizedServiceBusProperties
The modern sample drops the way to set sending and receiving related properties for a destination separately,
and supports setting the namespace-level properties for a destination instead, which will apply to both the sending and receiving sides.

For `CustomizedServiceBusProperties.java`, changes it as below:
```java
@ConfigurationProperties("my.servicebus")
public class CustomizedServiceBusProperties {

    private final List<NamespaceProperties> namespaces = new ArrayList<>();

    public List<NamespaceProperties> getNamespaces() {
        return namespaces;
    }
}
```
### ServiceBusIntegrationApplication
Due to the above changes, beans of `producerPropertiesSupplier` and `processorPropertiesSupplier` should be removed as a consequence.

For `ServiceBusIntegrationApplication.java`, changes it as below:
```java
@SpringBootApplication
@EnableIntegration
public class ServiceBusIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBusIntegrationApplication.class, args);
    }
}
```

### From MultipleNamespacesAzureServiceBusMessagingAutoConfiguration to MultipleAzureServiceBusNamespacesConfiguration

The modern sample refactors the Service Bus configuration to apply with [sample function changes](#sample-function-changes).
The configuration is now renamed to `MultipleAzureServiceBusNamespacesConfiguration`, with the user-defined beans of
`ServiceBusTemplate` and `ServiceBusMessageListenerContainer` for sending and listening to two queues.

To customize the properties of the underlying `ServiceBusProcessorClient` used to listening to queues, users can create
`ServiceBusContainerProperties` to configure the needed properties, and pass it to the associated `ServiceBusMessageListenerContainer`.

The legacy `MultipleNamespacesAzureServiceBusMessagingAutoConfiguration.java` should be changed to:
```java
@Configuration
@EnableConfigurationProperties(CustomizedServiceBusProperties.class)
public class MultipleAzureServiceBusNamespacesConfiguration {

    private final NamespaceProperties firstNamespaceProperties;
    private final NamespaceProperties secondNamespaceProperties;

    public MultipleAzureServiceBusNamespacesConfiguration(CustomizedServiceBusProperties properties) {
        this.firstNamespaceProperties = properties.getNamespaces().get(0);
        this.secondNamespaceProperties = properties.getNamespaces().get(1);
    }

    @Bean
    public ServiceBusTemplate firstServiceBusTemplate() {
        ServiceBusProducerFactory producerFactory = new DefaultServiceBusNamespaceProducerFactory(firstNamespaceProperties);
        return new ServiceBusTemplate(producerFactory);
    }

    @Bean
    public ServiceBusMessageListenerContainer firstMessageListenerContainer() {
        ServiceBusProcessorFactory processorFactory = new DefaultServiceBusNamespaceProcessorFactory(firstNamespaceProperties);
        ServiceBusContainerProperties containerProperties = new ServiceBusContainerProperties();
        containerProperties.setEntityName(firstNamespaceProperties.getEntityName());
        containerProperties.setPrefetchCount(10);
        return new ServiceBusMessageListenerContainer(processorFactory, containerProperties);
    }

    @Bean
    public ServiceBusTemplate secondServiceBusTemplate() {
        ServiceBusProducerFactory producerFactory = new DefaultServiceBusNamespaceProducerFactory(secondNamespaceProperties);
        return new ServiceBusTemplate(producerFactory);
    }

    @Bean
    public ServiceBusMessageListenerContainer secondMessageListenerContainer() {
        ServiceBusProcessorFactory processorFactory = new DefaultServiceBusNamespaceProcessorFactory(secondNamespaceProperties);
        ServiceBusContainerProperties containerProperties = new ServiceBusContainerProperties();
        containerProperties.setEntityName(secondNamespaceProperties.getEntityName());
        containerProperties.setPrefetchCount(10);
        return new ServiceBusMessageListenerContainer(processorFactory, containerProperties);
    }
}
```

### From QueueReceiveController and QueueSendController to IntegrationFlowConfiguration
In the legacy sample, `QueueReceiveController.java` and `QueueSendController.java` provides necessary beans used by Spring Integration framework
to send and listen to Azure Service Bus, and provides the web endpoint to trigger message sending operations.

In the modern sample, the configuration and message operation functionalities are moved to `IntegrationFlowConfiguration.java`,
with the message operation flows writing with `IntegrationFlow` to simplify the code.

Users can remove classes of `QueueReceiveController` and `QueueSendController`, and create a class of `IntegrationFlowConfiguration`:

```java
@Configuration
public class IntegrationFlowConfiguration {

    @Value("${my.servicebus.namespaces[0].entity-name:}")
    private String firstQueueName;

    @Value("${my.servicebus.namespaces[1].entity-name:}")
    private String secondQueueName;

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationFlowConfiguration.class);


    private final ServiceBusTemplate firstServiceBusTemplate;
    private final ServiceBusTemplate secondServiceBusTemplate;
    private final ServiceBusMessageListenerContainer firstMessageListenerContainer;
    private final ServiceBusMessageListenerContainer secondMessageListenerContainer;

    public IntegrationFlowConfiguration(@Qualifier("firstServiceBusTemplate") ServiceBusTemplate firstServiceBusTemplate,
                                        @Qualifier("secondServiceBusTemplate") ServiceBusTemplate secondServiceBusTemplate,
                                        @Qualifier("firstMessageListenerContainer") ServiceBusMessageListenerContainer firstMessageListenerContainer,
                                        @Qualifier("secondMessageListenerContainer") ServiceBusMessageListenerContainer secondMessageListenerContainer) {
        this.firstServiceBusTemplate = firstServiceBusTemplate;
        this.secondServiceBusTemplate = secondServiceBusTemplate;
        this.firstMessageListenerContainer = firstMessageListenerContainer;
        this.secondMessageListenerContainer = secondMessageListenerContainer;
    }


    @Bean
    public MessageHandler firstMessageHandler() {
        return new DefaultMessageHandler(firstQueueName, firstServiceBusTemplate);
    }

    @Bean
    public ServiceBusInboundChannelAdapter firstServiceBusInboundChannelAdapter() {
        ServiceBusInboundChannelAdapter channelAdapter = new ServiceBusInboundChannelAdapter(firstMessageListenerContainer);
        channelAdapter.setPayloadType(String.class);
        return channelAdapter;
    }

    @Bean
    public MessageHandler secondMessageHandler() {
        return new DefaultMessageHandler(secondQueueName, secondServiceBusTemplate);
    }

    @Bean
    public ServiceBusInboundChannelAdapter secondServiceBusInboundChannelAdapter() {
        ServiceBusInboundChannelAdapter channelAdapter = new ServiceBusInboundChannelAdapter(secondMessageListenerContainer);
        channelAdapter.setPayloadType(String.class);
        return channelAdapter;
    }

    @Bean
    public AtomicInteger integerSource() {
        return new AtomicInteger();
    }

    @Bean
    public IntegrationFlow sendFlow() {
        return IntegrationFlows.fromSupplier(integerSource()::getAndIncrement,
                c -> c.poller(Pollers.fixedRate(Duration.ofSeconds(10))))
                .<Integer, Boolean>route(p -> p % 2 == 0, m
                        -> m.subFlowMapping(true, f -> f.handle(firstMessageHandler()))
                        .subFlowMapping(false, f -> f.handle(secondMessageHandler())))
                .get();
    }

    @Bean
    public IntegrationFlow transformFlow() {
        return IntegrationFlows.from(firstServiceBusInboundChannelAdapter())
                .transform(m -> {
                    LOGGER.info("Receive messages from the first queue: {}", m);
                    return "transformed from queue1, " + m;
                })
                .handle(secondMessageHandler())
                .get();
    }

    @Bean
    public IntegrationFlow secondListenerFlow() {
        return IntegrationFlows.from(secondServiceBusInboundChannelAdapter())
                .handle(m -> LOGGER.info("Receive messages from the second queue: {}", m.getPayload()))
                .get();
    }
}
```