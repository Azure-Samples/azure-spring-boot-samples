[![CodeQL](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml) [![CI](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml) [![Markdown Links Check](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml)

# Spring Cloud Azure Samples

- The repository hold samples about
  using [Spring Cloud Azure](https://learn.microsoft.com/azure/developer/java/spring-framework/)
  libraries.
- The **main** branch is using the latest stable version of Spring Cloud Azure. If you want to find
  sample about specific version of Spring Cloud Azure, please switch to corresponding tag in this
  repository.

## All Samples in This Repo

The samples for Spring Boot 3.x are in the main branch. The samples for Spring Boot 2.x are in the [spring-boot-2.x](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x) branch.
> [!NOTE]  
> Spring Boot 3.x using Spirng Cloud Azure 5.x, Spring Boot 2.x using Spring Cloud Azure 4.x.

### Microsoft Entra ID

| Sample Project                          | Spring Boot 3.x                                                               | Spring Boot 2.x                                                                                                                                                         | 
|-----------------------------------------|----------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| aad-resource-server-by-filter           | ✅[link](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter)       | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter)           | 
| aad-resource-server-by-filter-stateless | ✅[link](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter-stateless) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter-stateless) | 
| aad-web-application-and-resource-server | ✅[link](aad/spring-cloud-azure-starter-active-directory/aad-web-application-and-resource-server) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/aad/spring-cloud-azure-starter-active-directory/aad-web-application-and-resource-server) | 
| web-client-access-resource-server       | ✅[link](aad/spring-cloud-azure-starter-active-directory/web-client-access-resource-server)       | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/aad/spring-cloud-azure-starter-active-directory/web-client-access-resource-server)       | 
| aad-b2c-resource-server                 | ✅[link](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-resource-server)             | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-resource-server)             | 
| aad-b2c-web-application                 | ✅[link](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-web-application)             | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-web-application)             | 
| spring-security-samples                 | ✅[link](aad/spring-security)                                                                     | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/aad/spring-security)                                                                     |

### Azure App Configuration

| Sample Project                                                     | Spring Boot 3.x                                                                                                                                                                | Spring Boot 2.x                                                                                                                                                                                                                                                         | 
|--------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| spring-cloud-azure-appconfiguration-config-convert-sample-complete | ✅[link](appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-convert-sample/spring-cloud-azure-appconfiguration-config-convert-sample-complete) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-convert-sample/spring-cloud-azure-appconfiguration-config-convert-sample-complete) |
| spring-cloud-azure-appconfiguration-config-convert-sample-initial  | ✅[link](appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-convert-sample/spring-cloud-azure-appconfiguration-config-convert-sample-initial)  | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-convert-sample/spring-cloud-azure-appconfiguration-config-convert-sample-initial) |
| spring-cloud-azure-appconfiguration-config-sample                  | ✅[link](appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-sample)                                                                            | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-sample)                                                                           |
| spring-cloud-azure-feature-management-sample                       | ✅[link](appconfiguration/spring-cloud-azure-feature-management/spring-cloud-azure-feature-management-sample)                                                                                      | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/appconfiguration/spring-cloud-azure-feature-management/spring-cloud-azure-feature-management-sample)                                                                                     |
| spring-cloud-azure-feature-management-web-sample                   | ✅[link](appconfiguration/spring-cloud-azure-feature-management-web/spring-cloud-azure-feature-management-web-sample)                                                                              | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/appconfiguration/spring-cloud-azure-feature-management-web/spring-cloud-azure-feature-management-web-sample)                                                                             |
| spring-cloud-azure-targeting-filter-web-sample                     | ✅[link](appconfiguration/spring-cloud-azure-feature-management-web/spring-cloud-azure-targeting-filter-web-sample)                                                                                | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/appconfiguration/spring-cloud-azure-feature-management-web/spring-cloud-azure-targeting-filter-web-sample)                                                                               |
| spring-cloud-azure-starter-appconfiguration-config-sample          | ✅[link](appconfiguration/spring-cloud-azure-starter-appconfiguration-config/spring-cloud-azure-starter-appconfiguration-config-sample)                                                            | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/appconfiguration/spring-cloud-azure-starter-appconfiguration-config/spring-cloud-azure-starter-appconfiguration-config-sample)                                                           |
| spring-cloud-azure-starter-appconfiguration-config-entraid-sample  | ✅[link](appconfiguration/spring-cloud-azure-starter-appconfiguration-config/spring-cloud-azure-starter-appconfiguration-config-entraid-sample)                                                    | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/appconfiguration/spring-cloud-azure-starter-appconfiguration-config/spring-cloud-azure-starter-appconfiguration-config-entraid-sample)                                                   |
| appconfiguration-client                                            | ✅[link](appconfiguration/spring-cloud-azure-starter-appconfiguration/appconfiguration-client)                                                                                                     | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/appconfiguration/spring-cloud-azure-starter-appconfiguration/appconfiguration-client)                                                                                                    |

