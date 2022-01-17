# Spring Cloud Azure Stream Binder for Multiple Service Bus Namespaces Code Sample shared library for Java 

This code sample demonstrates how to use the Spring Cloud Stream Binder for
multiple Azure Service Bus namespaces. In this sample you will bind to two Service Bus namespaces separately through
a queue binder and a topic binder.The sample app has two operating modes. One way is to expose a Restful API to receive string message,
another way is to automatically provide string messages. These messages are published to a service bus.
The sample will also consume messages from the same service bus.

## What You Will Build
You will build an application using Spring Cloud Stream to send and receive messages for multiple Azure Service Bus namespaces.

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
azurecaf_name.servicebus_01: Creating...
azurecaf_name.resource_group: Creating...
...
azurerm_resource_group.main: Creating...
azurerm_resource_group.main: Creation complete after 4s ...
azurerm_servicebus_namespace.servicebus_namespace_02: Creating...
azurerm_servicebus_namespace.servicebus_namespace_01: Creating...
...
azurerm_servicebus_namespace.servicebus_namespace_02: Creation complete ...
azurerm_role_assignment.role_servicebus_data_owner_02: Creating...
azurerm_servicebus_queue.servicebus_namespace_02_queue: Creating...
azurerm_servicebus_namespace.servicebus_namespace_01: Creation complete ...
azurerm_role_assignment.role_servicebus_data_owner_01: Creating...
azurerm_servicebus_topic.servicebus_namespace_01_topic: Creating...
...
azurerm_servicebus_subscription.servicebus_namespace_01_sub: Creating...
...
azurerm_role_assignment.role_servicebus_data_owner_02: Creation complete ...
azurerm_role_assignment.role_servicebus_data_owner_01: Creation complete ...

Apply complete! Resources: 11 added, 0 changed, 0 destroyed.

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
Message 'Hello world1, 3' successfully checkpointed
...
...
Message 'Hello world1, 4' successfully checkpointed
...
...
Message 'Hello world2, 3' successfully checkpointed
...
...
Message 'Hello world2, 5' successfully checkpointed
...
...

Sending message2...
...
New message2 received...

```

## Clean Up Resources
After running the sample, if you don't want to run the sample, remember to destroy the Azure resources you created to avoid unnecessary billing.

The terraform destroy command terminates resources managed by your Terraform project.   
To destroy the resources you created.

```shell
terraform -chdir=./terraform destroy
```
