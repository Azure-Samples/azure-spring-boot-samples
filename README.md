[![CodeQL](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml) [![CI](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml) [![Markdown Links Check](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml)

# Spring Cloud Azure Samples

- The repository hold samples about
  using [Spring Cloud Azure](https://learn.microsoft.com/azure/developer/java/spring-framework/)
  libraries.
- The **main** branch is using the latest stable version of Spring Cloud Azure. If you want to find
  sample about specific version of Spring Cloud Azure, please switch to corresponding tag in this
  repository.

> [!IMPORTANT]  
> We removed the parent pom, so you can directly run each sample without opening the whole projects.
> We removed all samples for Spring Cloud Azure 4.x from main branch, so if you still want to try,
> please use `Spring Cloud Azure 4.x` branch.

## All Samples in This Repo

### Microsoft Entra ID

| Sample Project                          | Support Spring Cloud Azure 5.x                                                               | Support Spring Cloud Azure 4.x | 
|-----------------------------------------|----------------------------------------------------------------------------------------------|--------------------------------|
| aad-resource-server-by-filter           | [✅](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter)           | ❌                              | 
| aad-resource-server-by-filter-stateless | [✅](aad/spring-cloud-azure-starter-active-directory/aad-resource-server-by-filter-stateless) | ❌                              | 
| aad-web-application-and-resource-server | [✅](aad/spring-cloud-azure-starter-active-directory/aad-web-application-and-resource-server) | ❌                              | 
| web-client-access-resource-server       | [✅](aad/spring-cloud-azure-starter-active-directory/web-client-access-resource-server)       | ❌                              | 
| aad-b2c-resource-server                 | [✅](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-resource-server)             | ❌                              | 
| aad-b2c-web-application                 | [✅](aad/spring-cloud-azure-starter-active-directory-b2c/aad-b2c-web-application)             | ❌                              | 
| spring-security-samples                 | [✅](aad/spring-security)                                                                     | ❌                              |

### Azure App Configuration

| Sample Project                                                     | Support Spring Cloud Azure 5.x                                                                                                                                                                | Support Spring Cloud Azure 4.x | 
|--------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|
| spring-cloud-azure-appconfiguration-config-convert-sample-complete | [✅](appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-convert-sample/spring-cloud-azure-appconfiguration-config-convert-sample-complete) | [✅                             |
| spring-cloud-azure-appconfiguration-config-sample                  | [✅](appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-sample)                                                                            | [✅                             |
| spring-cloud-azure-appconfiguration-config-convert-sample-initial  | [✅](appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-convert-sample/spring-cloud-azure-appconfiguration-config-convert-sample-initial)  | [✅                             |
| spring-cloud-azure-appconfiguration-config-sample                  | [✅](appconfiguration/spring-cloud-azure-appconfiguration-config/spring-cloud-azure-appconfiguration-config-sample)                                                                            | [✅                             |
| spring-cloud-azure-feature-management-sample                       | [✅](appconfiguration/spring-cloud-azure-feature-management/spring-cloud-azure-feature-management-sample)                                                                                      | [✅                             |
| spring-cloud-azure-feature-management-web-sample                   | [✅](appconfiguration/spring-cloud-azure-feature-management-web/spring-cloud-azure-feature-management-web-sample)                                                                              | [✅                             |
| spring-cloud-azure-targeting-filter-web-sample                     | [✅](appconfiguration/spring-cloud-azure-feature-management-web/spring-cloud-azure-targeting-filter-web-sample)                                                                                | [✅                             |
| spring-cloud-azure-starter-appconfiguration-config-sample          | [✅](appconfiguration/spring-cloud-azure-starter-appconfiguration-config/spring-cloud-azure-starter-appconfiguration-config-sample)                                                            | [✅                             |
| spring-cloud-azure-starter-appconfiguration-config-entraid-sample  | [✅](appconfiguration/spring-cloud-azure-starter-appconfiguration-config/spring-cloud-azure-starter-appconfiguration-config-entraid-sample)                                                    | [✅                             |
| appconfiguration-client                                            | [✅](appconfiguration/spring-cloud-azure-starter-appconfiguration/appconfiguration-client)                                                                                                     | [✅                             |

### Azure Cache

| Sample Project                               | Support Spring Cloud Azure 5.x                          | Support Spring Cloud Azure 4.x | 
|----------------------------------------------|---------------------------------------------------------|--------------------------------|
| spring-cloud-azure-cache-sample              | [✅](cache/spring-cloud-azure-redis-sample)              | [✅                             |
| spring-cloud-azure-cache-passwordless-sample | [✅](cache/spring-cloud-azure-redis-sample-passwordless) | [✅                             |

### Azure Cosmos DB

| Sample Project                        | Support Spring Cloud Azure 5.x                                                             | Support Spring Cloud Azure 4.x | 
|---------------------------------------|--------------------------------------------------------------------------------------------|--------------------------------|
| cosmos-aad-sample                     | [✅](cosmos/azure-spring-data-cosmos/cosmos-aad-sample)                                     | [✅                             |
| cosmos-multi-database-multi-account   | [✅](cosmos/azure-spring-data-cosmos/cosmos-multi-database-multi-account)                   | [✅                             |
| cosmos-multi-database-single-account  | [✅](cosmos/azure-spring-data-cosmos/cosmos-multi-database-single-account)                  | [✅                             |
| cosmos-multi-tenant-by-container      | [✅](cosmos/azure-spring-data-cosmos/cosmos-multi-tenant-samples/multi-tenant-by-container) | [✅                             |
| cosmos-multi-tenant-by-database       | [✅](cosmos/azure-spring-data-cosmos/cosmos-multi-tenant-samples/multi-tenant-by-database)  | [✅                             |
| cosmos-mvc-sample                     | [✅](cosmos/azure-spring-data-cosmos/cosmos-mvc-sample)                                     | [✅                             |
| cosmos-quickstart-samples             | [✅](cosmos/azure-spring-data-cosmos/cosmos-quickstart-samples)                             | [✅                             |
| spring-cloud-azure-data-cosmos-sample | [✅](cosmos/spring-cloud-azure-starter-data-cosmos/spring-cloud-azure-data-cosmos-sample)   | [✅                             | 
| spring-cloud-azure-cosmos-sample      | [✅](cosmos/spring-cloud-azure-starter-cosmos/spring-cloud-azure-cosmos-sample)             | [✅                             |

### Azure Event Hubs

| Sample Project                            | Support Spring Cloud Azure 5.x                                                        | Support Spring Cloud Azure 4.x | 
|-------------------------------------------|---------------------------------------------------------------------------------------|--------------------------------|
| spring-cloud-azure-sample-eventhubs-kafka | [✅](eventhubs/spring-cloud-azure-starter/spring-cloud-azure-sample-eventhubs-kafka)   | [✅                             | 
| eventhubs-integration                     | [✅](eventhubs/spring-cloud-azure-starter-integration-eventhubs/eventhubs-integration) | [✅                             | 
| eventhubs-binder                          | [✅](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-binder)            | [✅                             | 
| eventhubs-multibinders                    | [✅](eventhubs/spring-cloud-azure-stream-binder-eventhubs/eventhubs-multibinders)      | [✅                             | 
| eventhubs-client                          | [✅](eventhubs/spring-cloud-azure-starter-eventhubs/eventhubs-client)                  | [✅                             |
| eventhubs-spring-messaging                | [✅](eventhubs/spring-messaging-azure-eventhubs/eventhubs-spring-messaging)            | [✅                             |

### Azure Key Vault

| Sample Project                                                                                                                  | Support Spring Cloud Azure 5.x                                            | Support Spring Cloud Azure 4.x | 
|---------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------|--------------------------------|
| [keyvault-certificates-client-side](keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-client-side) | ❌                                                                         | ✅                              | 
| [keyvault-certificates-server-side](keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-server-side) | ❌                                                                         | ✅                              | 
| [run-with-command-line-server-side](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-server-side)                     | ❌                                                                         | ✅                              | 
| [run-with-command-line-client-side](keyvault/azure-securtiy-keyvault-jca/run-with-command-line-client-side)                     | ❌                                                                         | ✅                              | 
| property-source                                                                                                                 | [✅](keyvault/spring-cloud-azure-starter-keyvault-secrets/property-source) | [✅                             | 
| secret-client                                                                                                                   | [✅](keyvault/spring-cloud-azure-starter-keyvault-secrets/secret-client)   | [✅                             |

### Azure Database for MySQL

| Sample Project                  | Support Spring Cloud Azure 5.x                                                   | Support Spring Cloud Azure 4.x | 
|---------------------------------|----------------------------------------------------------------------------------|--------------------------------|
| spring-cloud-azure-mysql-sample | [✅](mysql/spring-cloud-azure-starter-jdbc-mysql/spring-cloud-azure-mysql-sample) | [✅                             | 

### Azure Database for PostgreSQL

| Sample Project                       | Support Spring Cloud Azure 5.x                                                                  | Support Spring Cloud Azure 4.x | 
|--------------------------------------|-------------------------------------------------------------------------------------------------|--------------------------------|
| spring-cloud-azure-postgresql-sample | [✅](postgresql/spring-cloud-azure-starter-jdbc-postgresql/spring-cloud-azure-postgresql-sample) | [✅                             |

### Azure Service Bus

| Sample Project                | Support Spring Cloud Azure 5.x                                                          | Support Spring Cloud Azure 4.x | 
|-------------------------------|-----------------------------------------------------------------------------------------|--------------------------------|
| servicebus-queue              | [✅](servicebus/spring-cloud-azure-starter-servicebus/servicebus-queue)                  | [✅                             | 
| servicebus-topic              | [✅](servicebus/spring-cloud-azure-starter-servicebus/servicebus-topic)                  | [✅                             | 
| servicebus-jms-queue          | [✅](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-queue)          | [✅                             | 
| servicebus-jms-topic          | [✅](servicebus/spring-cloud-azure-starter-servicebus-jms/servicebus-jms-topic)          | [✅                             | 
| single-namespace              | [✅](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace)      | [✅                             | 
| multiple-namespaces           | [✅](servicebus/spring-cloud-azure-starter-integration-servicebus/multiple-namespaces)   | [✅                             | 
| servicebus-queue-binder       | [✅](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder)     | [✅                             | 
| servicebus-queue-multibinders | [✅](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-multibinders)     | [✅                             | 
| servicebus-topic-binder       | [✅](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-topic-binder)     | [✅                             | 
| servicebus-queue-binder-arm   | [✅](servicebus/spring-cloud-azure-stream-binder-servicebus/servicebus-queue-binder-arm) | [✅                             |
| servicebus-spring-messaging   | [✅](servicebus/spring-messaging-azure-servicebus/servicebus-spring-messaging)           | [✅                             |

### Azure Storage

| Sample Project                 | Support Spring Cloud Azure 5.x                                                              | Support Spring Cloud Azure 4.x | 
|--------------------------------|---------------------------------------------------------------------------------------------|--------------------------------|
| storage-blob-sample            | [✅](storage/spring-cloud-azure-starter-storage-blob/storage-blob-sample)                    | ✅                              | 
| storage-file-sample            | [✅](storage/spring-cloud-azure-starter-storage-file-share/storage-file-sample)              | ✅                              | 
| storage-queue-client           | [✅](storage/spring-cloud-azure-starter-storage-queue/storage-queue-client)                  | ✅                              | 
| storage-queue-integration      | [✅](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-integration) | ✅                              | 
| storage-queue-operation        | [✅](storage/spring-cloud-azure-starter-integration-storage-queue/storage-queue-operation)   | ✅                              |
| storage-queue-spring-messaging | [✅](storage/spring-messaging-azure-storage-queue/storage-queue-spring-messaging)            | ✅                              |

### Spring PetClinic

| Sample Project                                                   | Support Spring Cloud Azure 5.x | Support Spring Cloud Azure 4.x | 
|------------------------------------------------------------------|--------------------------------|--------------------------------|
| [spring-petclinic-microservices](spring-petclinic-microservices) | ❌                              | ✅                              | 

### TestContainers Support

| Sample Project                   | Support Spring Cloud Azure 5.x                                                 | Support Spring Cloud Azure 4.x | 
|----------------------------------|--------------------------------------------------------------------------------|--------------------------------|
| testContainers for Cosmos        | [✅](testcontainers/spring-cloud-azure-testcontainers-for-cosmos-sample)        | ❌                              |
| testContainers for Storage Blob  | [✅](testcontainers/spring-cloud-azure-testcontainers-for-storage-blob-sample)  | ❌                              |
| testContainers for Storage Queue | [✅](testcontainers/spring-cloud-azure-testcontainers-for-storage-queue-sample) | ❌                              |

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
