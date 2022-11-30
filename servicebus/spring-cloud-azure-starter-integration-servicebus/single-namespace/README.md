---
page_type: sample
languages:
- java
products:
- azure-service-bus
name: Sending and Receiving Message by Azure Service Bus (Single Namespaces) And Spring Integration in Spring Boot Application
description: This sample demonstrates how to send and receive message by Azure Service Bus (single namespaces) and Spring Integration in Spring Boot application.
---

# Sending and Receiving Message by Azure Service Bus (Single Namespaces) And Spring Integration in Spring Boot Application

This guide walks you through the process of accessing Servicebus Queues and Topics with Spring Integration.

## What You Will Build
You will build an application that use Azure Servicebus Queues and Topics to send and receive messages with [Spring Integration](https://spring.io/projects/spring-integration) APIs.

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
azurecaf_name.servicebus: Creating...
azurecaf_name.topic: Creating...
azurecaf_name.resource_group: Creating...
azurecaf_name.resource_group: Creation complete after 0s [id=mvwycgvrvqxrbyiy]
azurecaf_name.servicebus: Creation complete after 0s [id=kfvxhnbckoaabrfh]
azurecaf_name.topic: Creation complete after 0s [id=tixdrtltwgohxbde]
...
azurerm_servicebus_namespace.servicebus_namespace: Creating...
...
azurerm_servicebus_namespace.servicebus_namespace: ...
azurerm_servicebus_queue.application: Creating...
azurerm_servicebus_topic.application: Creating...
azurerm_role_assignment.role_servicebus_data_owner: Creating...
...
azurerm_servicebus_subscription.application: Creating...
azurerm_servicebus_queue.application: Creation complete after 9s ...
...
azurerm_role_assignment.role_servicebus_data_owner: Still creating... [30s elapsed]
azurerm_role_assignment.role_servicebus_data_owner: ...

Apply complete! Resources: 9 added, 0 changed, 0 destroyed.

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
SERVICEBUS_NAMESPACE=...
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

1. Send a POST request to service bus queue

        curl -X POST http://localhost:8080/queues?message=hello

2. Verify in your app’s logs that similar messages were posted:

        New message received: 'hello'
        Message 'hello' successfully checkpointed
3. Send a POST request to service bus topic

        curl -X POST http://localhost:8080/topics?message=hello

4. Verify in your app’s logs that similar messages were posted:

        New message received: 'hello'
        Message 'hello' successfully checkpointed

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

### Set Service Bus message headers

Users can get all the supported ServiceBus message headers [here](https://aka.ms/spring/docs/4.0.0#si-sb-headers) to configure.
