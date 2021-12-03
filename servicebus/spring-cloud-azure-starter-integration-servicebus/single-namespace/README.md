# Using Servicebus Queue and Topic With Spring Integration 

This guide walks you through the process of accessing Servicebus Queues and Topics with Spring Integration.

## What You Will build
You will build an application that use Azure Servicebus Queues and Topics to send and receive messages withs [Spring Integration](https://spring.io/projects/spring-integration) APIs.

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
azurerm_servicebus_namespace_authorization_rule.application: Creation complete after 13s ...
azurerm_servicebus_subscription.application: Creation complete after 7s ...
azurerm_role_assignment.servicebus_data_owner: Still creating... [20s elapsed]
azurerm_role_assignment.servicebus_data_owner: Creation complete after 28s ...

Apply complete! Resources: 11 added, 0 changed, 0 destroyed.

Outputs:

SERVICEBUS_NAMESPACE = "${YOUR_SERVICEBUS_NAMESPACE}"

```

You can go to [Azure portal](https://ms.portal.azure.com/) in your web browser to check the resources you created.

### Export output to your local Environment
Running the command below to get environment values:

```shell
 terraform -chdir=./terraform output -json | jq -r --arg prefix "export " '$prefix + (
  . as $in
  | keys[]
  | ($in[.].value | tostring) as $value
  | ($in[.].sensitive | tostring) as $sensitive
  | [
    (. | ascii_upcase) + "=" + $value
    ]
  | .[])'  
  
```

you will get output like this
```shell
export SERVICEBUS_NAMESPACE=<your-servicebus-namespace>
```

Copy the output and paste it in you terminal to export the value to your local environment.


## Run locally

In your terminal, run `mvn clean spring-boot:run`.


```shell
mvn clean spring-boot:run
```



## Run on Azure Spring Cloud
You can also run the application on [Azure App Services.](https://azure.microsoft.com/services/app-service/)


### Deploy With Intellj plugin
If you want to upload with Intellj plugins you can check this [doc](https://docs.microsoft.com/en-us/azure/developer/java/toolkit-for-intellij/create-hello-world-web-app#deploying-web-app-to-azure) for guides.


## Clean up Resources
After running the sample, if you don't want to run the sample, remember to destroy the Azure resources you created to avoid unnecessary billing.

The terraform destroy command terminates resources managed by your Terraform project.   
To destroy the resources you created.

```shell
terraform -chdir=./terraform destroy
```




