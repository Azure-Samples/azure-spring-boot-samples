---
page_type: sample
languages:
- java
products:
- azure-cosmos-db
name: Using Azure Cosmos DB By Spring Data in Spring Boot Application
description: This sample demonstrates how to use Azure Cosmos DB in Spring Boot application.
---

# Using Azure Cosmos DB By Spring Data in Spring Boot Application

* This sample uses Spring Boot 4.
Azure [Spring Data](https://spring.io/projects/spring-data) [Azure Cosmos DB](https://learn.microsoft.com/azure/cosmos-db/introduction) Quickstart sample project.

 
## Features

Basic sample code for getting started with [azure-spring-data-cosmos](https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring/azure-spring-data-cosmos) for Azure Cosmos DB SQL API.

## Getting Started

### Prerequisites

- `Java Development Kit 17` or higher.
- An active Azure account. If you don't have one, you can sign up for a [free account](https://azure.microsoft.com/free/). Alternatively, you can use the [Azure Cosmos DB Emulator](https://docs.microsoft.com/en-us/azure/cosmos-db/local-emulator) for development and testing. As emulator https certificate is self-signed, you need to import its certificate to java trusted cert store, [explained here](https://docs.microsoft.com/en-us/azure/cosmos-db/local-emulator-export-ssl-certificates)
- Maven.
- (Optional) SLF4J is a logging facade.
- (Optional) [SLF4J binding](http://www.slf4j.org/manual.html) is used to associate a specific logging framework with SLF4J. SLF4J is only needed if you plan to use logging, please also download an SLF4J binding which will link the SLF4J API with the logging implementation of your choice. See the [SLF4J user manual](http://www.slf4j.org/manual.html) for more information.


### Quickstart

1. git clone https://github.com/Azure-Samples/azure-spring-boot-samples.git
2. cd cosmos/azure-spring-data-cosmos/cosmos-quickstart-samples
3. Add environment variables `ACCOUNT_HOST` and `ACCOUNT_KEY` with Cosmos DB uri and primary key respectively (see `resources/application.yaml`).
4. mvn spring-boot:run

### Query timeout and availability

The existing application saves two users with different `lastName` partition-key values. Its
`findByFirstName("testFirstName")` calls demonstrate cross-partition queries through the synchronous
and reactive repositories. Timeout and availability settings belong on the shared client builder in
[SampleAppConfiguration.java](src/main/java/com/azure/spring/data/cosmos/samples/quickstart/SampleAppConfiguration.java);
the repository query methods do not need to change.

To try the policy, set these environment variables before `mvn spring-boot:run`, or put them in
`cosmos-v4.properties` in your user home directory, outside the checkout:

```properties
ACCOUNT_HOST=https://<account-name>.documents.azure.com:443/
ACCOUNT_KEY=<account-key>
COSMOS_DATABASE=cosmos-availability-demo
PREFERRED_LOCATIONS=East US,West US
COSMOS_END_TO_END_TIMEOUT=PT5S
COSMOS_AVAILABILITY_THRESHOLD=PT0.5S
COSMOS_AVAILABILITY_THRESHOLD_STEP=PT0.1S
```

Replace the account values and region names with your own. Use the global account endpoint and at least
two readable account regions to benefit from cross-region hedging; a single-region account or emulator
can still run the queries. Do not commit credentials. Environment variables override the imported file.
Use a disposable database: the existing quickstart deletes all items from `myContainer` at startup,
uses fixed sample IDs, and leaves its database/container and sample items in place afterward.
Azure resources can incur charges.

`COSMOS_END_TO_END_TIMEOUT` opts into the client-wide policy; leaving it unset retains the SDK's existing
timeout/availability defaults. Durations use ISO-8601 syntax, and the timeout must exceed the initial
availability threshold. With the example above, a slow request can be hedged after 500 ms, with further
regions considered in 100 ms steps. The same client settings apply to both repositories.

The SDK query-page timeout is not a deadline for query-plan acquisition plus complete multi-page
consumption. Applications requiring that broader deadline must also handle application-level
timeout/cancellation. Query-plan metadata still uses the gateway in Direct mode, so allow HTTPS
connectivity as well as Direct-mode connectivity. A healthy run demonstrates the configuration and
query results; it does not prove that hedging occurred.

Build with `mvn clean verify` and run with `mvn spring-boot:run`. See the public
[threshold-based availability strategy guide](https://learn.microsoft.com/azure/cosmos-db/performance-tips-java-sdk-v4#threshold-based-availability-strategy)
for the hedging timeline and cost considerations.

## Resources

Please refer to azure spring data cosmos for sql api [azure-spring-data-cosmos source code](https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring/azure-spring-data-cosmos) for more information.

## Deploy to Azure Spring Apps

Now that you have the Spring Boot application running locally, it's time to move it to production. [Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/overview) makes it easy to deploy Spring Boot applications to Azure without any code changes. The service manages the infrastructure of Spring applications so developers can focus on their code. Azure Spring Apps provides lifecycle management using comprehensive monitoring and diagnostics, configuration management, service discovery, CI/CD integration, blue-green deployments, and more. To deploy your application to Azure Spring Apps, see [Deploy your first application to Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/quickstart?tabs=Azure-CLI).
