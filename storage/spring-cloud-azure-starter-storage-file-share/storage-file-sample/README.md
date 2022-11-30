---
page_type: sample
languages:
- java
products:
- azure-files
name: Reading and Writing Files Stored in Azure Files by Spring Resource Abstraction in Spring Boot Application
description: This sample demonstrates how to read and write files in Azure Files and Spring Resource abstraction in Spring Boot application.
---

# Reading and Writing Files Stored in Azure Files by Spring Resource Abstraction in Spring Boot Application

This code sample demonstrates how to read and write files with the [Spring Resource](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#resources) abstraction for Azure Storage using the Spring Cloud Azure storage starter.

## What You Will Build
You will build an application that use Spring Resource abstraction to read and write data with [Azure Storage File Share](https://azure.microsoft.com/services/storage/files/).

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
> Storage File Share clients don't support authenticating using Azure AD service principals or managed identities.

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

azurerm_resource_group.main: Creating...
azurerm_resource_group.main: Creation complete after 5s ...
azurerm_storage_account.application: Creating...
azurerm_storage_account.application: Still creating... [10s elapsed]
azurerm_storage_account.application: Creation complete after 36s ...
azurerm_storage_share.application: Creating...
azurerm_storage_share.application: Creation complete after 3s ...

...
Apply complete! Resources: 5 added, 0 changed, 0 destroyed.

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
AZURE_STORAGE_ACCOUNT=...
STORAGE_SHARE_NAME=...
STORAGE_ACCOUNT_KEY=...
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
1. Write and read a file.  
    1.1 Send a POST request to update file contents.
    ```shell
    curl http://localhost:8080/file/file1.txt -d "new message" -H "Content-Type: text/plain"
    ```
    1.2 Verify by sending a GET request.  
    ```shell
    curl -XGET http://localhost:8080/file/file1.txt
    ```


2. [Optional] Using AzureStorageFileProtocolResolver to get Azure Storage File resources with file pattern.
    ```shell
    curl -XGET http://localhost:8080/file
    ```
    
    Verify in app's log that a similar messages was posted:
    ```shell
    10 resources founded with pattern:*.txt
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
