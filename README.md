# Spring Cloud Azure Samples
This is a sample project for demonstration purposes.   
The project demonstrates how to use [Spring Cloud Azure](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html) to develop applications.    
The sample project contains 42 samples, and it still grows.    
These samples are grouped by Azure services and Spring Cloud Azure libraries.    

> For example: [single-namespaces](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespaces), it located in the path `/servicebus/spring-cloud-azure-starter-integration-servicebus`.
>
>  **servicebus**: The Azure service [single-namespaces](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespaces) integrated with.
>
> **spring-cloud-azure-starter-integration-servicebus**: the dependency [single-namespaces](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespaces) depends on.


## Current Branch Supported versions
- [spring-boot-dependencies:2.5.2](https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-dependencies/2.5.2/spring-boot-dependencies-2.5.2.pom).
- [spring-cloud-dependencies:2020.0.3](https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-dependencies/2020.0.3/spring-cloud-dependencies-2020.0.3.pom).

## All active branches

| Spring Boot Version | Spring Cloud version | Spring Cloud Azure Version | 
| ---                 | ---                  | ---                       | 
| 2.5.2               | 2020.0.3             | [4.0](https://github.com/Azure/azure-sdk-for-java/tree/feature/azure-spring-cloud-4.0/sdk/spring)                     | 

## All samples in this repo

| Azure Service    | Spring Cloud Azure Starter Dependency                                                          | Sample Project                                                                                                                    |
|------------------|------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| AAD              | [spring-cloud-azure-starter-active-directory-b2c:4.0.0-beta.2]                                 | [aad-b2c-resource-server](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-resource-server)                             |
| AAD              | [spring-cloud-azure-starter-active-directory-b2c:4.0.0-beta.2]                                 | [aad-b2c-web-application](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-web-application)                             |
| AAD              | [spring-cloud-azure-starter-active-directory:4.0.0-beta.2]                                     | [aad-resource-server-by-filter-stateless](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter-stateless) |
| AAD              | [spring-cloud-azure-starter-active-directory:4.0.0-beta.2]                                     | [aad-resource-server-by-filter](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter)                     |
| AAD              | [spring-cloud-azure-starter-active-directory:4.0.0-beta.2]                                     | [aad-resource-server-obo](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-obo)                                 |
| AAD              | [spring-cloud-azure-starter-active-directory:4.0.0-beta.2]                                     | [aad-resource-server](aad/spring-cloud-azure-starter-active-directory/aad-resource-server)                                         |
| AAD              | [spring-cloud-azure-starter-active-directory:4.0.0-beta.2]                                     | [aad-web-application](aad/spring-cloud-azure-starter-active-directory/aad-web-application)                                         |
| AAD              | [spring-cloud-azure-starter-active-directory:4.0.0-beta.2]                                     | [aad-webapp-resource-server](aad/spring-cloud-azure-starter-active-directory/aad-web-application-and-resource-server)              |
| App Configuration| [azure-spring-boot-starter-cosmos:3.9.0]                                                       | [azure-appconfiguration-conversion-sample-initial](appconfiguration/azure-appconfiguration-conversion-sample-initial)             |
| App Configuration| [spring-cloud-azure-appconfiguration-config:1.3.0]                                             | [azure-appconfiguration-sample](appconfiguration/azure-appconfiguration-sample)                                                   |
| App Configuration| [spring-cloud-azure-feature-management:1.3.0]                                                  | [feature-management-sample](appconfiguration/feature-management-sample)                                                           |
| App Configuration| [spring-cloud-azure-feature-management:1.3.0]                                                  | [feature-management-web-sample](appconfiguration/feature-management-web-sample)                                                   |
| App Configuration| [spring-cloud-starter-azure-appconfiguration-config:1.3.0]                                     | [azure-appconfiguration-conversion-sample-complete](appconfiguration/azure-appconfiguration-conversion-sample-complete)           |
| Cache            | N/A                                                                                            | [azure-spring-cloud-sample-cache](cache/spring-cloud-azure-starter/spring-cloud-azure-sample-cache)                                                          |
| Cloud Foundry    | N/A                                                                                            | [azure-cloud-foundry-service-sample](cloudfoundry/azure-cloud-foundry-service-sample)                                                          |
| Cosmos DB        | [azure-spring-data-cosmos:3.13.1]                                                              | [cosmos-multi-database-multi-account](cosmos/azure-spring-data-cosmos/cosmos-multi-database-multi-account)                |
| Cosmos DB        | [azure-spring-data-cosmos:3.13.1]                                                              | [cosmos-multi-database-single-account](cosmos/azure-spring-data-cosmos/cosmos-multi-database-single-account)              |
| Cosmos DB        | [spring-cloud-azure-starter-data-cosmos:4.0.0-beta.2]                                          | [spring-cloud-azure-data-cosmos-sample](cosmos/spring-cloud-azure-starter-data-cosmos/spring-cloud-azure-data-cosmos-sample)                                                                          |
| Cosmos DB        | [spring-cloud-azure-starter-cosmos:4.0.0-beta.2]                                               | [spring-cloud-azure-cosmos-sample](cosmos/spring-cloud-azure-starter-cosmos/spring-cloud-azure-cosmos-sample)                                                                          |
| Event Hubs       | N/A                                                                                            | [spring-cloud-azure-sample-eventhubs-kafka](eventhubs/spring-cloud-azure-starter/spring-cloud-azure-sample-eventhubs-kafka)                                           |
| Event Hubs       | [spring-cloud-azure-starter-integration-eventhubs:4.0.0-beta.2]                                                   | [eventhubs-integration](eventhubs/spring-cloud-azure-starter-integration-eventhubs/eventhubs-integration)                                     |
| Event Hubs       | [spring-cloud-azure-stream-binder-eventhubs:4.0.0-beta.2]                                             | [eventhubs-binder](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-binder)                                         |
| Event Hubs       | [spring-cloud-azure-stream-binder-eventhubs:4.0.0-beta.2]                                             | [eventhubs-multibinders](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-multibinders)                             |
| Key Vault        | [azure-spring-boot-starter-keyvault-certificates:3.10.0]                                       | [keyvault-certificates-client-side](keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-client-side)   |
| Key Vault        | [azure-spring-boot-starter-keyvault-certificates:3.10.0]                                       | [keyvault-certificates-server-side](keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-server-side)   |
| Key Vault        | [azure-security-keyvault-jca:2.2.0]                                                            | [run-with-command-line-server-side](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-server-side)   |
| Key Vault        | [azure-security-keyvault-jca:2.2.0]                                                            | [run-with-command-line-client-side](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-client-side)   |
| Key Vault        | [spring-cloud-azure-starter-keyvault-secrets:4.0.0-beta.2]                                     | [keyvault-secrets](keyvault/spring-cloud-azure-starter-keyvault-secrets/keyvault-secrets)                                          |
| Service Bus      | [spring-cloud-azure-starter-servicebus-jms:4.0.0-beta.2]                                       | [servicebus-jms-queue](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-queue)                                  |
| Service Bus      | [spring-cloud-azure-starter-servicebus-jms:4.0.0-beta.2]                                       | [servicebus-jms-topic](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-topic)                                  |
| Service Bus      | [spring-cloud-azure-starter-integration-servicebus:4.0.0-beta.2]                               | [single-namespaces](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespaces)                                 |
| Service Bus      | [spring-cloud-azure-starter-integration-servicebus:4.0.0-beta.2]                               | [multiple-namespaces](servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces)                                 |
| Service Bus      | [spring-cloud-azure-stream-binder-servicebus:4.0.0-beta.2]                                     | [servicebus-queue-binder](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder)                   |
| Service Bus      | [spring-cloud-azure-stream-binder-servicebus:4.0.0-beta.2]                                     | [servicebus-queue-multibinders](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-multibinders)       |
| Service Bus      | [spring-cloud-azure-stream-binder-servicebus:4.0.0-beta.2]                                     | [servicebus-topic-binder](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-topic-binder)                   |
| Storage          | [spring-cloud-azure-starter-storage-blob:4.0.0-beta.2]                                         | [storage-blob-sample](storage/spring-cloud-azure-starter-storage-blob/storage-blob-sample)     
| Storage          | [spring-cloud-azure-starter-storage-file-share:4.0.0-beta.2]                                   | [storage-file-sample](storage/spring-cloud-azure-starter-storage-file-share/storage-file-sample)     |
| Storage          | [spring-cloud-azure-starter-integration-storage-queue:4.0.0-beta.2]                            | [storage-queue-integration](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-integration)                           |
| Storage          | [spring-cloud-azure-starter-integration-storage-queue:4.0.0-beta.2]                            | [storage-queue-operation](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-operation)                               |

## Running Samples
With [terraform](https://www.terraform.io/) scripts and [DefaultAzureCredential](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#defaultazurecredential), most samples in the project can be run with the same 4 steps below:

```shell
# Step1 Initialize Terraform
terraform -chdir=./terraform init

# Step2 Apply your Terraform Configuration
terraform -chdir=./terraform apply -auto-approve

# Step3 Export Environment Valuables
source ./terraform/setup_env.sh

# Step4 Run With Maven
mvn clean spring-boot:run
```
It supports both Bash environment and [PowerShell](https://docs.microsoft.com/en-us/powershell/) environment.   
Please refer to [README.md](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace/README.md) under each sample for detailed information.


###
[azure-spring-boot-starter-cosmos:3.9.0]: https://search.maven.org/artifact/com.azure.spring/azure-spring-boot-starter-cosmos/3.9.0/jar
[spring-cloud-azure-feature-management:1.3.0]: https://search.maven.org/artifact/com.microsoft.azure/spring-cloud-azure-feature-management/1.3.0/jar
[spring-cloud-azure-appconfiguration-config:1.3.0]: https://search.maven.org/artifact/com.microsoft.azure/spring-cloud-azure-appconfiguration-config/1.3.0/jar
[spring-cloud-starter-azure-appconfiguration-config:1.3.0]: https://search.maven.org/artifact/com.microsoft.azure/spring-cloud-starter-azure-appconfiguration-config/1.3.0/jar
[spring-cloud-azure-starter-keyvault-secrets:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-keyvault-secrets/4.0.0-beta.2/jar
[azure-spring-boot-starter-keyvault-certificates:3.10.0]: https://search.maven.org/artifact/com.azure.spring/azure-spring-boot-starter-keyvault-certificates/3.10.0/jar
[spring-cloud-azure-stream-binder-eventhubs:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-stream-binder-eventhubs/4.0.0-beta.2/jar
[spring-cloud-azure-starter-integration-eventhubs:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-integration-eventhubs/4.0.0-beta.2/jar
[spring-cloud-azure-stream-binder-servicebus:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-stream-binder-servicebus/4.0.0-beta.2/jar
[spring-cloud-azure-starter-active-directory:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-active-directory/4.0.0-beta.2/jar
[spring-cloud-azure-starter-active-directory-b2c:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-active-directory-b2c/4.0.0-beta.2/jar
[azure-spring-data-cosmos:3.13.1]: https://search.maven.org/artifact/com.azure/azure-spring-data-cosmos/3.13.1/jar
[spring-cloud-azure-starter-data-cosmos:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-data-cosmos/4.0.0-beta.2/jar
[spring-cloud-azure-starter-cosmos:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-cosmos/4.0.0-beta.2/jar
[spring-cloud-azure-starter-servicebus-jms:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-servicebus-jms/4.0.0-beta.2/jar
[spring-cloud-azure-starter-integration-servicebus:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-integration-servicebus/4.0.0-beta.2/jar
[azure-security-keyvault-jca:2.1.0]: https://mvnrepository.com/artifact/com.azure/azure-security-keyvault-jca
[spring-cloud-azure-starter-integration-storage-queue:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-integration-storage-queue/4.0.0-beta.2/jar
[spring-cloud-azure-starter-storage-file-share:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-storage-file-share/4.0.0-beta.2/jar
[spring-cloud-azure-starter-storage-blob:4.0.0-beta.2]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-storage-blob/4.0.0-beta.2/jar
[azure-security-keyvault-jca:2.2.0]: https://search.maven.org/artifact/com.azure/azure-security-keyvault-jca/2.2.0/jar
