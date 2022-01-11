# Spring Cloud Azure Starter Key Vault Secrets Sample 

This sample illustrates the usage of `spring-cloud-azure-starter-keyvault-secrets`. To learn all features, please refer to [reference doc](https://microsoft.github.io/spring-cloud-azure/docs/4.0.0-beta.2/reference/html/index.html).

## What You Will Build

You will build an application that use `spring-cloud-azure-starter-keyvault-secrets` to retrieve multiple secrets from multiple [Azure Key Vault](https://azure.microsoft.com/services/key-vault/).

## What You Need

- [An Azure subscription](https://azure.microsoft.com/free/)
- [Terraform](https://www.terraform.io/)
- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)
- [JDK8](https://www.oracle.com/java/technologies/downloads/) or later
- Maven
- You can also import the code straight into your IDE:
    - [IntelliJ IDEA](https://www.jetbrains.com/idea/download)

## Provision Azure Resources Required to Run This Sample

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
...
azurecaf_name.azurecaf_name_kv_02: Creating...
azurecaf_name.azurecaf_name_kv_01: Creating...
azurecaf_name.resource_group: Creating...
azurecaf_name.azurecaf_name_kv_01: Creation complete after 0s ...
azurecaf_name.resource_group: Creation complete after 0s ...
azurecaf_name.azurecaf_name_kv_02: Creation complete after 0s ...
azurerm_resource_group.main: Creating...
azurerm_resource_group.main: Creation complete after 3s ...
azurerm_key_vault.kv_account_02: Creating...
azurerm_key_vault.kv_account_01: Creating...
azurerm_key_vault.kv_account_02: Still creating... 
...
azurerm_key_vault_secret.kv_01: Creation complete ...
azurerm_key_vault_secret.kv_both_01: Creation complete ...
azurerm_key_vault.kv_account_02: Creation complete after ...
azurerm_key_vault_secret.kv_02_both: Creating...
azurerm_key_vault_secret.kv_02: Creating...
azurerm_key_vault_secret.kv_02_both: Creation complete ...
azurerm_key_vault_secret.kv_02: Creation complete ...

Apply complete! Resources: 10 added, 0 changed, 0 destroyed.

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

Start the application, you will see logs like this:
```text
secretNameInKeyVault1: secret-value-1
secretNameInKeyVault2: secret-name-in-key-vault-2-value-2
secretNameInKeyVaultBoth: secret-value-1
```

We can see that property-source-1 have higher priority.

## Clean Up Resources
After running the sample, if you don't want to run the sample, remember to destroy the Azure resources you created to avoid unnecessary billing.

The terraform destroy command terminates resources managed by your Terraform project.   
To destroy the resources you created.

```shell
terraform -chdir=./terraform destroy
```
