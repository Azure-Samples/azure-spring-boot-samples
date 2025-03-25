---
page_type: sample
languages:
- java
products:
- azure-key-vault
name: Enable Client SSL from Azure Key Vault SSL Bundles in Spring Boot webflux Application
description: This sample demonstrates how to enable Client SSL via Azure KeyVault SSL bundles in Spring Boot webflux application.
---

# Enable WebClient SSL from Azure Key Vault SSL Bundles in Spring Boot Reactive Web Application

This sample demonstrates how to enable WebClient SSL via Azure KeyVault SSL bundles in Spring Boot Reactive Web application.

## What You Will Build

You will build an application that use `spring-cloud-azure-starter-keyvault-jca` to retrieve certificates from multiple [Azure Key Vault](https://azure.microsoft.com/services/key-vault/).

## What You Need

- [An Azure subscription](https://azure.microsoft.com/free/)
- [Terraform](https://www.terraform.io/)
- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)
- [JDK 17](https://www.oracle.com/java/technologies/downloads/) or later
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
...
azurecaf_name.azurecaf_name_kv_01: Creating...
azurecaf_name.azurecaf_name_kv_02: Creating...
azurecaf_name.resource_group: Creating...
azurecaf_name.azurecaf_name_kv_01: Creation complete after 0s [id=tsnjmjbuwvumasse]
azurecaf_name.resource_group: Creation complete after 0s [id=ddeodontheybkwgm]
azurecaf_name.azurecaf_name_kv_02: Creation complete after 0s [id=tsnjmjbuwvumasse]
azuread_application.app: Creating...
azuread_application.app: Creation complete after 3s [id=37a44efb-1cd2-44e4-a149-d9bb9c315d6f]
azuread_application_password.service_principal_password: Creating...
azuread_service_principal.service_principal: Creating...


Apply complete! Resources: 11 added, 0 changed, 0 destroyed.

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
KEY_VAULT_SSL_BUNDLES_CLIENT_ID=
KEY_VAULT_SSL_BUNDLES_CLIENT_SECRET=
KEY_VAULT_SSL_BUNDLES_KEYVAULT_URI_01=
KEY_VAULT_SSL_BUNDLES_KEYVAULT_URI_02=
KEY_VAULT_SSL_BUNDLES_RESOURCE_GROUP_NAME=
KEY_VAULT_SSL_BUNDLES_TENANT_ID=
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

This sample requires an SSL server, you can use sample `spring-cloud-azure-starter-keyvault-jca/ssl-bundles-server` as the target server, which means the https://localhost:8444/ssl-test is available.For Azure resource usage, you can share the output environment variable of `spring-cloud-azure-starter-keyvault-jca/ssl-bundles-server` or create the new resources and shared to `spring-cloud-azure-starter-keyvault-jca/ssl-bundles-web-client` as they use the same environment variables.

1. Send below request to acquire a resource with TLS connection, the server side should not enable client-auth via property `server.ssl.client-auth=NEED`:

   ```bash
   curl http://localhost:8081/webclient/tls
   ```
   
   You will see the following in the console:
   
   ```console
   Response from webClient tls "https://localhost:8443/ssl-test": Inbound TLS is working!
   ```

2. Send below request to acquire a resource with mTLS connection, the server side should enable client-auth via property `server.ssl.client-auth=NEED`:

   ```bash
   curl http://localhost:8081/webclient/mtls
   ```
   
   you will see console like this:
   
   ```console
   Response from webClient mtls "https://localhost:8443/ssl-test": Inbound TLS is working!
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
