# Using Spring Integration for Azure Service Bus with multiple destinations. 

This code sample demonstrates how to use Spring Integration for Azure Service Bus with multiple destinations. 

## What You Will build


You will build an application that using Spring Integration for Azure Service Bus to send and receive messages from one queue in one Service Bus namespace and then forward them to another queue in another Service Bus namespace.

## What You Need

- [An Azure subscription](https://azure.microsoft.com/en-us/free/)
- [Terraform](https://www.terraform.io/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=mac)
- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)
- [jq](https://stedolan.github.io/jq/)
- JDK8
- Maven

## Prevision Azure Resources required to run this sample.

### Authenticate using the Azure CLI
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

If you have more than one subscriptions, specify the subscription-id you want to use with command below:
```shell
az account set --subscription <your-subscription-id>
```

### Provision the Resources

After login Azure CLI with your accout, now you can use the terraform script to create Azure Resources.

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
azurerm_role_assignment.servicebus_03_data_owner: Still creating... 
azurerm_role_assignment.servicebus_01_data_owner: Still creating... 
azurerm_role_assignment.servicebus_01_data_owner: Creation complete after 29s... 
azurerm_role_assignment.servicebus_03_data_owner: Creation complete after 29s... 
azurerm_role_assignment.servicebus_02_data_owner: Creation complete after 29s... 

Apply complete! Resources: 14 added, 0 changed, 0 destroyed.

Outputs:

AZURE_SERVICEBUS_NAMESPACE_01 = "${YOUR_SERVICEBUS_NAMESPACE_01}"
AZURE_SERVICEBUS_NAMESPACE_02 = "${YOUR_SERVICEBUS_NAMESPACE_02}"
AZURE_SERVICEBUS_NAMESPACE_03 = "${YOUR_SERVICEBUS_NAMESPACE_03}"


```

You can go to [Azure portal](https://ms.portal.azure.com/) in your web browser to check the resources you created.

### Export output to your local Environment
Running the command below to export environment values:

```shell
 source ./terraform/environment_values.sh 
```

## Run locally

In your terminal, run `mvn clean spring-boot:run`.


```shell
# in the root directory of the sample
mvn clean spring-boot:run
```


## Verify this sample
Send a POST request to service bus queue
```shell
 $ curl -X POST http://localhost:8080/queues?message=hello
```

Verify in your app’s logs that a similar message was posted:
```shell
Message was sent successfully for queue1.
New message received: 'hello'
Message 'hello' successfully checkpointed
Message was sent successfully for queue2.
```


## Clean up Resources
After running the sample, if you don't want to run the sample, remember to destroy the Azure resources you created to avoid unnecessary billing.

The terraform destroy command terminates resources managed by your Terraform project.   
To destroy the resources you created.

```shell
terraform -chdir=./terraform destroy
```



