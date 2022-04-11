[![CodeQL](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml) [![CI](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/java_ci_with_maven_adopt.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/java_ci_with_maven_adopt.yml) [![Markdown Links Check](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml) 

# Spring Cloud Azure Samples
- This is a sample project for demonstration purposes.   
- The project demonstrates how to use [Spring Cloud Azure](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html) to develop applications.    
- The sample project contains 42 samples, and it still grows.    
- These samples are grouped by Azure services and [Spring Cloud Azure libraries](https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring).    

    > For example: [Use Spring Integration with single Azure Service Bus namespace](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace), it located in the path `/servicebus/spring-cloud-azure-starter-integration-servicebus`.
    >
    >  **servicebus**: The Azure service that the sample integrated with.
    >
    > **spring-cloud-azure-starter-integration-servicebus**: The dependency that the sample depends on.

## Branch Policy
We use `main` branch as the develop branch while setting the default branch as the latest released branch.

| Branch Name               | Default Branch | Branch type    |
|---------------------------|----------------|----------------|
| main                      | false          | develop branch |
| spring-cloud-azure_v4.0.0 | true           | release branch |


## Current Branch Supported Versions
- [spring-boot-dependencies:2.6.3](https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-dependencies/2.6.3/spring-boot-dependencies-2.6.3.pom).
- [spring-cloud-dependencies:2021.0.1](https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-dependencies/2021.0.1/spring-cloud-dependencies-2021.0.1.pom).

## All Active Branches

| Spring Boot Version  | Spring Cloud version  | Spring Cloud Azure Version                                                                 |
|----------------------|-----------------------|--------------------------------------------------------------------------------------------|
| 2.6.3                | 2021.0.1              | [4.0.0](https://github.com/Azure/azure-sdk-for-java/releases/tag/spring-cloud-azure_4.0.0) | 

## All Samples in This Repo

| Azure Service    | Spring Cloud Azure Starter Dependency                               | Sample Project                                                                                                                     |
|------------------|---------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| AAD              | [spring-cloud-azure-starter-active-directory-b2c:4.0.0]      | [aad-b2c-resource-server](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-resource-server)                             |
| AAD              | [spring-cloud-azure-starter-active-directory-b2c:4.0.0]      | [aad-b2c-web-application](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-web-application)                             |
| AAD              | [spring-cloud-azure-starter-active-directory:4.0.0]          | [aad-resource-server-by-filter-stateless](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter-stateless) |
| AAD              | [spring-cloud-azure-starter-active-directory:4.0.0]          | [aad-resource-server-by-filter](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter)                     |
| AAD              | [spring-cloud-azure-starter-active-directory:4.0.0]          | [web-client-access-resource-server](aad/spring-cloud-azure-starter-active-directory/web-client-access-resource-server)             |
| AAD              | [spring-cloud-azure-starter-active-directory:4.0.0]          | [aad-webapp-resource-server](aad/spring-cloud-azure-starter-active-directory/aad-web-application-and-resource-server)              |
| App Configuration| [spring-cloud-azure-starter-data-cosmos:4.0.0]               | [azure-appconfiguration-conversion-sample-initial](appconfiguration/azure-appconfiguration-conversion-sample-initial)              |
| App Configuration| [spring-cloud-azure-starter-data-cosmos:4.0.0]               | [azure-appconfiguration-conversion-sample-initial](appconfiguration/azure-appconfiguration-conversion-sample-initial)              |
| App Configuration| [azure-spring-cloud-appconfiguration-config:2.3.0]                  | [azure-appconfiguration-sample](appconfiguration/azure-appconfiguration-sample)                                                    |
| App Configuration| [azure-spring-cloud-feature-management:2.2.0]                       | [feature-management-sample](appconfiguration/feature-management-sample)                                                            |
| App Configuration| [azure-spring-cloud-feature-management:2.2.0]                       | [feature-management-web-sample](appconfiguration/feature-management-web-sample)                                                    |
| App Configuration| [azure-spring-cloud-appconfiguration-config:2.3.0]                  | [azure-appconfiguration-conversion-sample-complete](appconfiguration/azure-appconfiguration-conversion-sample-complete)            |
| Cache            | N/A                                                                 | [azure-spring-cloud-sample-cache](cache/spring-cloud-azure-starter/spring-cloud-azure-sample-cache)                                |
| Cloud Foundry    | N/A                                                                 | [azure-cloud-foundry-service-sample](cloudfoundry/azure-cloud-foundry-service-sample)                                              |
| Cosmos DB        | [azure-spring-data-cosmos:3.19.0]                                   | [cosmos-multi-database-multi-account](cosmos/azure-spring-data-cosmos/cosmos-multi-database-multi-account)                         |
| Cosmos DB        | [azure-spring-data-cosmos:3.19.0]                                   | [cosmos-multi-database-single-account](cosmos/azure-spring-data-cosmos/cosmos-multi-database-single-account)                       |
| Cosmos DB        | [spring-cloud-azure-starter-data-cosmos:4.0.0]               | [spring-cloud-azure-data-cosmos-sample](cosmos/spring-cloud-azure-starter-data-cosmos/spring-cloud-azure-data-cosmos-sample)       |
| Cosmos DB        | [spring-cloud-azure-starter-cosmos:4.0.0]                    | [spring-cloud-azure-cosmos-sample](cosmos/spring-cloud-azure-starter-cosmos/spring-cloud-azure-cosmos-sample)                      |
| Event Hubs       | N/A                                                                 | [spring-cloud-azure-sample-eventhubs-kafka](eventhubs/spring-cloud-azure-starter/spring-cloud-azure-sample-eventhubs-kafka)        |
| Event Hubs       | [spring-cloud-azure-starter-integration-eventhubs:4.0.0]     | [eventhubs-integration](eventhubs/spring-cloud-azure-starter-integration-eventhubs/eventhubs-integration)                          |
| Event Hubs       | [spring-cloud-azure-stream-binder-eventhubs:4.0.0]           | [eventhubs-binder](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-binder)                                          |
| Event Hubs       | [spring-cloud-azure-stream-binder-eventhubs:4.0.0]           | [eventhubs-multibinders](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-multibinders)                              |
| Key Vault        | [azure-spring-boot-starter-keyvault-certificates:3.14.0]            | [keyvault-certificates-client-side](keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-client-side)    |
| Key Vault        | [azure-spring-boot-starter-keyvault-certificates:3.14.0]            | [keyvault-certificates-server-side](keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-server-side)    |
| Key Vault        |                                                                     | [run-with-command-line-server-side](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-server-side)                        |
| Key Vault        |                                                                     | [run-with-command-line-client-side](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-client-side)                        |
| Key Vault        | [spring-cloud-azure-starter-keyvault-secrets:4.0.0]          | [property-source](keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source)                                            |
| Key Vault        | [spring-cloud-azure-starter-keyvault-secrets:4.0.0]          | [secret-client](keyvault/spring-cloud-azure-starter-keyvault-secrets/secret-client)                                                |
| Service Bus      | [spring-cloud-azure-starter-servicebus-jms:4.0.0]            | [servicebus-jms-queue](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-queue)                                  |
| Service Bus      | [spring-cloud-azure-starter-servicebus-jms:4.0.0]            | [servicebus-jms-topic](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-topic)                                  |
| Service Bus      | [spring-cloud-azure-starter-integration-servicebus:4.0.0]    | [single-namespace](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace)                                  |
| Service Bus      | [spring-cloud-azure-starter-integration-servicebus:4.0.0]    | [multiple-namespaces](servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces)                            |
| Service Bus      | [spring-cloud-azure-stream-binder-servicebus:4.0.0]          | [servicebus-queue-binder](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder)                          |
| Service Bus      | [spring-cloud-azure-stream-binder-servicebus:4.0.0]          | [servicebus-queue-multibinders](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-multibinders)                    |
| Service Bus      | [spring-cloud-azure-stream-binder-servicebus:4.0.0]          | [servicebus-topic-binder](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-topic-binder)                          |
| Service Bus      | [spring-cloud-azure-stream-binder-servicebus:4.0.0]          | [servicebus-queue-binder-arm](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder-arm)                  |
| Storage          | [spring-cloud-azure-starter-storage-blob:4.0.0]              | [storage-blob-sample](storage/spring-cloud-azure-starter-storage-blob/storage-blob-sample)                                         
| Storage          | [spring-cloud-azure-starter-storage-file-share:4.0.0]        | [storage-file-sample](storage/spring-cloud-azure-starter-storage-file-share/storage-file-sample)                                   |
| Storage          | [spring-cloud-azure-starter-integration-storage-queue:4.0.0] | [storage-queue-integration](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-integration)                |
| Storage          | [spring-cloud-azure-starter-integration-storage-queue:4.0.0] | [storage-queue-operation](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-operation)                    |

## Running Samples With Terraform
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
[spring-cloud-azure-starter-keyvault-secrets:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-keyvault-secrets/4.0.0/jar
[spring-cloud-azure-stream-binder-eventhubs:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-stream-binder-eventhubs/4.0.0/jar
[spring-cloud-azure-starter-integration-eventhubs:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-integration-eventhubs/4.0.0/jar
[spring-cloud-azure-stream-binder-servicebus:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-stream-binder-servicebus/4.0.0/jar
[spring-cloud-azure-starter-active-directory:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-active-directory/4.0.0/jar
[spring-cloud-azure-starter-active-directory-b2c:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-active-directory-b2c/4.0.0/jar
[spring-cloud-azure-starter-data-cosmos:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-data-cosmos/4.0.0/jar
[spring-cloud-azure-starter-cosmos:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-cosmos/4.0.0/jar
[spring-cloud-azure-starter-servicebus-jms:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-servicebus-jms/4.0.0/jar
[spring-cloud-azure-starter-integration-servicebus:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-integration-servicebus/4.0.0/jar
[spring-cloud-azure-starter-integration-storage-queue:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-integration-storage-queue/4.0.0/jar
[spring-cloud-azure-starter-storage-file-share:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-storage-file-share/4.0.0/jar
[spring-cloud-azure-starter-storage-blob:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-storage-blob/4.0.0/jar
[spring-cloud-azure-starter-data-cosmos:4.0.0]: https://search.maven.org/artifact/com.azure.spring/spring-cloud-azure-starter-data-cosmos/4.0.0/jar
[azure-spring-cloud-feature-management:2.2.0]: https://search.maven.org/artifact/com.azure.spring/azure-spring-cloud-feature-management/2.2.0/jar
[azure-spring-cloud-appconfiguration-config:2.3.0]: https://search.maven.org/artifact/com.azure.spring/azure-spring-cloud-appconfiguration-config/2.3.0/jar
[azure-spring-boot-starter-keyvault-certificates:3.14.0]: https://search.maven.org/artifact/com.azure.spring/azure-spring-boot-starter-keyvault-certificates/3.14.0/jar
[azure-spring-data-cosmos:3.19.0]: https://search.maven.org/artifact/com.azure/azure-spring-data-cosmos/3.19.0/jar
