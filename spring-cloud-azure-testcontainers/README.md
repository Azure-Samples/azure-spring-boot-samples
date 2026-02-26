---
page_type: sample
languages:
- java
products:
- spring-cloud-azure-testcontainers
name: Spring Cloud Azure Testcontainers samples
description: These samples demonstrates how to use Spring Cloud Azure Testcontainers in test cases.
---

# Testcontainers Service Connection Samples for Spring Cloud Azure
Testcontainers is an open source framework for providing throwaway, lightweight instances of databases, message brokers, web browsers, or just about anything that can run in a Docker container.

We provide `spring-cloud-azure-testcontainers` library to support Testcontainers in Spring Cloud Azure. It allows you to write a test class that can start up a container before any of the tests run. Testcontainers is especially useful for writing integration tests that talk to a real backend service.

This sample project demonstrates how to use Testcontainers Service Connection with Azure Cosmos DB, Azure Storage Blob, and Azure Storage Queue in test cases. 

## What You Need

- [Docker environment](https://java.testcontainers.org/supported_docker_environment/)
- [JDK8](https://www.oracle.com/java/technologies/downloads/) or later
- Maven
- You can also import the code straight into your IDE:
    - [IntelliJ IDEA](https://www.jetbrains.com/idea/download)

## Run Locally
With docker environment running, you can directly run the tests.

### Run the sample with Maven

In your terminal, run `mvn clean test`.

```shell
mvn clean test
```

### Run the sample in IDEs

You can debug your sample by IDEs.

* If your tool is `IDEA`, please refer to [Debug your first Java application](https://www.jetbrains.com/help/idea/debugging-your-first-java-application.html) and [add environment variables](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html#add-environment-variables).

* If your tool is `ECLIPSE`, please refer to [Debugging the Eclipse IDE for Java Developers](https://www.eclipse.org/community/eclipse_newsletter/2017/june/article1.php) and [Eclipse Environment Variable Setup](https://examples.javacodegeeks.com/desktop-java/ide/eclipse/eclipse-environment-variable-setup-example).
