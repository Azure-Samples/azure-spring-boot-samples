[![CodeQL](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml) [![CI](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml) [![Markdown Links Check](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml) 

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

| Branch Name                      | Default Branch | Branch type    |
|----------------------------------|----------------|----------------|
| main                             | false          | develop branch |
| spring-cloud-azure_v4.5.0-beta.1 | true           | release branch |

## Current Branch Supported Versions
- [spring-boot-dependencies:2.7.4](https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-dependencies/2.7.4/spring-boot-dependencies-2.7.4.pom).
- [spring-cloud-dependencies:2021.0.4](https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-dependencies/2021.0.4/spring-cloud-dependencies-2021.0.4.pom).

## All Samples in This Repo


### Azure Active Directory (Azure AD)

| Name                                                                                                                             | Description                                                                                                                                                                             | Location                                                                                                                           |
|----------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| 1. Single Sign-On (SSO) by Azure AD And Access Azure AD Protected Resource Server                                                | This sample demonstrates how to develop a Spring Boot web application supports single sign-on (SSO) by Azure AD account and access Azure AD protected resource-server at the same time. | [web-client-access-resource-server](aad/spring-cloud-azure-starter-active-directory/web-client-access-resource-server)             |
| 2. Single Sign-On (SSO) by Azure AD and Expose Azure AD Protected Resource Server In One Application                             | This sample demonstrates how to develop a Spring Boot web application supports single sign-on (SSO) by Azure AD account and expose resource-server at the same time.                    | [aad-web-application-and-resource-server](aad/spring-cloud-azure-starter-active-directory/aad-web-application-and-resource-server) |
| 3. (Spring Security) Single Sign-On (SSO) by Azure AD Using Client Secret                                                        | This sample demonstrates how to develop a Spring Boot web application that supports single sign-on (SSO) by Azure AD using client secret.                                               | [login](aad/spring-security/servlet/oauth2/login)                                                                                  |
| 4. (Spring Security) Single Sign-On (SSO) by Azure AD Using Client Certificate                                                   | This sample demonstrates how to develop a Spring Boot web application that supports single sign-on (SSO) by Azure AD using client certificate.                                          | [login-authenticate-using-private-key-jwt](aad/spring-security/servlet/oauth2/login-authenticate-using-private-key-jwt)            |
| 5. (Spring Security) Single Sign-On (SSO) by Azure AD And Access Azure AD Protected Resource Server                              | This sample demonstrates how to develop a Spring Boot web application that supports single sign-on (SSO) by Azure AD and access Azure AD protected resource-server.                     | [client-access-resource-server](aad/spring-security/servlet/oauth2/client-access-resource-server)                                  |
| 6. (Spring Security) Single Sign-On (SSO) by Azure AD And Access Multiple Azure AD Resource Server                               | This sample demonstrates how to develop a Spring Boot web application that supports single sign-on (SSO) by Azure AD and access multiple Azure AD protected resource-servers.           | [client-access-multiple-resource-server](aad/spring-security/servlet/oauth2/client-access-multiple-resource-server)                |
| 7. (Spring Security) Azure AD Protected Resource Server Require A New Token To Access Another Azure AD Protected resource-server | This sample demonstrates how to develop a Azure AD protected resource-server that can require a new token to access another Azure AD protected resource-server.                         | [resource-server-support-on-behalf-of-flow](aad/spring-security/servlet/oauth2/resource-server-support-on-behalf-of-flow)          |
| 8. (Spring Security) Protecting Resource Server by Azure AD in Gateway                                                           | This sample demonstrates how to protect resource-server behind gateway by validating token issued by Azure AD in gateway.                                                               | [spring-cloud-gateway](aad/spring-security/reactive/webflux/oauth2/spring-cloud-gateway)                                           |


### Azure Active Directory B2C (Azure AD B2C)

| Name                                      | Description                                                                                                          | Location                                                                                               |
|-------------------------------------------|----------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| 1. Single Sign-On (SSO) by Azure AD B2C   | This sample demonstrates how to develop a Spring Boot web application supports single sign-on (SSO) by Azure AD B2C. | [aad-b2c-web-application](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-web-application) |
| 2. Azure AD B2C Protected Resource Server | This sample demonstrates how to develop Azure AD B2C Protected Resource Server                                       | [aad-b2c-resource-server](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-resource-server) |


### Azure App Configuration

| Name                                                                                               | Description                                                                                                                                                | Location                                                                                                                                                                                   |
|----------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1. Loading Configuration Properties From App Configuration by Autoconfigured SDK Client            | This sample demonstrates how to load configuration properties from App Configuration by autoconfigured SDK client in Spring Boot application.              | [appconfiguration-client](appconfiguration/spring-cloud-azure-starter-appconfiguration/appconfiguration-client)                                                                            |    
| 2. Loading Configuration Properties From App Configuration                                         | This sample demonstrates how to load configuration properties from App Configuration to Spring Environment in Spring Boot application.                     | [azure-spring-cloud-appconfiguration-config-sample](appconfiguration/azure-spring-cloud-appconfiguration-config/azure-spring-cloud-appconfiguration-config-sample)                         |
| 3. Refreshing Configuration Properties From App Configuration                                      | This sample demonstrates how to refresh configuration properties from App Configuration in Spring Boot application.                                        | [azure-spring-cloud-starter-appconfiguration-config-sample](appconfiguration/azure-spring-cloud-starter-appconfiguration-config/azure-spring-cloud-starter-appconfiguration-config-sample) |
| 4. Managing Features and Get Configurations From App Configuration                                 | This sample demonstrates how to manage features and how to get configurations from App Configuration to Spring Environment in Spring Boot application.     | [azure-spring-cloud-feature-management-sample](appconfiguration/azure-spring-cloud-feature-management/azure-spring-cloud-feature-management-sample)                                        |
| 5. Managing Features and Get Configurations From App Configuration In Web Application              | This sample demonstrates how to manage features and how to get configurations from App Configuration to Spring Environment in Spring Boot web application. | [azure-spring-cloud-feature-management-web-sample](appconfiguration/azure-spring-cloud-feature-management-web/azure-spring-cloud-feature-management-web-sample)                            |
| 6. Converting a Spring Boot Application With Cosmos Db to Be Using App Configuration and Key Vault | This sample demonstrates how to convert a Spring Boot application with Cosmos DB to be using App Configuration and Key Vault.                              | [azure-spring-cloud-appconfiguration-config-convert-sample](appconfiguration/azure-spring-cloud-appconfiguration-config/azure-spring-cloud-appconfiguration-config-convert-sample)         |


### Azure Cache for Redis

| Name                                     | Description                                                                                                                                                         | Location                                                                                            |
|------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------|
| 1. Caching Data to Azure Cache for Redis | This sample demonstrates how to cache data to Azure Cache for Redis in Spring Boot application. Some redis properties can be got from Management SDK automatically. | [azure-spring-cloud-sample-cache](cache/spring-cloud-azure-starter/spring-cloud-azure-sample-cache) |


### Azure Cosmos DB

| Name                                      | Description                                                                                                                                                                       | Location                                                                                                                     |
|-------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|
| Using Azure Cosmos DB RBAC by Spring Data | This sample demonstrate basic Spring Data code for Java SQL API to connect to Azure Cosmos DB using built-in role-based access control (RBAC), and authenticating using Azure AD. | [cosmos-aad-sample](cosmos/azure-spring-data-cosmos/cosmos-aad-sample)                                                       |
| Cosmos DB                                 | Using Azure Cosmos DB by Spring Data - Multi database and multi account                                                                                                           | [cosmos-multi-database-multi-account](cosmos/azure-spring-data-cosmos/cosmos-multi-database-multi-account)                   |
| Cosmos DB                                 | Using Azure Cosmos DB by Spring Data - Multi database and single account.                                                                                                         | [cosmos-multi-database-single-account](cosmos/azure-spring-data-cosmos/cosmos-multi-database-single-account)                 |
| Cosmos DB                                 | sing Azure Cosmos DB by Spring Data                                                                                                                                               | [spring-cloud-azure-data-cosmos-sample](cosmos/spring-cloud-azure-starter-data-cosmos/spring-cloud-azure-data-cosmos-sample) |
| Cosmos DB                                 | Accessing Azure Cosmos DB with autoconfigured CosmosClient                                                                                                                        | [spring-cloud-azure-cosmos-sample](cosmos/spring-cloud-azure-starter-cosmos/spring-cloud-azure-cosmos-sample)                |


| Event Hubs                                                                                      | Sending and receiving message by Azure Event Hubs and Spring Cloud Stream Binder Kafka                                                                     | [spring-cloud-azure-sample-eventhubs-kafka](eventhubs/spring-cloud-azure-starter/spring-cloud-azure-sample-eventhubs-kafka)                                                                |
| Event Hubs                                                                                      | Sending and receiving message by Azure Event Hubs and Spring Integration                                                                                   | [eventhubs-integration](eventhubs/spring-cloud-azure-starter-integration-eventhubs/eventhubs-integration)                                                                                  |
| Event Hubs                                                                                      | Sending and receiving message by Azure Event Hubs and Spring Cloud Stream Binder EventHubs                                                                 | [eventhubs-binder](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-binder)                                                                                                  |
| Event Hubs                                                                                      | Sending and receiving message by Azure Event Hubs and Spring Cloud Stream Binder EventHubs (multi binder)                                                  | [eventhubs-multibinders](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-multibinders)                                                                                      |
| Event Hubs                                                                                      | Sending and receiving message by Azure Event Hubs and autoconfigured SDK client                                                                            | [eventhubs-client](eventhubs/spring-cloud-azure-starter-eventhubs/eventhubs-client)                                                                                                        |
| Key Vault                                                                                       | Enabling https connection by certificate stored in Azure Key Vault in Spring Boot application                                                              | [azure-spring-boot-starter-keyvault-certificates](keyvault/azure-spring-boot-starter-keyvault-certificates)                                                                                |
| Key Vault                                                                                       | Enabling https connection by certificate stored in Azure Key Vault in Java application                                                                     | [azure-securtiy-keyvault-jca](keyvault/azure-securtiy-keyvault-jca)                                                                                                                        |
| Key Vault                                                                                       | Loading secrets from Azure Key Vault to PropertySource                                                                                                     | [property-source](keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source)                                                                                                    |
| Key Vault                                                                                       | Managing secrets stored in Azure Key Vault by autoconfigured SDK client                                                                                    | [secret-client](keyvault/spring-cloud-azure-starter-keyvault-secrets/secret-client)                                                                                                        |
| Service Bus                                                                                     | Sending and receiving message by Azure Service Bus (queue) and JMS                                                                                         | [servicebus-jms-queue](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-queue)                                                                                          |
| Service Bus                                                                                     | Sending and receiving message by Azure Service Bus (topic) and JMS                                                                                         | [servicebus-jms-topic](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-topic)                                                                                          |
| Service Bus                                                                                     | Sending and receiving message by Azure Service Bus (single namespaces) and Spring Integration                                                              | [single-namespace](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace)                                                                                          |
| Service Bus                                                                                     | Sending and receiving message by Azure Service Bus (multiple namespaces) and Spring Integration                                                            | [multiple-namespaces](servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces)                                                                                    |
| Service Bus                                                                                     | Sending and receiving message by Azure Service Bus (queue) and Spring Cloud Stream Binder                                                                  | [servicebus-queue-binder](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder)                                                                                  |
| Service Bus                                                                                     | Sending and receiving message by Azure Service Bus and Spring Cloud Stream Binder (multi binder)                                                           | [servicebus-queue-multibinders](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-multibinders)                                                                            |
| Service Bus                                                                                     | Sending and receiving message by Azure Service Bus (topic) and Spring Cloud Stream Binder                                                                  | [servicebus-topic-binder](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-topic-binder)                                                                                  |
| Service Bus                                                                                     | Sending and receiving message by Azure Service Bus (queue) (retrieving connection string via ARM) and Spring Cloud Stream Binder                           | [servicebus-queue-binder-arm](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder-arm)                                                                          |
| Storage                                                                                         | Reading and writing files stored in Azure Storage Blob by Spring Resource abstraction                                                                      | [storage-blob-sample](storage/spring-cloud-azure-starter-storage-blob/storage-blob-sample)                                                                                                 |
| Storage                                                                                         | Reading and writing files stored in Azure Files by Spring Resource abstraction                                                                             | [storage-file-sample](storage/spring-cloud-azure-starter-storage-file-share/storage-file-sample)                                                                                           |
| Storage                                                                                         | Sending and receiving message by Azure Storage Queue and SDK client                                                                                        | [storage-queue-client](storage/spring-cloud-azure-starter-storage-queue/storage-queue-client)                                                                                              |
| Storage                                                                                         | Sending and receiving message by Azure Storage Queue and Spring Integration                                                                                | [storage-queue-integration](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-integration)                                                                        |
| Storage                                                                                         | Sending and receiving message by Azure Storage Queue and StorageQueueTemplate                                                                              | [storage-queue-operation](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-operation)                                                                            |
| Spring Native                                                                                   | Spring Native application about Storage Blob                                                                                                               | [storage-blob-native](spring-native/storage-blob-native)                                                                                                                                   |

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

### Run Samples Based On Spring Native
Two Maven profiles have been defined in this project to support compiling Spring applications to native executables: `buildpack` and `native`. The `buildpack` profile will use [Buildpacks](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-buildpacks) and the `native` profile will use [Native Build Tools](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-native-build-tools). Please follow the [storage-blob-native sample](spring-native/storage-blob-native) for more details.
