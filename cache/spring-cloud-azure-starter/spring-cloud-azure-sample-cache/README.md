# Spring Cloud Azure Sample redis cache

Redis support is based on spring-boot-starter-data-redis, which obtains and
Automatic configuration of redis properties through Azure Redis Cache Management SDK.

## What You Will Build
You will build an application using the Spring Boot Starter Redis, Spring Cloud Azure Starter and Spring Cloud Azure Resource Manager to cache data to Azure Cache for Redis.

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
> please refer to [Authorize access with Azure AD](https://microsoft.github.io/spring-cloud-azure/docs/current/reference/html/index.html#authorize-access-with-azure-active-directory) to make sure the security principal has been granted the sufficient permission to access the Azure resource.

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

After login Azure CLI with your account, at least two variables `azure_subscription_id` and `azure_tenant_id` according to the returned information in the *./terraform/variables.tf*.

Now you can use the terraform script to create Azure Resources.

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

azurecaf_name.resource_group: Creating...
azurecaf_name.azurecaf_name_dns_redis: Creating...
azurecaf_name.azurecaf_name_dns_redis: Creation complete after 0s ...
azurerm_redis_cache.redis: Still creating...
azurerm_redis_cache.redis: Still creating...
azurerm_redis_cache.redis: Creation complete after ...

Apply complete! Resources: 4 added, 0 changed, 0 destroyed.

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


1. Send a GET request to check, where `name` could be any string:

```shell
$ curl -XGET http://localhost:8080/{name}
```

1. Confirm from Azure Redis Cache console in Azure Portal:

```shell
$ keys *
```


## Clean Up Resources
After running the sample, if you don't want to run the sample, remember to destroy the Azure resources you created to avoid unnecessary billing.

The terraform destroy command terminates resources managed by your Terraform project.   
To destroy the resources you created.

```shell
terraform -chdir=./terraform destroy
```

## Troubleshooting

- Meet with  `Creating topics with default partitions/replication factor are only supported in CreateTopicRequest version 4+` error.

  ```text
  o.s.c.s.b.k.p.KafkaTopicProvisioner      : Failed to create topics
    org.apache.kafka.common.errors.UnsupportedVersionException: Creating topics with default partitions/replication factor are only supported in CreateTopicRequest version 4+. The following topics need values for partitions and replicas
  ```

  When this error is found, add this configuration item `spring.cloud.stream.kafka.binder.replicationFactor`, with the value set to at least 1. For more information, see [Spring Cloud Stream Kafka Binder Reference Guide](https://docs.spring.io/spring-cloud-stream-binder-kafka/docs/current/reference/html/spring-cloud-stream-binder-kafka.html).
