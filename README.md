[![CodeQL](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml) [![CI](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml) [![Markdown Links Check](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml) 

# Spring Cloud Azure Samples  
- The repository hold samples about using [Spring Cloud Azure](https://learn.microsoft.com/azure/developer/java/spring-framework/) libraries. 
- The **main** branch is using the latest stable version of Spring Cloud Azure. If you want to find sample about specific version of Spring Cloud Azure, please switch to corresponding tag in this repository.

## All Samples in This Repo

### Microsoft Entra ID

| Sample Project                                                                                                                     | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|--------------------------------|
| [aad-b2c-resource-server](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-resource-server)                             | ✅                              | ❌                              | 
| [aad-b2c-web-application](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-web-application)                             | ✅                              | ❌                              | 
| [aad-resource-server-by-filter-stateless](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter-stateless) | ✅                              | ❌                              | 
| [aad-resource-server-by-filter](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter)                     | ✅                              | ❌                              | 
| [web-client-access-resource-server](aad/spring-cloud-azure-starter-active-directory/web-client-access-resource-server)             | ✅                              | ❌                              | 
| [aad-webapp-resource-server](aad/spring-cloud-azure-starter-active-directory/aad-web-application-and-resource-server)              | ✅                              | ❌                              |

### Azure App Configuration

| Sample Project                                                                                                                                                                                                                                                 | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|--------------------------------|
| [spring-cloud-azure-appconfiguration-config-convert-sample-complete](appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-convert-sample/spring-cloud-azure-appconfiguration-config-convert-sample-complete) | ✅                              | ✅                              |
| [spring-cloud-azure-appconfiguration-config-sample](appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-sample)                                                                                             | ✅                              | ✅                              |
| [spring-cloud-azure-appconfiguration-config-convert-sample-initial](appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-convert-sample/spring-cloud-azure-appconfiguration-config-convert-sample-initial)   | ✅                              | ✅                              |
| [spring-cloud-azure-feature-management-sample](appconfiguration/spring-cloud-azure-feature-management/spring-cloud-azure-feature-management-sample)                                                                                                            | ✅                              | ✅                              |
| [spring-cloud-azure-feature-management-web-sample](appconfiguration/spring-cloud-azure-feature-management-web)                                                                                                | ✅                              | ✅                              |
| [spring-cloud-azure-starter-appconfiguration-config-sample](appconfiguration/spring-cloud-azure-starter-appconfiguration-config/spring-cloud-azure-starter-appconfiguration-config-sample)                                                                     | ✅                              | ✅                              |
| [spring-cloud-azure-starter-appconfiguration-config-entraid-sample](appconfiguration/spring-cloud-azure-starter-appconfiguration-config/spring-cloud-azure-starter-appconfiguration-config-entraid-sample)                                                                     | ✅                              | ✅                              |
| [appconfiguration-client](appconfiguration/spring-cloud-azure-starter-appconfiguration/appconfiguration-client)                                                                                                                                                | ✅                              | ✅                              |

### Azure Cache

| Sample Project                             | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|--------------------------------------------|--------------------------------|--------------------------------|
| [azure-spring-cloud-sample-cache](cache)   | ✅                              | ✅                              |

### Azure Cosmos DB

| Sample Project                                                                                                               | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|------------------------------------------------------------------------------------------------------------------------------|--------------------------------|--------------------------------|
| [azure-spring-data-cosmos-sample](cosmos/azure-spring-data-cosmos)                                                           | ✅                              | ✅                              |
| [spring-cloud-azure-data-cosmos-sample](cosmos/spring-cloud-azure-starter-data-cosmos/spring-cloud-azure-data-cosmos-sample) | ✅                              | ✅                              | 
| [spring-cloud-azure-cosmos-sample](cosmos/spring-cloud-azure-starter-cosmos/spring-cloud-azure-cosmos-sample)                | ✅                              | ✅                              |

### Azure Event Hubs

| Sample Project                                                                                                              | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|-----------------------------------------------------------------------------------------------------------------------------|--------------------------------|--------------------------------|
| [spring-cloud-azure-sample-eventhubs-kafka](eventhubs/spring-cloud-azure-starter/spring-cloud-azure-sample-eventhubs-kafka) | ✅                              | ✅                              | 
| [eventhubs-integration](eventhubs/spring-cloud-azure-starter-integration-eventhubs/eventhubs-integration)                   | ✅                              | ✅                              | 
| [eventhubs-binder](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-binder)                                   | ✅                              | ✅                              | 
| [eventhubs-multibinders](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-multibinders)                       | ✅                              | ✅                              | 
| [eventhubs-client](eventhubs/spring-cloud-azure-starter-eventhubs/eventhubs-client)                                         | ✅                              | ✅                              |

### Azure Key Vault

| Sample Project                                                                                                                  | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|---------------------------------------------------------------------------------------------------------------------------------|--------------------------------|--------------------------------|
| [keyvault-certificates-client-side](keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-client-side) | ✅                              | ❌                              | 
| [keyvault-certificates-server-side](keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-server-side) | ✅                              | ❌                              | 
| [run-with-command-line-server-side](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-server-side)                     | ✅                              | ❌                              | 
| [run-with-command-line-client-side](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-client-side)                     | ✅                              | ❌                              | 
| [property-source](keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source)                                         | ✅                              | ✅                              | 
| [secret-client](keyvault/spring-cloud-azure-starter-keyvault-secrets/secret-client)                                             | ✅                              | ✅                              |

### Azure Database for MySQL

| Sample Project                                                                                                        | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|-----------------------------------------------------------------------------------------------------------------------|--------------------------------|--------------------------------|
| [spring-cloud-azure-mysql-sample](mysql/spring-cloud-azure-starter-jdbc-mysql/spring-cloud-azure-mysql-sample) | ✅                             | ✅                              | 

### Azure Database for PostgreSQL

| Sample Project                                                                                                                       | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|--------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|--------------------------------|
| [spring-cloud-azure-postgresql-sample](postgresql/spring-cloud-azure-starter-jdbc-postgresql/spring-cloud-azure-postgresql-sample) | ✅                             | ✅                              |

### Azure Service Bus

| Sample Project                                                                                | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|-----------------------------------------------------------------------------------------------|--------------------------------|--------------------------------|
| [servicebus-queue](servicebus/spring-cloud-azure-starter-servicebus/servicebus-queue)         | ✅                              | ✅                              | 
| [servicebus-topic](servicebus/spring-cloud-azure-starter-servicebus/servicebus-topic)         | ✅                              | ✅                              | 
| [servicebus-jms-queue](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-queue) | ✅                              | ✅                              | 
| [servicebus-jms-topic](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-topic) | ✅                              | ✅                              | 
| [single-namespace](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace) | ✅                              | ✅                              | 
| [multiple-namespaces](servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces) | ✅                              | ✅                              | 
| [servicebus-queue-binder](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder) | ✅                              | ✅                              | 
| [servicebus-queue-multibinders](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-multibinders) | ✅                              | ✅                              | 
| [servicebus-topic-binder](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-topic-binder) | ✅                              | ✅                              | 
| [servicebus-queue-binder-arm](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder-arm) | ✅                              | ✅                              |

### Azure Storage

| Sample Project                                                                                                      | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|---------------------------------------------------------------------------------------------------------------------|--------------------------------|--------------------------------|
| [storage-blob-sample](storage/spring-cloud-azure-starter-storage-blob/storage-blob-sample)                          | ✅                              | ✅                              | 
| [storage-file-sample](storage/spring-cloud-azure-starter-storage-file-share/storage-file-sample)                    | ✅                              | ✅                              | 
| [storage-queue-client](storage/spring-cloud-azure-starter-storage-queue/storage-queue-client)                       | ✅                              | ✅                              | 
| [storage-queue-integration](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-integration) | ✅                              | ✅                              | 
| [storage-queue-operation](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-operation)     | ✅                              | ✅                              |


### Spring PetClinic

| Sample Project                                                                                                      | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|---------------------------------------------------------------------------------------------------------------------|--------------------------------|--------------------------------|
| [spring-petclinic-microservices](spring-petclinic-microservices)                                                    | ✅                              | ❌                              | 

### TestContainers Support

| Sample Project                                                   | Support Spring Cloud Azure 4.x | Support Spring Cloud Azure 5.x | 
|------------------------------------------------------------------|--------------------------------|--------------------------------|
| [testContainers for Cosmos](testcontainers/cosmos)               | ❌                              | ✅                              |
| [testContainers for Storage Blob](testcontainers/storage-blob)   | ❌                              | ✅                              |
| [testContainers for Storage Queue](testcontainers/storage-queue) | ❌                              | ✅                              |

## Getting Help
- If you have any question about using these samples, please [create an new issue](https://github.com/Azure-Samples/azure-spring-boot-samples/issues/new/choose).

## Introduction to Maven profiles 

This project uses one pom file to manage the samples for Spring Cloud Azure 4.x and Spring Cloud Azure 5.x, the profile `spring-cloud-azure-4.x` is activated by default.

The profile names ending with `-4.x` are used to run the samples with Spring Cloud Azure 4.x and Spring Boot 2;
the profile names ending with `-5.x` are used to run the samples with Spring Cloud Azure 5.x and Spring Boot 3. The profile names ending with `4.x` and `5.x` cannot be mixed at the same time.

**Note**: 
- Profiles `native-5.x` and `nativeTest-5.x` are variants of the two profiles `native` and `nativeTest` provided by `spring-boot-starter-parent` of Spring Boot 3, and they have the same function. If you want to use this profile, please use it with the profile `spring-cloud-azure-5.x`, such as `mvn -P spring-cloud-azure-5.x,native-5.x native:compile`.


## Samples for Spring Boot 3

**Note**: This section is used to convert the samples to run in Spring Boot 3, the branch `feature/spring-boot-3` is no longer maintained.

### Convert samples to use Spring Boot 3

To get samples for Spring Boot 3, please refer to the content in each sample's *README.md* like this:
> Current sample is using Spring Cloud Azure 4.x (which is compatible with Spring Boot 2.x).
> If you want sample about Spring Cloud Azure 5.x (which is compatible with Spring Boot 3.x),
> please refer to [CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3.md](./CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3_TEMPLATE.md).

Mostly samples can work with both Spring Cloud Azure 4.x and Spring Cloud Azure 5.x, you can directly activate the profile of Spring Cloud Azure 5.x to run.

If you're adding a new example, here's a template that converts the example to support Spring Boot 3: [./CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3_TEMPLATE.md](CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3_TEMPLATE.md).

### Run samples with Maven command

Use below command to enable the profile for Spring Cloud Azure 5.x.

```shell
mvn clean spring-boot:run -P spring-cloud-azure-5.x
```

### Run samples with IDE

Activate the profile Spring Cloud Azure 5.x by default, then you can run in your IDE tool.

Remove the below activation for `spring-cloud-azure-4.x`, and add it to profile `spring-cloud-azure-5.x`:

```xml
<activation>
  <activeByDefault>true</activeByDefault>
</activation>
```