### Azure Cache

| Sample Project                               | Spring Boot 3.x                          | Spring Boot 2.x                                                                                                                    | 
|----------------------------------------------|---------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| spring-cloud-azure-cache-sample              | ✅[link](cache/spring-cloud-azure-redis-sample)              | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/cache/spring-cloud-azure-sample-cache)              |
| spring-cloud-azure-cache-passwordless-sample | ✅[link](cache/spring-cloud-azure-redis-sample-passwordless) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/cache/spring-cloud-azure-sample-cache-passwordless) |

### Azure Cosmos DB

| Sample Project                        | Spring Boot 3.x                                                             | Spring Boot 2.x                                                                                                                                                       | 
|---------------------------------------|--------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| cosmos-aad-sample                     | ✅[link](cosmos/azure-spring-data-cosmos/cosmos-aad-sample)                                     | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/cosmos/azure-spring-data-cosmos/cosmos-aad-sample)                                     |
| cosmos-multi-database-multi-account   | ✅[link](cosmos/azure-spring-data-cosmos/cosmos-multi-database-multi-account)                   | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/cosmos/azure-spring-data-cosmos/cosmos-multi-database-multi-account)                   |
| cosmos-multi-database-single-account  | ✅[link](cosmos/azure-spring-data-cosmos/cosmos-multi-database-single-account)                  | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/cosmos/azure-spring-data-cosmos/cosmos-multi-database-single-account)                  |
| cosmos-multi-tenant-by-container      | ✅[link](cosmos/azure-spring-data-cosmos/cosmos-multi-tenant-samples/multi-tenant-by-container) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/cosmos/azure-spring-data-cosmos/cosmos-multi-tenant-samples/multi-tenant-by-container) |
| cosmos-multi-tenant-by-database       | ✅[link](cosmos/azure-spring-data-cosmos/cosmos-multi-tenant-samples/multi-tenant-by-database)  | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/cosmos/azure-spring-data-cosmos/cosmos-multi-tenant-samples/multi-tenant-by-database)  |
| cosmos-mvc-sample                     | ✅[link](cosmos/azure-spring-data-cosmos/cosmos-mvc-sample)                                     | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/cosmos/azure-spring-data-cosmos/cosmos-mvc-sample)                                     |
| cosmos-quickstart-samples             | ✅[link](cosmos/azure-spring-data-cosmos/cosmos-quickstart-samples)                             | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/cosmos/azure-spring-data-cosmos/cosmos-quickstart-samples)                             |
| spring-cloud-azure-data-cosmos-sample | ✅[link](cosmos/spring-cloud-azure-starter-data-cosmos/spring-cloud-azure-data-cosmos-sample)   | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/cosmos/spring-cloud-azure-starter-data-cosmos)                                         | 
| spring-cloud-azure-cosmos-sample      | ✅[link](cosmos/spring-cloud-azure-starter-cosmos/spring-cloud-azure-cosmos-sample)             | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/cosmos/spring-cloud-azure-starter-cosmos/spring-cloud-azure-cosmos-sample)             |

### Azure Event Grid

| Sample Project                      | Spring Boot 3.x                                                          | Spring Boot 2.x | 
|-------------------------------------|-----------------------------------------------------------------------------------------|--------------------------------|
| spring-cloud-azure-sample-eventgrid | ✅[link](eventgrid/spring-cloud-azure-starter-eventgrid/spring-cloud-azure-sample-eventgrid) | ❌                              | 

### Azure Event Hubs

