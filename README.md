[![CodeQL](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml) [![CI](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml) [![Markdown Links Check](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml) 

# Spring Cloud Azure Samples  
- The repository hold samples about using [Spring Cloud Azure](https://learn.microsoft.com/azure/developer/java/spring-framework/) libraries. 
- The **main** branch is using the latest stable version of Spring Cloud Azure. If you want to find sample about specific version of Spring Cloud Azure, please switch to corresponding tag in this repository.

## All Samples in This Repo

| Azure Service     | Sample Project                                                                                                                                                                                                                                                 | Support Spring Cloud Azure 4.x     | Support Spring Cloud Azure 6.x | 
|-------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------|--------------------------------|
| AAD               | [aad-b2c-resource-server](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-resource-server)                                                                                                                                                         | Yes                                | No                             | 
| AAD               | [aad-b2c-web-application](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-web-application)                                                                                                                                                         | Yes                                | No                             | 
| AAD               | [aad-resource-server-by-filter-stateless](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter-stateless)                                                                                                                             | Yes                                | No                             | 
| AAD               | [aad-resource-server-by-filter](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter)                                                                                                                                                 | Yes                                | No                             | 
| AAD               | [web-client-access-resource-server](aad/spring-cloud-azure-starter-active-directory/web-client-access-resource-server)                                                                                                                                         | Yes                                | No                             | 
| AAD               | [aad-webapp-resource-server](aad/spring-cloud-azure-starter-active-directory/aad-web-application-and-resource-server)                                                                                                                                          | Yes                                | No                             | 
| App Configuration | [azure-spring-cloud-appconfiguration-config-sample](appconfiguration/azure-spring-cloud-appconfiguration-config/azure-spring-cloud-appconfiguration-config-sample)                                                                                             | Yes                                | No                             | 
| App Configuration | [azure-spring-cloud-appconfiguration-config-convert-sample-complete](appconfiguration/azure-spring-cloud-appconfiguration-config/azure-spring-cloud-appconfiguration-config-convert-sample/azure-spring-cloud-appconfiguration-config-convert-sample-complete) | Yes                                | No                             | 
| App Configuration | [azure-spring-cloud-appconfiguration-config-convert-sample-initial](appconfiguration/azure-spring-cloud-appconfiguration-config/azure-spring-cloud-appconfiguration-config-convert-sample/azure-spring-cloud-appconfiguration-config-convert-sample-initial)   | Yes                                | No                             | 
| App Configuration | [azure-spring-cloud-feature-management-sample](appconfiguration/azure-spring-cloud-feature-management/azure-spring-cloud-feature-management-sample)                                                                                                            | Yes                                | No                             | 
| App Configuration | [azure-spring-cloud-feature-management-web-sample](appconfiguration/azure-spring-cloud-feature-management-web/azure-spring-cloud-feature-management-web-sample)                                                                                                | Yes                                | No                             | 
| App Configuration | [azure-spring-cloud-starter-appconfiguration-config-sample](appconfiguration/azure-spring-cloud-starter-appconfiguration-config/azure-spring-cloud-starter-appconfiguration-config-sample)                                                                     | Yes                                | No                             | 
| App Configuration | [appconfiguration-client](appconfiguration/spring-cloud-azure-starter-appconfiguration/appconfiguration-client)                                                                                                                                                | Yes                                | Yes                            | 
| Cache             | [azure-spring-cloud-sample-cache](cache/spring-cloud-azure-starter/spring-cloud-azure-sample-cache)                                                                                                                                                            | Yes                                | Yes                            | 
| Cloud Foundry     | [azure-cloud-foundry-service-sample](cloudfoundry/azure-cloud-foundry-service-sample)                                                                                                                                                                          | Yes                                | No                             | 
| Cosmos DB         | [cosmos-multi-database-multi-account](cosmos/azure-spring-data-cosmos/cosmos-multi-database-multi-account)                                                                                                                                                     | Yes                                | No                             | 
| Cosmos DB         | [cosmos-multi-database-single-account](cosmos/azure-spring-data-cosmos/cosmos-multi-database-single-account)                                                                                                                                                   | Yes                                | No                             | 
| Cosmos DB         | [spring-cloud-azure-data-cosmos-sample](cosmos/spring-cloud-azure-starter-data-cosmos/spring-cloud-azure-data-cosmos-sample)                                                                                                                                   | Yes                                | No                             | 
| Cosmos DB         | [spring-cloud-azure-cosmos-sample](cosmos/spring-cloud-azure-starter-cosmos/spring-cloud-azure-cosmos-sample)                                                                                                                                                  | Yes                                | Yes                            | 
| Event Hubs        | [spring-cloud-azure-sample-eventhubs-kafka](eventhubs/spring-cloud-azure-starter/spring-cloud-azure-sample-eventhubs-kafka)                                                                                                                                    | Yes                                | Yes                            | 
| Event Hubs        | [eventhubs-integration](eventhubs/spring-cloud-azure-starter-integration-eventhubs/eventhubs-integration)                                                                                                                                                      | Yes                                | Yes                            | 
| Event Hubs        | [eventhubs-binder](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-binder)                                                                                                                                                                      | Yes                                | Yes                            | 
| Event Hubs        | [eventhubs-multibinders](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-multibinders)                                                                                                                                                          | Yes                                | Yes                            | 
| Event Hubs        | [eventhubs-client](eventhubs/spring-cloud-azure-starter-eventhubs/eventhubs-client)                                                                                                                                                                            | Yes                                | Yes                            | 
| Key Vault         | [keyvault-certificates-client-side](keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-client-side)                                                                                                                                | Yes                                | No                             | 
| Key Vault         | [keyvault-certificates-server-side](keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-server-side)                                                                                                                                | Yes                                | No                             | 
| Key Vault         | [run-with-command-line-server-side](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-server-side)                                                                                                                                                    | Yes                                | No                             | 
| Key Vault         | [run-with-command-line-client-side](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-client-side)                                                                                                                                                    | Yes                                | No                             | 
| Key Vault         | [property-source](keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source)                                                                                                                                                                        | Yes                                | Yes                            | 
| Key Vault         | [secret-client](keyvault/spring-cloud-azure-starter-keyvault-secrets/secret-client)                                                                                                                                                                            | Yes                                | Yes                            | 
| Service Bus       | [servicebus-jms-queue](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-queue)                                                                                                                                                              | Yes                                | Yes                            | 
| Service Bus       | [servicebus-jms-topic](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-topic)                                                                                                                                                              | Yes                                | Yes                            | 
| Service Bus       | [single-namespace](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace)                                                                                                                                                              | Yes                                | Yes                            | 
| Service Bus       | [multiple-namespaces](servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces)                                                                                                                                                        | Yes                                | Yes                            | 
| Service Bus       | [servicebus-queue-binder](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder)                                                                                                                                                      | Yes                                | Yes                            | 
| Service Bus       | [servicebus-queue-multibinders](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-multibinders)                                                                                                                                                | Yes                                | Yes                            | 
| Service Bus       | [servicebus-topic-binder](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-topic-binder)                                                                                                                                                      | Yes                                | Yes                            | 
| Service Bus       | [servicebus-queue-binder-arm](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder-arm)                                                                                                                                              | Yes                                | Yes                            | 
| Storage           | [storage-blob-sample](storage/spring-cloud-azure-starter-storage-blob/storage-blob-sample)                                                                                                                                                                     | Yes                                | Yes                            | 
| Storage           | [storage-file-sample](storage/spring-cloud-azure-starter-storage-file-share/storage-file-sample)                                                                                                                                                               | Yes                                | Yes                            | 
| Storage           | [storage-queue-client](storage/spring-cloud-azure-starter-storage-queue/storage-queue-client)                                                                                                                                                                  | Yes                                | Yes                            | 
| Storage           | [storage-queue-integration](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-integration)                                                                                                                                            | Yes                                | Yes                            | 
| Storage           | [storage-queue-operation](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-operation)                                                                                                                                                | Yes                                | Yes                            | 
| Spring Native     | [storage-blob-native](spring-native/storage-blob-native)                                                                                                                                                                                                       | Yes                                | No                             |

## Getting Help
- If you have any question about using these samples, please [create an new issue](https://github.com/Azure-Samples/azure-spring-boot-samples/issues/new/choose).

## Run Samples Based On Spring Native in Spring Boot 2
Two Maven profiles have been defined in this project to support compiling Spring applications to native executables: `buildpack-4.x` and `native-4.x`. The `buildpack-4.x` profile will use [Buildpacks](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-buildpacks) and the `native-4.x` profile will use [Native Build Tools](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-native-build-tools). Please follow the [storage-blob-native sample](spring-native/storage-blob-native) for more details.

**Note**: This section is only suitable for Spring Native in Spring Boot 2.

## Samples for Spring Boot 3

**Note**: This section is used to convert the samples to run in Spring Boot 3, the branch `feature/spring-boot-3` is no longer maintained.

### Convert samples to use Spring Boot 3

To get samples for Spring Boot 3, please refer to the content in each sample's *README.md* like this:
> Current sample is using Spring Cloud Azure 4.x (which is compatible with Spring Boot 2.x).
> If you want sample about Spring Cloud Azure 6.x (which is compatible with Spring Boot 3.x),
> please refer to [CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3.md](./CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3_TEMPLATE.md).

Mostly samples can work with both Spring Cloud Azure 4.x and Spring Cloud Azure 6.x, you can directly activate the profile of Spring Cloud Azure 6.x to run.

If you're adding a new example, here's a template that converts the example to support Spring Boot 3: [./CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3_TEMPLATE.md](CONVERT_SAMPLE_TO_USE_SPRING_BOOT_3_TEMPLATE.md).

### Run samples with Maven command

Use below command to enable the profile for Spring Cloud Azure 6.x.

```shell
mvn clean spring-boot:run -P spring-cloud-azure-6.x
```

### Run samples with IDE

Activate the profile Spring Cloud Azure 6.x by default, then you can run in your IDE tool.

Remove the below activation for `spring-cloud-azure-4.x`, and add it to profile `spring-cloud-azure-6.x`:

```xml
<activation>
  <activeByDefault>true</activeByDefault>
</activation>
```

