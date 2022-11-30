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

Azure [Spring Data](https://spring.io/projects/spring-data) [Azure Cosmos DB](https://learn.microsoft.com/azure/cosmos-db/introduction) Quickstart sample project.

 
## Features

Basic sample code for getting started with [azure-spring-data-cosmos](https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/cosmos/azure-spring-data-cosmos) for Azure Cosmos DB SQL API.

## Getting Started

### Prerequisites

- `Java Development Kit 8` or higher. 
- An active Azure account. If you don't have one, you can sign up for a [free account](https://azure.microsoft.com/free/). Alternatively, you can use the [Azure Cosmos DB Emulator](https://docs.microsoft.com/en-us/azure/cosmos-db/local-emulator) for development and testing. As emulator https certificate is self-signed, you need to import its certificate to java trusted cert store, [explained here](https://docs.microsoft.com/en-us/azure/cosmos-db/local-emulator-export-ssl-certificates)
- Maven.
- (Optional) SLF4J is a logging facade.
- (Optional) [SLF4J binding](http://www.slf4j.org/manual.html) is used to associate a specific logging framework with SLF4J. SLF4J is only needed if you plan to use logging, please also download an SLF4J binding which will link the SLF4J API with the logging implementation of your choice. See the [SLF4J user manual](http://www.slf4j.org/manual.html) for more information.


### Quickstart

1. git clone https://github.com/Azure-Samples/azure-spring-boot-samples.git
2. cd cosmos/azure-spring-data-cosmos/cosmos-quickstart-samples
3. Add environment variables `ACCOUNT_HOST` and `ACCOUNT_KEY` with Cosmos DB uri and primary key respectively (see `resources/application.yaml`).
4. mvn spring-boot:run

## Resources

Please refer to azure spring data cosmos for sql api [azure-spring-data-cosmos source code](https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/cosmos/azure-spring-data-cosmos) for more information.