| Sample Project                            | Spring Boot 3.x                                                        | Spring Boot 2.x                                                                                                                                                  | 
|-------------------------------------------|---------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| spring-cloud-azure-sample-eventhubs-kafka | ✅[link](eventhubs/spring-cloud-azure-starter/spring-cloud-azure-sample-eventhubs-kafka)   | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/eventhubs/spring-cloud-azure-starter/spring-cloud-azure-sample-eventhubs-kafka)   | 
| eventhubs-integration                     | ✅[link](eventhubs/spring-cloud-azure-starter-integration-eventhubs/eventhubs-integration) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/eventhubs/spring-cloud-azure-starter-integration-eventhubs/eventhubs-integration) | 
| eventhubs-binder                          | ✅[link](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-binder)            | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-binder)            | 
| eventhubs-multibinders                    | ✅[link](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-multibinders)      | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-multibinders)      | 
| eventhubs-client                          | ✅[link](eventhubs/spring-cloud-azure-starter-eventhubs/eventhubs-client)                  | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/eventhubs/spring-cloud-azure-starter-eventhubs/eventhubs-client)                  |
| eventhubs-spring-messaging                | ✅[link](eventhubs/spring-messaging-azure-eventhubs/eventhubs-spring-messaging)            | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/eventhubs/spring-messaging-azure-eventhubs/eventhubs-spring-messaging)            |

### Azure Key Vault

| Sample Project                    | Spring Boot 3.x                                              | Spring Boot 2.x                                                                                                                                                                     | 
|-----------------------------------|-----------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| keyvault-certificates-client-side | ❌                                                                           | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-client-side) | 
| keyvault-certificates-server-side | ❌                                                                           | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-server-side) | 
| run-with-command-line-server-side | ✅[link](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-server-side) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/keyvault/azure-securtiy-keyvault-jca/run-with-command-line-server-side)                     | 
| run-with-command-line-client-side | ✅[link](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-client-side) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/keyvault/azure-securtiy-keyvault-jca/run-with-command-line-client-side)                     | 
| property-source                   | ✅[link](keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source)   | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source)                       | 
| secret-client                     | ✅[link](keyvault/spring-cloud-azure-starter-keyvault-secrets/secret-client)     | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/keyvault/spring-cloud-azure-starter-keyvault-secrets/secret-client)                         |

### Azure Database for MySQL

| Sample Project                  | Spring Boot 3.x                                                   | Spring Boot 2.x                                                                                                                                             | 
|---------------------------------|----------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| spring-cloud-azure-mysql-sample | ✅[link](mysql/spring-cloud-azure-starter-jdbc-mysql/spring-cloud-azure-mysql-sample) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/mysql/spring-cloud-azure-starter-jdbc-mysql/spring-cloud-azure-mysql-sample) | 

### Azure Database for PostgreSQL

| Sample Project                       | Spring Boot 3.x                                                                  | Spring Boot 2.x                                                                                                                                                            | 
|--------------------------------------|-------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| spring-cloud-azure-postgresql-sample | ✅[link](postgresql/spring-cloud-azure-starter-jdbc-postgresql/spring-cloud-azure-postgresql-sample) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/postgresql/spring-cloud-azure-starter-jdbc-postgresql/spring-cloud-azure-postgresql-sample) |

### Azure Service Bus

| Sample Project                | Spring Boot 3.x                                                          | Spring Boot 2.x                                                                                                                                                      | 
|-------------------------------|-----------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| servicebus-queue              | ✅[link](servicebus/spring-cloud-azure-starter-servicebus/servicebus-queue)                  | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/servicebus/spring-cloud-azure-starter-servicebus/servicebus-queue)                    | 
| servicebus-topic              | ✅[link](servicebus/spring-cloud-azure-starter-servicebus/servicebus-topic)                  | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/servicebus/spring-cloud-azure-starter-servicebus/servicebus-topic)                    | 
| servicebus-jms-queue          | ✅[link](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-queue)          | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-queue)            | 
| servicebus-jms-topic          | ✅[link](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-topic)          | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-topic)            | 
| single-namespace              | ✅[link](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace)      | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace)        | 
| multiple-namespaces           | ✅[link](servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces)   | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces)     | 
| servicebus-queue-binder       | ✅[link](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder)     | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder)       | 
| servicebus-queue-multibinders | ✅[link](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-multibinders)     | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-multibinders) | 
| servicebus-topic-binder       | ✅[link](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-topic-binder)     | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-topic-binder)       | 
| servicebus-queue-binder-arm   | ✅[link](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder-arm) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder-arm)   |
| servicebus-spring-messaging   | ✅[link](servicebus/spring-messaging-azure-servicebus/servicebus-spring-messaging)           | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/servicebus/spring-messaging-azure-servicebus/servicebus-spring-messaging)             |

### Azure Storage

