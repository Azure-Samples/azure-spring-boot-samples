# Using Spring Integration for Azure Service Bus With Multiple Destinations 

This code sample demonstrates how to use Spring Integration for Azure Service Bus with multiple destinations. 

## What You Will Build


You will build an application that using Spring Integration for Azure Service Bus to send and receive messages from one queue in one Service Bus namespace and then forward them to another queue in another Service Bus namespace.

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

# in the root directory of the sample
# Initialize your Terraform configuration
terraform -chdir=./terraform init

# Apply your Terraform Configuration
# Type `yes` at the confirmation prompt to proceed.
terraform -chdir=./terraform apply

```




It may take a few minutes to run the script. After successful running, you will see prompt information like below:

```shell

...
azurerm_role_assignment.servicebus_02_data_owner: Still creating... 
azurerm_role_assignment.servicebus_01_data_owner: Still creating... 
azurerm_role_assignment.servicebus_01_data_owner: Creation complete after 29s... 
azurerm_role_assignment.servicebus_02_data_owner: Creation complete after 29s... 

Apply complete! Resources: 14 added, 0 changed, 0 destroyed.

Outputs:

AZURE_SERVICEBUS_NAMESPACE_01 = "${YOUR_SERVICEBUS_NAMESPACE_01}"
AZURE_SERVICEBUS_NAMESPACE_02 = "${YOUR_SERVICEBUS_NAMESPACE_02}"
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
# in the root directory of the sample
mvn clean spring-boot:run
```


## Verify This Sample


Verify in your app’s logs that similar messages were posted:
```shell
Message was sent successfully for queue1.
...
New message received: 'hello'
Message 'hello' successfully checkpointed
...
Message was sent successfully for queue2.
```


## Clean Up Resources
After running the sample, if you don't want to run the sample, remember to destroy the Azure resources you created to avoid unnecessary billing.

The terraform destroy command terminates resources managed by your Terraform project.   
To destroy the resources you created.

```shell
terraform -chdir=./terraform destroy
```



