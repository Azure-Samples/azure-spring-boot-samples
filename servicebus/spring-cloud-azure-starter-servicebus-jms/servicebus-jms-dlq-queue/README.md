---
page_type: sample
languages:
- java
products:
- azure-service-bus
name: Sending and Receiving Message (DLQ) by Azure Service Bus (Queue) And Jms using Passwordless Connections
description: This sample demonstrates how to send and receive message (DLQ) by Azure Service Bus (queue) and JMS using passwordless connections
---

# Sending and Receiving (DLQ) Message by Azure Service Bus (Queue) And Jms using Passwordless connections

This sample project demonstrates how to use Spring JMS for Azure Service Bus Queue (`spring-cloud-azure-starter-servicebus-jms`) to consume DLQ message of the queue using passwordless connections.


## What You Will Build
You will build an application using Spring JMS to send and receive messages (DLQ) for Azure Service Bus Queue.

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
> If you choose to use a security principal to authenticate and authorize with Microsoft Entra ID for accessing an Azure resource
> please refer to [Authorize access with Microsoft Entra ID](https://learn.microsoft.com/azure/developer/java/spring-framework/authentication#authorize-access-with-microsoft-entra-id) to make sure the security principal has been granted the sufficient permission to access the Azure resource.

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
azurecaf_name.azurecaf_name_servicebus: Creating...
azurecaf_name.resource_group: Creating...
azurecaf_name.azurecaf_name_servicebus: Creation complete after 0s [id=qikkuohqscnhospw]
azurecaf_name.resource_group: Creation complete after 0s [id=hsgmdikkcuwtxecs]
azurerm_resource_group.main: Creating...
azurerm_resource_group.main: Creation complete after 5s ...
azurerm_servicebus_namespace.servicebus_namespace: Creating...
...
azurerm_servicebus_namespace.servicebus_namespace: Creation complete after ...
azurerm_servicebus_queue.queue: Creating...
azurerm_servicebus_queue.queue: Creation complete ...

Apply complete! Resources: 5 added, 0 changed, 0 destroyed.

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
SERVICEBUS_NAMESPACE_NAME_DLQ_QUQUE=...
PRICING_TIER=...
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

1. Send a POST request with a normal message to Service Bus queue:

   ```
   curl -X POST http://localhost:8080/queue?message=hello
   ```

   Verify in your app's logs that a similar message was posted:
    
   ```
   Sending message: hello
   ...
   Received message from queue: User{name='hello'}.
   ```

2. Send a POST request with an invalid message to DLQ of theService Bus queue:

   ```
   curl -X POST http://localhost:8080/queue?message=hello-invalid
   ```

   Verify in your app's logs that a similar message was posted:

   ```
   Sending message: hello-invalid
   Received message from queue: User{name='hello-invalid'}.
   Move message into dead letter queue: User{name='hello-invalid'}.
   Received message from dead letter queue: User{name='hello-invalid'}.
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

## Deploy to Azure Spring Apps

Now that you have the Spring Boot application running locally, it's time to move it to production. [Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/overview) makes it easy to deploy Spring Boot applications to Azure without any code changes. The service manages the infrastructure of Spring applications so developers can focus on their code. Azure Spring Apps provides lifecycle management using comprehensive monitoring and diagnostics, configuration management, service discovery, CI/CD integration, blue-green deployments, and more. To deploy your application to Azure Spring Apps, see [Deploy your first application to Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/quickstart?tabs=Azure-CLI).
