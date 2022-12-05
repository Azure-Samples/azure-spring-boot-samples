---
page_type: sample
languages:
- java
products:
- azure-event-hubs
name: Sending and Receiving Message by Azure Event Hubs and Spring Cloud Stream Binder Event Hubs (Multi Binder) In Spring Boot Application
description: This sample demonstrates how to send and receive message by Azure Event Hubs and Spring Cloud Stream Binder EventHubs (multi binder) in Spring Boot application.
---

# Sending and Receiving Message by Azure Event Hubs and Spring Cloud Stream Binder Event Hubs (Multi Binder) In Spring Boot Application

This sample demonstrates how to use the `Spring Cloud Stream Binder`
for multiple `Azure Event Hubs` namespaces. In this sample you will bind to
two Event Hubs namespaces separately through two binders. The sample app has two operating modes.
One way is to expose a RESTful API to receive string message, another way is to automatically provide string messages.
These messages are published to one event hub. The sample will also consume messages from the same
event hub.

## What You Will Build
You will build an application using `Spring Cloud Stream Binder` to send and receive messages for multiple `Azure Event Hubs` namespaces.

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
> please refer to [Authorize access with Azure AD](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#authorize-access-with-azure-active-directory) to make sure the security principal has been granted the sufficient permission to access the Azure resource.

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

#### Run with Bash

```shell
# In the root directory of the sample
# Initialize your Terraform configuration
terraform -chdir=./terraform init

# Apply your Terraform Configuration
terraform -chdir=./terraform apply -auto-approve

```

#### Run with Powershell

```shell
# In the root directory of the sample
# Initialize your Terraform configuration
terraform -chdir=terraform init

# Apply your Terraform Configuration
terraform -chdir=terraform apply -auto-approve

```

It may take a few minutes to run the script. After successful running, you will see prompt information like below:

```shell

azurecaf_name.azurecaf_name_storage_account: Creating...
azurecaf_name.resource_group: Creating...
...
azurecaf_name.azurecaf_name_storage_account: Creation complete ...
azurecaf_name.resource_group: Creation complete after 0s ...
azurecaf_name.azurecaf_name_eventhubs_01: Creation complete after 0s ...
azurerm_resource_group.main: Creating...
azurerm_resource_group.main: Creation complete after 5s ...
azurerm_eventhub_namespace.eventhubs_namespace_02: Creating...
...
azurerm_eventhub.eventhubs_02: Creation complete after ...
azurerm_role_assignment.role_eventhubs_data_owner_02: Creating...
azurerm_eventhub.eventhubs_01: Creation complete after ...
azurerm_role_assignment.role_eventhubs_data_owner_01: Creating...
...
azurerm_role_assignment.role_eventhubs_data_owner_02: Creation complete ...
azurerm_role_assignment.role_eventhubs_data_owner_01: Creation complete ...
...

Apply complete! Resources: 15 added, 0 changed, 0 destroyed.

Outputs:

...


```

You can go to [Azure portal](https://ms.portal.azure.com/) in your web browser to check the resources you created.

### Export Output to Your Local Environment
Running the command below to export environment values:

#### Run with Bash

```shell
source ./terraform/setup_env.sh
```

#### Run with Powershell

```shell
terraform\setup_env.ps1
```

If you want to run the sample in debug mode, you can save the output value.

```shell
AZURE_STORAGE_CONTAINER_NAME=...
AZURE_STORAGE_ACCOUNT_NAME=...
EVENTHUB_NAMESPACE_01=...
AZURE_EVENTHUB_NAME_01=...
AZURE_EVENTHUB_CONSUMER_GROUP_01=...
EVENTHUB_NAMESPACE_02=...
AZURE_EVENTHUB_NAME_02=...
AZURE_EVENTHUB_CONSUMER_GROUP_02=...
```

## Run Locally

### Run the sample with Maven

In your terminal, run `mvn clean spring-boot:run`.

```shell
mvn clean spring-boot:run
```

### Run the sample in IDEs

You can debug your sample by adding the saved output values to the tool's environment variables or the sample's `application.yaml` file.

* If your tool is `IDEA`, please refer to [Debug your first Java application](https://www.jetbrains.com/help/idea/debugging-your-first-java-application.html) and [add environment variables](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html#add-environment-variables).

* If your tool is `ECLIPSE`, please refer to [Debugging the Eclipse IDE for Java Developers](https://www.eclipse.org/community/eclipse_newsletter/2017/june/article1.php) and [Eclipse Environment Variable Setup](https://examples.javacodegeeks.com/desktop-java/ide/eclipse/eclipse-environment-variable-setup-example/).

## Verify This Sample

1.  Verify in your app’s logs that similar messages were posted:

```shell

...
Message1 'GenericMessage [payload=Hello world1, 11,... ]' successfully checkpointed
Message1 'GenericMessage [payload=Hello world1, 12,... ]' successfully checkpointed
...

Message1 'GenericMessage [payload=Hello world1, 23,... ]' successfully checkpointed
...
Message2 'GenericMessage [payload=Hello world2, 25,... ]' successfully checkpointed
...

```

## Clean Up Resources
After running the sample, if you don't want to run the sample, remember to destroy the Azure resources you created to avoid unnecessary billing.

The terraform destroy command terminates resources managed by your Terraform project.   
To destroy the resources you created.

#### Run with Bash

```shell
terraform -chdir=./terraform destroy -auto-approve
```

#### Run with Powershell

```shell
terraform -chdir=terraform destroy -auto-approve
```

## Enhancement

### [Enable sync message](https://aka.ms/spring/docs/4.0.0#producer-properties)

To enable message sending in a synchronized way with Spring Cloud Stream 3.x, spring-cloud-azure-stream-binder-eventhubs supports the sync producer mode to get responses for sent messages.

Make sure set `spring.cloud.stream.eventhubs.bindings.<binding-name>.producer.sync=true` before use it.

### [Using Batch Consuming](https://aka.ms/spring/docs/4.0.0#batch-consumer-support)

To work with the batch-consumer mode, the property of spring.cloud.stream.bindings.<binding-name>.consumer.batch-mode should be set as true. When enabled, an org.springframework.messaging.Message of which the payload is a list of batched events will be received and passed to the consumer function.

In this sample, users can try the batch-consuming mode by enabling the "batch" profile and fill the "application-batch.yml". For more details about how to work in batch-consuming mode, please refer to the [reference doc](https://aka.ms/spring/docs/4.0.0#batch-consumer-support-2).

### Set Event Hubs message headers

Users can get all the supported EventHubs message headers [here](https://aka.ms/spring/docs/4.0.0#scs-eh-headers) to configure.

### Resource Provision

Event Hubs binder supports provisioning of event hub and consumer group, users could use [properties](https://aka.ms/spring/docs/4.0.0#resource-provision) to enable provisioning.

### Partitioning Support

A PartitionSupplier with user-provided partition information will be created to configure the partition information about the message to be sent. The binder supports Event Hubs partitioning by allowing setting partition key and id. Please refer to the [reference doc](https://aka.ms/spring/docs/4.0.0#partitioning-support-2) for more details.

### Error Channel

Event Hubs binder supports consumer error channel, producer error channel and global default error channel, click [here](https://aka.ms/spring/docs/4.0.0#error-channels) to see more information.
