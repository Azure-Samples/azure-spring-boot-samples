# Azure Spring Boot Sample Cosmos Multi Database Multi Account for Java

This guide demonstrates how to use Azure Cosmos DB via `azure-spring-data-cosmos` to store data in and retrieve data from your Azure Cosmos DB.

## What You Will Build
You will build an application to write data to and query data from Azure Cosmos DB via `azure-spring-data-cosmos`.

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
azurecaf_name.cosmos_02: Creating...
azurecaf_name.cosmos_01: Creating...
azurecaf_name.resource_group: Creation complete after 0s [id=lsayywncwrhxslhx]
azurecaf_name.cosmos_01: Creation complete after 0s [id=horcpnoxqwmegvlx]
azurecaf_name.cosmos_02: Creation complete after 0s [id=horcpnoxqwmegvlx]
azurerm_resource_group.main: Creating......
...
azurerm_cosmosdb_account.application_01: Creating...
azurerm_cosmosdb_account.application_02: Creating...
...
azurerm_cosmosdb_sql_database.db_01: Creating...
...
...
Apply complete! Resources: 13 added, 0 changed, 0 destroyed.

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
 . terraform\setup_env.ps1
```

## Run Locally

In your terminal, run `mvn clean spring-boot:run`.


```shell
mvn clean spring-boot:run
```

## Verify This Sample

Verify Result:
The corresponding data is added to the mysql database and cosmos database
    ![Result in MYSQL](resource/result-in-mysql.png)
    ![Result in Primary Cosmos Database](resource/result-in-primary-cosmos-database.png)
    ![Result in Secondary Cosmos Database](resource/result-in-secondary-cosmos-database.png)

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
