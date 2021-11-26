# Using Servicebus Queue and Topic With Spring Integration 

This guide walks you through the process of accessing Servicebus Queues and Topics with Spring Integration.

## What You Will build
You will build an application that use Azure Servicebus Queues and Topics to send and receive messages.

## What You Need

- [Terraform](https://www.terraform.io/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=mac) 
- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli)
- JDK8
- Maven

## Prevision Azure Resources required to run this sample.

## Provision the Resources
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

# Login to azure-cli
az login
az account set --subscription <your-subscription-id>

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

```shell
terraform destroy
```




