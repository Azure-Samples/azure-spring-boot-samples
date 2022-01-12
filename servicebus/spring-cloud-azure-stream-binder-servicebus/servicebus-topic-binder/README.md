# Spring Cloud Azure Stream Binder for Service Bus topic Sample shared library for Java 


This code sample demonstrates how to use the Spring Cloud Stream binder for Azure Service Bus topic. The sample app has two operating modes.
One way is to expose a Restful API to receive string message, another way is to automatically provide string messages.
These messages are published to a service bus topic. The sample will also consume messages from the same service bus topic.


## What You Will Build
You will build an application using Spring Cloud Stream to send and receive messages for Azure Service Bus Topic.

## What You Need

- [An Azure subscription](https://azure.microsoft.com/free/)
- [Terraform](https://www.terraform.io/)
- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)
- [JDK8](https://www.oracle.com/java/technologies/downloads/) or later
- Maven
- You can also import the code straight into your IDE:
    - [IntelliJ IDEA](https://www.jetbrains.com/idea/download)

## Provision Azure Resources Required to Run This Sample
This sample will create Azure resources using Terraform. If you choose to run it without using Terraform to provision resources, please pay attention to:
> [!IMPORTANT]  
> If you choose to use a security principal to authenticate and authorize with Azure Active Directory for accessing an Azure resource
> please refer to [Authorize access with Azure AD](https://microsoft.github.io/spring-cloud-azure/docs/current/reference/html/index.html#authorize-access-with-azure-active-directory) to make sure the security principal has been granted the sufficient permission to access the Azure resource.

### Authenticate Using the Azure CLI
Terraform must authenticate to Azure to create infrastructure.

In your terminal, use the Azure CLI tool to setup your account permissions locally.

```shell
az login
```

Your browser window will open and you will be prompted to enter your Azure login credentials. After successful authentication, your terminal will display your subscription information. You do not need to save this output as it is saved in your system for Terraform to use.

```shell
You have logged in. Now let us find all the subscriptions to which you have access...

[
  {
    "cloudName": "AzureCloud",
    "homeTenantId": "home-Tenant-Id",
    "id": "subscription-id",
    "isDefault": true,
    "managedByTenants": [],
    "name": "Subscription-Name",
    "state": "Enabled",
    "tenantId": "0envbwi39-TenantId",
    "user": {
      "name": "your-username@domain.com",
      "type": "user"
    }
  }
]
```

If you have more than one subscription, specify the subscription-id you want to use with command below: 
```shell
az account set --subscription <your-subscription-id>
```

### Provision the Resources

After login Azure CLI with your account, now you can use the terraform script to create Azure Resources.

```shell
# In the root directory of the sample
# Initialize your Terraform configuration
terraform -chdir=./terraform init

# Apply your Terraform Configuration
# Type `yes` at the confirmation prompt to proceed.
terraform -chdir=./terraform apply

```

It may take a few minutes to run the script. After successful running, you will see prompt information like below:

```shell
azurecaf_name.resource_group: Creating...
azurecaf_name.azurecaf_name_servicebus: Creating...
azurecaf_name.resource_group: Creation complete ...
azurecaf_name.azurecaf_name_servicebus: Creation complete ...
azurerm_resource_group.main: Creating...
azurerm_resource_group.main: Creation complete ...
azurerm_servicebus_namespace.servicebus_namespace: Creating...
...
azurerm_servicebus_namespace.servicebus_namespace: Creation complete ...
azurerm_role_assignment.role_servicebus_data_owner: Creating...
azurerm_servicebus_topic.servicebus_topic: Creating...
azurerm_servicebus_topic.servicebus_topic: Creation complete ...
azurerm_servicebus_subscription.servicebus_subscription: Creating...
...
azurerm_servicebus_subscription.servicebus_subscription: Creation complete...
...
azurerm_role_assignment.role_servicebus_data_owner: Creation complete ...

Apply complete! Resources: 7 added, 0 changed, 0 destroyed.

Outputs:

...
```

You can go to [Azure portal](https://ms.portal.azure.com/) in your web browser to check the resources you created.

### Export Output to Your Local Environment
Running the command below to export environment values:

```shell
 source ./terraform/setup_env.sh
```

## Run Locally

In your terminal, run `mvn clean spring-boot:run`.


```shell
mvn clean spring-boot:run
```

## Verify This Sample


1.  Verify in your app’s logs that similar messages were posted:

```shell
...
...
New message received: 'Hello world, 2'
...
Message 'Hello world, 2' successfully checkpointed
...
New message received: 'Hello world, 3'
...
Message 'Hello world, 3' successfully checkpointed
...
...
```

## Clean Up Resources
After running the sample, if you don't want to run the sample, remember to destroy the Azure resources you created to avoid unnecessary billing.

The terraform destroy command terminates resources managed by your Terraform project.   
To destroy the resources you created.

```shell
terraform -chdir=./terraform destroy
```



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
### Set Service Bus message headers
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

