
## Current Branch Supported versions
- [spring-boot-dependencies:2.5.2](https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-dependencies/2.5.2/spring-boot-dependencies-2.5.2.pom).
- [spring-cloud-dependencies:2020.0.3](https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-dependencies/2020.0.3/spring-cloud-dependencies-2020.0.3.pom).

## All active branches

| spring boot version | spring cloud version | azure spring boot version | 
| ---                 | ---                  | ---                       | 
| 2.5.2               | 2020.0.3             | 3.6.x                     | 

## All samples in this repo
### AAD
- com.azure.spring:azure-spring-boot-starter-active-directory-b2c:3.6.0
    - [azure-spring-boot-sample-active-directory-b2c-oidc](/aad/azure-spring-boot-sample-active-directory-b2c-oidc) 
    - [azure-spring-boot-sample-active-directory-b2c-resource-server](/aad/azure-spring-boot-sample-active-directory-b2c-resource-server) 
- com.azure.spring:azure-spring-boot-starter-active-directory:3.6.0
    - [azure-spring-boot-sample-active-directory-resource-server](/aad/azure-spring-boot-sample-active-directory-resource-server) 
    - [azure-spring-boot-sample-active-directory-resource-server-by-filter](/aad/azure-spring-boot-sample-active-directory-resource-server-by-filter) 
    - [azure-spring-boot-sample-active-directory-resource-server-by-filter-stateless](/aad/azure-spring-boot-sample-active-directory-resource-server-by-filter-stateless) 
    - [azure-spring-boot-sample-active-directory-resource-server-obo](/aad/azure-spring-boot-sample-active-directory-resource-server-obo) 
    - [azure-spring-boot-sample-active-directory-webapp](/aad/azure-spring-boot-sample-active-directory-webapp) 

### Appconfiguration
- com.microsoft.azure:spring-cloud-starter-azure-appconfiguration-config:1.3.0
    - [azure-appconfiguration-conversion-sample-complete](/appconfiguration/azure-appconfiguration-conversion-sample-complete)
- com.microsoft.azure:spring-cloud-azure-appconfiguration-config:1.3.0
    - [azure-appconfiguration-sample](/appconfiguration/azure-appconfiguration-sample) 
- com.microsoft.azure:spring-cloud-azure-feature-management:1.3.0
    - [feature-management-sample](/appconfiguration/feature-management-sample) 
    - [feature-management-web-sample](/appconfiguration/feature-management-web-sample)
- com.azure.spring:azure-spring-boot-starter-cosmos:3.6.0
    - [azure-appconfiguration-conversion-sample-initial](/appconfiguration/azure-appconfiguration-conversion-sample-initial)

### Cache
- com.azure.spring:azure-spring-cloud-starter-cache:2.6.0
    - [azure-spring-cloud-sample-cache](/cache/azure-spring-cloud-sample-cache) 

### Cloudfoundry
- com.azure.spring:azure-spring-boot-starter-cosmos:3.6.0
- com.azure.spring:azure-spring-boot-starter-servicebus-jms:3.6.0
    - [azure-cloud-foundry-service-sample](/cloudfoundry/azure-cloud-foundry-service-sample) 
  
### Cosmos
- com.azure.spring:azure-spring-boot-starter-cosmos:3.6.0
    - [azure-spring-boot-sample-cosmos](/cosmos/azure-spring-boot-sample-cosmos) 
    - [azure-spring-boot-sample-cosmos-multi-database-multi-account](/cosmos/azure-spring-boot-sample-cosmos-multi-database-multi-account) 
    - [azure-spring-boot-sample-cosmos-multi-database-single-account](/cosmos/azure-spring-boot-sample-cosmos-multi-database-single-account)

### Eventhubs
- com.azure.spring:azure-spring-cloud-stream-binder-eventhubs:2.6.0
    - [azure-spring-cloud-sample-eventhubs-binder](/eventhubs/azure-spring-cloud-sample-eventhubs-binder) 
    - [azure-spring-cloud-sample-eventhubs-multibinders](/eventhubs/azure-spring-cloud-sample-eventhubs-multibinders)    
