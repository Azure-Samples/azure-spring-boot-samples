# Using Spring Cloud Azure App Configuration Starter

This sample illustrates the simplest usage of `spring-cloud-azure-starter-appconfiguration`.

## What You Will Build
You will build an application that use Spring Resource abstraction to read and write data with [Azure Storage Blob](https://azure.microsoft.com/services/storage/blobs/).

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

#### Run with Powershell, Command Prompt or Native Tools Command Prompt

```shell
# In the root directory of the sample
# Initialize your Terraform configuration
terraform -chdir=terraform init

# Apply your Terraform Configuration
terraform -chdir=terraform apply -auto-approve

```

It may take a few minutes to run the script. After successful running, you will see prompt information like below:

```shell
azurecaf_name.appconfig: Creating...
azurecaf_name.resource_group: Creating...
...
azurerm_resource_group.main: Creating...
...
azurerm_role_assignment.appconf_dataowner: Creating...
...
Apply complete! Resources: 6 added, 0 changed, 0 destroyed.

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

#### Run with Command Prompt or Native Tools Command Prompt

```shell
terraform\setup_env.bat
```

If you want to run the sample in debug mode, you can save the output value.

```shell
APPCONFIGURATION_ENDPOINT=...
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

### Run the sample based on Spring Native

There are two main ways to build a Spring Boot native application.

#### Run with Buildpacks

- System Requirements

Docker should be installed, see [System Requirements](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-buildpacks-system-requirements) for more details.

- Build the native application

Run `mvn -Pbuildpack package spring-boot:build-image`, see [Build the native application](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#_build_the_native_application) for more details.

```shell
mvn -Pbuildpack package spring-boot:build-image
```

- Run the native application

Run `docker run --rm -p 8080:8080 appconfiguration-sample:1.0.0`, see [Run the native application](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#_run_the_native_application) for more details.
```shell
docker run --rm -p 8080:8080 appconfiguration-sample:1.0.0
```

#### Run with Native Build Tools

- System Requirements

GraalVM `native-image` compiler should be installed, see [System Requirements](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-native-image-system-requirements) for more details. If using the Windows platform, you need to install `Visual Studio Build Tools`.

- Build the native application

Run `mvn -Pnative -DskipTests package`, see [Build the native application](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#_build_the_native_application_2) for more details.

```shell
mvn -Pnative -DskipTests package
```

When you encounter the `The command line is too long` exception when executing the native-image command, please use **shade** profile instead of **native** to build, and you must turn off the Spring Cloud Azure compatibility verification function (`spring.cloud.azure.compatibility-verifier.enabled=false`).

```shell
mvn -Pshade -DskipTests package
```

- Run the native application

Run `target\appconfiguration-sample`, see [Run the native application](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#_run_the_native_application_2) for more details.
```shell
target\appconfiguration-sample
```

## Verify This Sample
View the below results in the console.

```text
INFO 15116 --- [           main] .s.s.a.AppConfigurationSampleApplication : Returned the from Azure App Configuration: sample-key, hello from default application
```


## Clean Up Resources
After running the sample, if you don't want to run the sample, remember to destroy the Azure resources you created to avoid unnecessary billing.

The terraform destroy command terminates resources managed by your Terraform project.   
To destroy the resources you created.

#### Run with Bash

```shell
terraform -chdir=./terraform destroy -auto-approve
```

#### Run with Powershell, Command Prompt or Native Tools Command Prompt

```shell
terraform -chdir=terraform destroy -auto-approve
```
