# Azure Cosmos DB Spring Data - Multi Tenant by Container

[Spring Data Cosmos](https://aka.ms/SpringDataCosmos) sample for a multi-tenanted app where each tenant has its own [Azure Cosmos DB](https://learn.microsoft.com/azure/cosmos-db/introduction) [container](https://learn.microsoft.com/azure/cosmos-db/resource-model#azure-cosmos-db-containers).

## Features

- The application is a simple CRUD REST web service which creates `User` and `Order` entries in each tenant (`colocated` in the same container), and makes use of  `azure-spring-data-cosmos` for Azure Cosmos DB [NoSQL](https://learn.microsoft.com/azure/cosmos-db/nosql/) API.
- The `tenants` database is created if it does not exist. It will be used to store all containers created for each tenant.
- At application startup, all the names of any existing containers in the `tenants` database are retrieved and stored in `tenantList` in [TenantStorage](./src/main/java/com/azure/spring/data/cosmos/example/tenant/TenantStorage.java)  class. This class also contains resources to create Cosmos containers named by `tenantId` dynamically. The `tenants` database is created if it does not exist.
- The application uses [WebRequestInterceptor](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/context/request/WebRequestInterceptor.html) to capture a http request header of `TenantId`. This is used to check if the corresponding tenant id exists in `tenantList`. If it does not, the container will be created.
- `WebRequestInterceptor` is also used to capture a http request header of `TenantTier`. This is used to determine whether to set dedicated throughput for the container ("premium") or use the shared throughput for the database. 
- If a tenant container does not exist, the [MultiTenantContainerCosmosFactory](./src/main/java/com/azure/spring/data/cosmos/example/tenant/MultiTenantContainerCosmosFactory.java) class overrides `overrideContainerName()` in `CosmosFactory` (see PR https://github.com/Azure/azure-sdk-for-java/pull/33400) to allow different tenant containers to be created and/or referenced on the fly, while still allowing native Spring Repository APIs to query the tenant container.
- The methods in [TenantStorage](./src/main/java/com/azure/spring/data/cosmos/example/tenant/TenantStorage.java) are used to retrieve the appropriate tenant id from `tenantList`, or create a tenant container, and add its name to the list of existing tenants.
- CRUD operations are performed in `UserController` and `OrderController` using corresponding Spring `Repository` APIs. The two entities `Order` and `User` are co-located in the same container, and differentiated using a `type` field in the record.

## Multi-tenancy considerations

This sample application fetches the value of the tenant from request header (TenantId). In a real-world application, it is up to you how to identify this while keeping your application secure. For example, you may want to fetch the identifier from a cookie, or other header name. The approach of assigning a container (or database) to each tenant may be useful if it is necessary to strictly isolate performance for each tenant. However, you should consider the trade-offs in taking this approach. Review our article on [Multitenancy and Azure Cosmos DB](https://learn.microsoft.com/azure/architecture/guide/multitenant/service/cosmos-db) for more guidance. **Note**: this is a contrived sample in terms of the data model, and does not reflect any recommendations for how to model any *application* in particular. It is intented purely to illustrate how to maintain a container-per-tenant isolation model using Spring Data for Azure Cosmos DB, where containers for each tenant are created/referenced dynamically (on-the-fly) rather than being hardcoded at project startup.
 

## Getting Started

### Prerequisites

- `Java Development Kit 8`.
- An active Azure account. If you don't have one, you can sign up for a [free account](https://azure.microsoft.com/free/). Alternatively, you can use the [Azure Cosmos DB Emulator](https://docs.microsoft.com/en-us/azure/cosmos-db/local-emulator) for development and testing. As emulator https certificate is self signed, you need to import its certificate to java trusted cert store, [explained here](https://docs.microsoft.com/en-us/azure/cosmos-db/local-emulator-export-ssl-certificates).
- [Apache Maven](https://maven.apache.org/install.html).
- (Optional) SLF4J is a logging facade.
- (Optional) [SLF4J binding](http://www.slf4j.org/manual.html) is used to associate a specific logging framework with SLF4J.


SLF4J is only needed if you plan to use logging, please also download an SLF4J binding which will link the SLF4J API with the logging implementation of your choice. See the [SLF4J user manual](http://www.slf4j.org/manual.html) for more information.

### Quickstart

1. The app uses environment variables `ACCOUNT_HOST` and `ACCOUNT_KEY`. Make sure these environment variables exist, and are set to your Azure Cosmos DB account `URI` and `PRIMARY KEY` respectively.
1. git clone https://github.com/Azure-Samples/azure-spring-boot-samples.git
1. cd cosmos/azure-spring-data-cosmos/cosmos-multi-tenant-samples/tenant-by-container
1. start the application: `mvn spring-boot:run`
1. Send a request to the web service from a linux based command line (or you can use [postman](https://www.postman.com/downloads/)): `curl -s -d '{"firstName":"Theo","lastName":"van Kraay"}' -H "Content-Type: application/json" -H "TenantId: theo" -X POST http://localhost:8080/users`
1. Create an Order: `curl -s -d '{"orderDetails":"Order no.1","lastName":"van Kraay"}' -H "Content-Type: application/json" -H "TenantI
1. You should see containers named by each tenant created in Cosmos DB, with colocated entities of `User` and `Order`, identifiable by a `type` field in the document.


## Resources

Please refer to azure spring data cosmos for sql api [source code](https://github.com/Azure/azure-sdk-for-java/tree/master/sdk/cosmos) for more information.