- com.azure.spring:azure-spring-cloud-starter-eventhubs-kafka:2.6.0
    - [azure-spring-cloud-sample-eventhubs-kafka](/eventhubs/azure-spring-cloud-sample-eventhubs-kafka) 
- com.azure.spring:azure-spring-cloud-starter-eventhubs:2.6.0     
    - [azure-spring-cloud-sample-eventhubs-operation](/eventhubs/azure-spring-cloud-sample-eventhubs-operation) 
    - [azure-spring-cloud-sample-messaging-eventhubs](/eventhubs/azure-spring-cloud-sample-messaging-eventhubs) 
    - [azure-spring-integration-sample-eventhubs](/eventhubs/azure-spring-integration-sample-eventhubs) 

### Keyvault
- com.azure.spring:azure-spring-boot-starter-keyvault-certificates:3.0.1
    - [azure-spring-boot-sample-keyvault-certificates-client-side](/keyvault/azure-spring-boot-sample-keyvault-certificates-client-side) 
    - [azure-spring-boot-sample-keyvault-certificates-server-side](/keyvault/azure-spring-boot-sample-keyvault-certificates-server-side) 
- com.azure.spring:azure-spring-boot-starter-keyvault-secrets:3.6.0      
    - [azure-spring-boot-sample-keyvault-secrets](/keyvault/azure-spring-boot-sample-keyvault-secrets) 

### Mediaservices
- com.microsoft.azure:azure-media:0.9.8
    - [azure-spring-boot-sample-mediaservices](/mediaservices/azure-spring-boot-sample-mediaservices) 
  
### Servicebus
- com.azure:azure-messaging-servicebus:7.2.3
    - [azure-spring-boot-sample-servicebus](/servicebus/azure-spring-boot-sample-servicebus)    
- com.azure.spring:azure-spring-boot-starter-servicebus-jms:3.6.0
    - [azure-spring-boot-sample-servicebus-jms-topic](/servicebus/azure-spring-boot-sample-servicebus-jms-topic)
- com.azure.spring:azure-spring-cloud-starter-servicebus:2.6.0
    - [azure-spring-cloud-sample-messaging-servicebus](/servicebus/azure-spring-cloud-sample-messaging-servicebus) 
    - [azure-spring-cloud-sample-servicebus-operation](/servicebus/azure-spring-cloud-sample-servicebus-operation)
    - [azure-spring-integration-sample-servicebus](/servicebus/azure-spring-integration-sample-servicebus)
- com.azure.spring:azure-spring-cloud-stream-binder-servicebus-queue:2.6.0
    - [azure-spring-cloud-sample-servicebus-queue-binder](/servicebus/azure-spring-cloud-sample-servicebus-queue-binder) 
    - [azure-spring-cloud-sample-servicebus-queue-multibinders](/servicebus/azure-spring-cloud-sample-servicebus-queue-multibinders) 
- com.azure.spring:azure-spring-cloud-stream-binder-servicebus-topic:2.6.0
    - [azure-spring-cloud-sample-servicebus-topic-binder](/servicebus/azure-spring-cloud-sample-servicebus-topic-binder) 

### Storage
- com.azure.spring:azure-spring-boot-starter-storage:3.6.0
    - [azure-spring-boot-sample-storage-resource](/storage/azure-spring-boot-sample-storage-resource)
- com.azure.spring:azure-spring-cloud-starter-storage-queue:2.6.0
    - [azure-spring-cloud-sample-storage-queue-operation](/storage/azure-spring-cloud-sample-storage-queue-operation) 
    - [azure-spring-integration-sample-storage-queue](/storage/azure-spring-integration-sample-storage-queue) 


[main]: https://github.com/Azure-Samples/azure-spring-boot-samples
