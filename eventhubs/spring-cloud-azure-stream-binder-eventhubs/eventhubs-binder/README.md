# Spring Cloud Azure Stream Binder for Event Hub Code Sample shared library for Java


This code sample demonstrates how to use the `Spring Cloud Stream Binder` for `Azure Event Hubs`.The
sample app has two operating modes.
One way is to expose a Restful API to receive string message, another way is to automatically provide string messages.
These messages are published to one `Event Hub` instance and then consumed by one consumer
endpoint from the same application.

## What You Will Build
You will build an application using `Spring Cloud Stream Binder` to send and receive messages for `Azure Event Hubs`.

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


azurerm_resource_group.main: Creating...
azurerm_resource_group.main: Creation complete after 3s [id=/subscriptions/799c12ba-353c-44a1-883d-84808ebb2216/resourceGroups/rg-eventhubs-binder-nxatj]
azurerm_eventhub_namespace.eventhubs_namespace: Creating...
azurerm_storage_account.storage_account: Creating...
...
azurerm_storage_account.storage_account: Creation complete ...
azurerm_storage_container.storage_container: Creating...
azurerm_role_assignment.role_storage_account_contributor: Creating...
azurerm_storage_container.storage_container: Creation complete ...
azurerm_role_assignment.role_storage_blob_data_owner: Creating...
...
azurerm_role_assignment.role_storage_blob_data_owner: Creation complete ...
azurerm_role_assignment.role_storage_account_contributor: Creation complete ...
...
azurerm_eventhub_namespace.eventhubs_namespace: Creation complete ...
azurerm_eventhub.eventhubs: Creating...
azurerm_eventhub.eventhubs: Creation complete ...
...
azurerm_role_assignment.role_eventhubs_data_owner: Creation complete ...

Apply complete! Resources: 8 added, 0 changed, 0 destroyed.

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
New message received: 'Hello world, 17' ...
Message 'Hello world, 17' successfully checkpointed
...
New message received: 'Hello world, 18' ...
Message 'Hello world, 18' successfully checkpointed
...
New message received: 'Hello world, 27' ...
Message 'Hello world, 27' successfully checkpointed

```


## Clean Up Resources
After running the sample, if you don't want to run the sample, remember to destroy the Azure resources you created to avoid unnecessary billing.

The terraform destroy command terminates resources managed by your Terraform project.   
To destroy the resources you created.

```shell
terraform -chdir=./terraform destroy
```


## Enhancement

### Enable sync message
To enable message sending in a synchronized way with Spring Cloud Stream 3.x,
azure-spring-cloud-stream-binder-eventhubs supports the sync producer mode to get responses for sent messages.
By enabling following configuration, you could use [StreamBridge][StreamBridge] for the synchronized message producing.

```yaml
spring:
  cloud:
    stream:
      eventhubs:
        bindings:
          supply-out-0:
            producer:
              sync: true
```

### Using Batch Consuming
To enable [batch consuming][spring-cloud-stream-batch0-consumer] feature, you should add below configuration in the `batch` profile.

```yaml
spring:
  cloud:
    azure:
      eventhubs:
        connection-string:  ${AZURE_EVENTHUBS_CONNECTION_STRING}
        processor:
          checkpoint-store:
            container-name: ${AZURE_STORAGE_CONTAINER_NAME}
            account-name: ${AZURE_STORAGE_ACCOUNT_NAME}
            account-key: ${AZURE_STORAGE_ACCOUNT_KEY}
    stream:
      function:
        definition: consume;supply
      bindings:
        consume-in-0:
          destination: ${AZURE_EVENTHUB_NAME}
          group: ${AZURE_EVENTHUB_CONSUMER_GROUP}
          consumer:
            batch-mode: true
        supply-out-0:
          destination: ${SAME_AS_ABOVE_DESTINATION}
      eventhubs:
        bindings:
          consume-in-0:
            consumer:
              batch:
                max-size: 10 # The default value is 10
                max-wait-time: 1m # Optional, the default value is null
              checkpoint:
                mode: BATCH
      default:
        producer:
          errorChannelEnabled: true
      poller:
        initial-delay: 0
        fixed-delay: 1000
```
For checkpointing mode as BATCH, you can use below code to send messages and consume in batches,
see the [BatchProducerAndConsumerConfiguration.java][BatchProducerAndConsumerConfiguration]
```java
    @Bean
    public Consumer<List<String>> consume() {
        return list -> list.forEach(event -> LOGGER.info("New event received: '{}'",event));
    }

    @Bean
    public Supplier<Message<String>> supply() {
        return () -> {
            LOGGER.info("Sending message, sequence " + i);
            return MessageBuilder.withPayload("\"Hello world "+ i++ +"\"").build();
        };
    }
```

For checkpointing mode as MANUAL, you can use below code to send messages and consume/checkpoint in batches.
```java
    @Bean
    public Consumer<Message<List<String>>> consume() {
        return message -> {
            for (int i = 0; i < message.getPayload().size(); i++) {
                LOGGER.info("New message received: '{}', partition key: {}, sequence number: {}, offset: {}, enqueued time: {}",
                    message.getPayload().get(i),
                    ((List<Object>) message.getHeaders().get(EventHubHeaders.PARTITION_KEY)).get(i),
                    ((List<Object>) message.getHeaders().get(EventHubHeaders.SEQUENCE_NUMBER)).get(i),
                    ((List<Object>) message.getHeaders().get(EventHubHeaders.OFFSET)).get(i),
                    ((List<Object>) message.getHeaders().get(EventHubHeaders.ENQUEUED_TIME)).get(i));
            }
        
            Checkpointer checkpointer = (Checkpointer) message.getHeaders().get(CHECKPOINTER);
            checkpointer.success()
                        .doOnSuccess(success -> LOGGER.info("Message '{}' successfully checkpointed", message.getPayload()))
                        .doOnError(error -> LOGGER.error("Exception found", error))
                        .subscribe();
        };
    }

    @Bean
    public Supplier<Message<String>> supply() {
        return () -> {
            LOGGER.info("Sending message, sequence " + i);
            return MessageBuilder.withPayload("\"Hello world "+ i++ +"\"").build();
        };
    }
```

NOTE: in the batch-consuming mode, the default content type of Spring Cloud Stream binder is **application/json**, so make sure the message payload is aligned with the content-type. For example, when using the default content type of **application/json** to received messages with **String** payload, the payload should be a JSON String, surrounded with double quotes for the original String text. While for **text/plain** content type, it can be a **String** object directly.

For more details, please refer to the official doc of [Spring Cloud Stream Content Type Negotiation][content-type-negotiation].


<!-- LINKS -->
[StreamBridge]: https://docs.spring.io/spring-cloud-stream/docs/3.1.3/reference/html/spring-cloud-stream.html#_sending_arbitrary_data_to_an_output_e_g_foreign_event_driven_sources
[spring-cloud-stream-batch0-consumer]: https://docs.spring.io/spring-cloud-stream/docs/3.1.4/reference/html/spring-cloud-stream.html#_batch_consumers
[BatchProducerAndConsumerConfiguration]:
src/main/java/com/azure/spring/sample/eventhubs/binder/BatchProducerAndConsumerConfiguration.java
[deploy-spring-boot-application-to-app-service]: https://docs.microsoft.com/java/azure/spring-framework/deploy-spring-boot-java-app-with-maven-plugin?toc=%2Fazure%2Fapp-service%2Fcontainers%2Ftoc.json&view=azure-java-stable
[content-type-negotiation]: https://docs.spring.io/spring-cloud-stream/docs/current/reference/html/spring-cloud-stream.html#content-type-management