| Sample Project                 | Spring Boot 3.x                                                              | Spring Boot 2.x                                                                                                                                                        | 
|--------------------------------|---------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| storage-blob-sample            | ✅[link](storage/spring-cloud-azure-starter-storage-blob/storage-blob-sample)                    | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/storage/spring-cloud-azure-starter-storage-blob/storage-blob-sample)                    | 
| storage-file-sample            | ✅[link](storage/spring-cloud-azure-starter-storage-file-share/storage-file-sample)              | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/storage/spring-cloud-azure-starter-storage-file-share/storage-file-sample)              | 
| storage-queue-client           | ✅[link](storage/spring-cloud-azure-starter-storage-queue/storage-queue-client)                  | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/storage/spring-cloud-azure-starter-storage-queue/storage-queue-client)                  | 
| storage-queue-integration      | ✅[link](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-integration) | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-integration) | 
| storage-queue-operation        | ✅[link](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-operation)   | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-operation)   |
| storage-queue-spring-messaging | ✅[link](storage/spring-messaging-azure-storage-queue/storage-queue-spring-messaging)            | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/storage/spring-messaging-azure-storage-queue/storage-queue-spring-messaging)            |

### Spring PetClinic

| Sample Project                 | Spring Boot 3.x | Spring Boot 2.x                                                                                                | 
|--------------------------------|--------------------------------|-------------------------------------------------------------------------------------------------------------------------------|
| spring-petclinic-microservices | ❌                              | ✅[link](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-boot-2.x/spring-petclinic-microservices) | 

### TestContainers Support

| Sample Project                   | Spring Boot 3.x                                                 | Spring Boot 2.x | 
|----------------------------------|--------------------------------------------------------------------------------|--------------------------------|
| testContainers for Cosmos        | ✅[link](testcontainers/spring-cloud-azure-testcontainers-for-cosmos-sample)        | ❌                              |
| testContainers for Storage Blob  | ✅[link](testcontainers/spring-cloud-azure-testcontainers-for-storage-blob-sample)  | ❌                              |
| testContainers for Storage Queue | ✅[link](testcontainers/spring-cloud-azure-testcontainers-for-storage-queue-sample) | ❌                              |

## Getting Help

- If you have any question about using these samples,
  please [create an new issue](https://github.com/Azure-Samples/azure-spring-boot-samples/issues/new/choose).

## Work with Spring Native

Since we removed the parent pom, the Profiles `native-5.x` and `nativeTest-5.x` are removed either.
They are variants of the two profiles `native` and `nativeTest` provided by
`spring-boot-starter-parent` of Spring Boot 3, and they have the same function.
So if you want to use them, just add them into your current pom:

```yaml
    <profile>
      <id>native-5.x</id>
      <build>
        <pluginManagement>
          <plugins>
            <plugin>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-maven-plugin</artifactId>
              <configuration>
                <image>
                  <builder>paketobuildpacks/builder:tiny</builder>
                  <env>
                    <BP_NATIVE_IMAGE>true</BP_NATIVE_IMAGE>
                  </env>
                </image>
              </configuration>
              <executions>
                <execution>
                  <id>process-aot</id>
                  <goals>
                    <goal>process-aot</goal>
                  </goals>
                </execution>
              </executions>
            </plugin>
            <plugin>
              <groupId>org.graalvm.buildtools</groupId>
              <artifactId>native-maven-plugin</artifactId>
              <configuration>
                <classesDirectory>${project.build.outputDirectory}</classesDirectory>
                <metadataRepository>
                  <enabled>true</enabled>
                </metadataRepository>
                <requiredVersion>22.3</requiredVersion>
              </configuration>
              <executions>
                <execution>
                  <id>add-reachability-metadata</id>
                  <goals>
                    <goal>add-reachability-metadata</goal>
                  </goals>
                </execution>
              </executions>
            </plugin>
          </plugins>
        </pluginManagement>
      </build>
    </profile>
    <profile>
      <id>nativeTest-5.x</id>
      <dependencies>
        <dependency>
          <groupId>org.junit.platform</groupId>
          <artifactId>junit-platform-launcher</artifactId>
          <scope>test</scope>
        </dependency>
      </dependencies>
      <build>
        <plugins>
          <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
              <execution>
                <id>process-test-aot</id>
                <goals>
                  <goal>process-test-aot</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
          <plugin>
            <groupId>org.graalvm.buildtools</groupId>
            <artifactId>native-maven-plugin</artifactId>
            <configuration>
              <classesDirectory>${project.build.outputDirectory}</classesDirectory>
              <metadataRepository>
                <enabled>true</enabled>
              </metadataRepository>
              <requiredVersion>22.3</requiredVersion>
            </configuration>
            <executions>
              <execution>
                <id>native-test</id>
                <goals>
                  <goal>test</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
        </plugins>
      </build>
    </profile>
  </profiles>
```
