# Azure Cosmos DB Spring Data - Multi Tenant by Container

[Spring Data Cosmos](https://aka.ms/SpringDataCosmos) sample for a multi-tenanted app where each tenant has its own [Azure Cosmos DB](https://learn.microsoft.com/azure/cosmos-db/introduction) [container](https://learn.microsoft.com/azure/cosmos-db/resource-model#azure-cosmos-db-containers).

This sample application fetches the value of the tenant from request header (TenantId). In a real-world application, it is up to you how to identify this while keeping your application secure. For example, you may want to fetch the identifier from a cookie, or other header name. The approach of assigning a container (or database) to each tenant may be useful if it is necessary to strictly isolate performance for each tenant. However, you should consider the trade-offs in taking this approach. Review our article on [Multitenancy and Azure Cosmos DB](https://learn.microsoft.com/azure/architecture/guide/multitenant/service/cosmos-db) for more guidance.

**Note**: this is a contrived sample in terms of the data model, and does not reflect any recommendations for how to model any *application* in particular. It is intended purely to illustrate how to maintain a container-per-tenant isolation model using Spring Data for Azure Cosmos DB, where containers for each tenant are created/referenced dynamically (on-the-fly) rather than being hardcoded at project startup.

## Prerequisites
- `Java Development Kit 8`.
- [Apache Maven](https://maven.apache.org/install.html).

## Create Azure Resources
- Create an active Azure account. If you don't have one, you can sign up for a [free account](https://azure.microsoft.com/free/). Alternatively, you can use the [Azure Cosmos DB Emulator](https://docs.microsoft.com/en-us/azure/cosmos-db/local-emulator) for development and testing. As emulator https certificate is self signed, you need to import its certificate to java trusted cert store, [explained here](https://docs.microsoft.com/en-us/azure/cosmos-db/local-emulator-export-ssl-certificates).
- [Apache Maven](https://maven.apache.org/install.html).

## Update environment variables
1. The app uses environment variables `ACCOUNT_HOST` and `ACCOUNT_KEY`. Make sure these environment variables exist, and are set to your Azure Cosmos DB account `URI` and `PRIMARY KEY` respectively.

## Run application
1. git clone https://github.com/Azure-Samples/azure-spring-boot-samples.git
1. cd cosmos/azure-spring-data-cosmos/cosmos-multi-tenant-samples/tenant-by-container
1. start the application: `mvn spring-boot:run`
1. Send a request to the web service from a linux based command line (or you can use [postman](https://www.postman.com/downloads/)): `curl -s -d '{"firstName":"Theo","lastName":"van Kraay"}' -H "Content-Type: application/json" -H "TenantId: theo" -H "TenantTier: standard" -X POST http://localhost:8080/users`
1. Create an Order: `curl -s -d '{"orderDetail":"Order no.1","lastName":"Brown"}' -H "Content-Type: application/json" -H "TenantId: mark" -H "TenantTier: premium" -X POST http://localhost:8080/orders`
1. You should see containers named by each tenant created in Cosmos DB, with co-located entities of `User` and `Order`, identifiable by a `type` field in the document.

## Sample investigation
- The application is a simple CRUD REST web service which creates `User` and `Order` entries in each tenant (co-located in the same container), and makes use of  `azure-spring-data-cosmos` for Azure Cosmos DB [NoSQL](https://learn.microsoft.com/azure/cosmos-db/nosql/) API.
- The application captures a http request header of `TenantId`. This is used to check if the corresponding tenant id exists. If it does not, the container will be created.
- The application also captures a http request header of `TenantTier`. This is used to determine whether to set dedicated throughput for the container ("premium") or use the shared throughput for the database.
- The Java classes with functionality for capturing `TenantId` and `TenantTier`, and referencing and/or creating tenant containers by `TenantId`, are in the [tenant folder](./src/main/java/com/azure/spring/data/cosmos/example/tenant).
- CRUD operations are performed in `UserController` and `OrderController` using corresponding Spring `Repository` APIs. The two entities `Order` and `User` are co-located in the same container, and differentiated using a `type` field in the record.

## Next steps
Please refer to azure spring data cosmos for NoSql api [source code](https://aka.ms/SpringDataCosmos) for more information.
