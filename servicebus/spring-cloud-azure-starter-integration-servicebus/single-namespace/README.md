# Using Servicebus Queue and Topic With Spring Integration 

This guide walks you through the process of accessing Servicebus Queues and Topics with Spring Integration.

## What You Will build
You will build an application that use Azure Servicebus Queues and Topics to send and receive messages.

## What You Need

- An Azure [subscription](https://azure.microsoft.com/en-us/free/)
- [Terraform](https://www.terraform.io/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=mac) 
- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)
- JDK8
- Maven
- jq

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

```shell
az account set --subscription <your-subscription-id>
```

### Provision the Resources
1. Before running terraform, there're two parameters needed to be updated in the `./terraform/variables.tf` file.

```shell
variable "application_name" {
  type        = string
  description = "The name of your application"
  default     = "<your-alias>-sample"
}

variable "service_principal_id" {
  type        = string
  description = "The Azure Service Principal id or object_id to assign role to"
  default     = ""
}
```

2. Use the terraform script to create Azure Resources
```shell

cd terraform

# 
terraform init
# it may take a few minutes
terraform apply
```


## Run locally

Open your terminal, and input the command below.

```shell
mvn clean spring-boot:run
```

### Get Environment
Run the command below

Note: Make user `jq` has been installed.

```shell
 terraform output -json | jq -r '
  . as $in
  | keys[]
  | ($in[.].value | tostring) as $value
  | ($in[.].sensitive | tostring) as $sensitive
  | [
    (. | ascii_upcase) + "=" + $value
    ]
  | .[]'  
  
```


## Run on App Service
You can also run the application on [Azure App Services.](https://azure.microsoft.com/services/app-service/)

### Deploy With Maven
1. Fill in the value in the pom.xml file.

```shell
<subscriptionId>${your-subscriptionId}</subscriptionId>
<resourceGroup>${your-resourceGroup}</resourceGroup>
<appName>${your-appName}</appName>
```

2. Open you terminal and run
```shell
mvn package azure-webapp:deploy
```

### Deploy With Intellj plugin
If you want to upload with Intellj plugins you can check this [doc](https://docs.microsoft.com/en-us/azure/developer/java/toolkit-for-intellij/create-hello-world-web-app#deploying-web-app-to-azure) for guides.


## Clean up Resources

The terraform destroy command terminates resources managed by your Terraform project. To destroy the resources you created.

```shell
terraform destroy
```




