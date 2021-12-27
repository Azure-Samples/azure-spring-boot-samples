# Using Spring Cloud Azure Storage Blob Starter

This code sample demonstrates how to read and write files with the [Spring Resource](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#resources) abstraction for Azure Storage using the Spring Cloud Azure storage starter.

## What You Will build
You will build an application that use Spring Resource abstraction to read and write data with [Azure Storage Blob](https://azure.microsoft.com/services/storage/blobs/).

## What You Need

- [An Azure subscription](https://azure.microsoft.com/free/)
- [Terraform](https://www.terraform.io/)
- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)
- [JDK8](https://www.oracle.com/java/technologies/downloads/) or later
- Maven
- You can also import the code straight into your IDE:
    - [IntelliJ IDEA](https://www.jetbrains.com/idea/download)

## Prevision Azure Resources required to run this sample

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
azurerm_resource_group.main: Creation complete after 3s ...
azurerm_storage_account.application: Creating...
azurerm_storage_account.application: Still creating... [10s elapsed]
azurerm_storage_account.application: Creation complete after 39s ...
azurerm_storage_container.application: Creating...
azurerm_storage_container.application: Creation complete after 1s ...
azurerm_role_assignment.storage_blob_contributor: Creating...
azurerm_role_assignment.storage_blob_contributor: Still creating... [20s elapsed]
azurerm_role_assignment.storage_blob_contributor: Creation complete after 26s...

...
Apply complete! Resources: 6 added, 0 changed, 0 destroyed.

```

You can go to [Azure portal](https://ms.portal.azure.com/) in your web browser to check the resources you created.

### Export output to your local environment
Running the command below to export environment values:

```shell
 source ./terraform/setup_env.sh
```

## Run Locally

In your terminal, run `mvn clean spring-boot:run`.


```shell
mvn clean spring-boot:run
```

## Verify this Sample
Send a POST request to update file contents:
```shell
$ curl http://localhost:8080/blob -d "new message" -H "Content-Type: text/plain"
```

Verify by sending a GET request

```shell
$ curl -XGET http://localhost:8080/blob
```

## Clean up Resources
After running the sample, if you don't want to run the sample, remember to destroy the Azure resources you created to avoid unnecessary billing.

The terraform destroy command terminates resources managed by your Terraform project.   
To destroy the resources you created.

```shell
terraform -chdir=./terraform destroy
```
