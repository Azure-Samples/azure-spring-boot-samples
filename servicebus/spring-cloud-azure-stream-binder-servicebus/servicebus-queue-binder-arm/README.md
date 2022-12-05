---
page_type: sample
languages:
- java
products:
- azure-service-bus
name: Sending and Receiving Message by Azure Service Bus (Queue) (Retrieving Connection String via Arm) And Spring Cloud Stream Binder in Spring Boot Application
description: This sample demonstrates how to send and receive message by Azure Service Bus (queue) (retrieving connection string via ARM) and Spring Cloud Stream Binder in Spring Boot application.
---

# Sending and Receiving Message by Azure Service Bus (Queue) (Retrieving Connection String via Arm) And Spring Cloud Stream Binder in Spring Boot Application


This code sample demonstrates how to use the Spring Cloud Stream binder for
Azure Service Bus Queue by retrieving connection string via ARM.


## What You Will Build
You will build an application using Spring Cloud Stream to send and receive messages for Azure Service Bus Queue.
This sample will
- Create a Service Bus queue and a service principal and assign the `Contributor` role of the service bus to the service principal.
- Connect to the ARM using the service principal and retrieve the connection string of the service bus.
- Connect to the Service Bus queue to send and receive messages.

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
azurecaf_name.resource_group: Creating...
azurecaf_name.azurecaf_name_servicebus: Creating...
azurecaf_name.azurecaf_name_servicebus: Creation complete ...
azurecaf_name.resource_group: Creation complete ...
azuread_application.servicebusqueuebinder: Creating...
azuread_application.servicebusqueuebinder: Creation complete ...
azuread_application_password.servicebusqueuebinder: Creating...
azuread_service_principal.servicebusqueuebinder: Creating...
azuread_service_principal.servicebusqueuebinder: Creation complete ...
azuread_application_password.servicebusqueuebinder: Creation complete ...
azurerm_resource_group.main: Creating...
azurerm_resource_group.main: Creation complete ...
azurerm_servicebus_namespace.servicebus_namespace: Creating...
...
azurerm_servicebus_namespace.servicebus_namespace: Creation complete ...
azurerm_role_assignment.role_servicebus_data_contributor: Creating...
azurerm_servicebus_queue.queue: Creating...
azurerm_servicebus_queue.queue: Creation complete ...
...
azurerm_role_assignment.role_servicebus_data_contributor: Creation complete ...

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
AZURE_SERVICEBUS_NAMESPACE=...
AZURE_SERVICEBUS_QUEUE_NAME=...
AZURE_SERVICEBUS_SUBSCRIPTION_ID=...
AZURE_SERVICEBUS_RESOURCE_GROUP=...
AZURE_SERVICE_PRINCIPAL_CLIENT_ID=...
AZURE_SERVICE_PRINCIPAL_CLIENT_SECRET=...
AZURE_SERVICE_PRINCIPAL_TENANT_ID=...
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
New message received: 'Hello world, 2'
...
Message 'Hello world, 2' successfully checkpointed
...
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

#### Run with Bash

```shell
terraform -chdir=./terraform destroy -auto-approve
```

#### Run with Powershell

```shell
terraform -chdir=terraform destroy -auto-approve
```
