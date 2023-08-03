---
page_type: sample
languages:
- java
products:
- cognitive-services
name: Use Azure OpenAI to generate completions in a Spring Boot application
description: This sample demonstrates how to use Azure OpenAI to generate completions in a Spring Boot application.
---

# Use Azure OpenAI to generate completions in a Spring Boot application

This sample demonstrates how to use Azure OpenAI to generate completions.

## What You Will Build
You will build an application with Azure OpenAI to generate completions.

## What You Need

- [An Azure subscription](https://azure.microsoft.com/free/)
- [JDK8](https://www.oracle.com/java/technologies/downloads/) or later
- Maven
- An Azure OpenAI instance. If you don't have one, see [Create a resource and deploy a model using Azure OpenAI](https://learn.microsoft.com/azure/cognitive-services/openai/how-to/create-resource).
- You can also import the code straight into your IDE:
    - [IntelliJ IDEA](https://www.jetbrains.com/idea/download)

## Configure the sample

```yaml
spring:
  cloud:
    azure:
      openai:
        endpoint: ${AZURE_OPENAI_ENDPOINT}
        service-version: ${AZURE_OPENAI_SERVICE_VERSION}
        key: ${AZURE_OPENAI_KEY}
```

## Run Locally

### Run the sample with Maven

In your terminal, run `mvn clean spring-boot:run`.

```shell
mvn clean spring-boot:run
```

### Run the sample in IDEs

You can debug your sample by adding the saved output values to the tool's environment variables or the sample's `application.yaml` file. 

* If your tool is `IDEA`, please refer to [Debug your first Java application](https://www.jetbrains.com/help/idea/debugging-your-first-java-application.html) and [add environment variables](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html#add-environment-variables). 

* If your tool is `ECLIPSE`, please refer to [Debugging the Eclipse IDE for Java Developers](https://www.eclipse.org/community/eclipse_newsletter/2017/june/article1.php) and [Eclipse Environment Variable Setup](https://examples.javacodegeeks.com/desktop-java/ide/eclipse/eclipse-environment-variable-setup-example/). 

## Verify This Sample

1. Send a POST request

```shell
curl http://localhost:8080/prompt -H "Content-Type: text/plain" -d "When was Microsoft founded?"
```

2. Verify in your app’s logs that similar messages were posted:

```shell
Text:  Microsoft was founded on April 4, 1975 by Bill Gates and Paul Allen in Albuquerque, New Mexico.
```

## Deploy to Azure Spring Apps

Now that you have the Spring Boot application running locally, it's time to move it to production. [Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/overview) makes it easy to deploy Spring Boot applications to Azure without any code changes. The service manages the infrastructure of Spring applications so developers can focus on their code. Azure Spring Apps provides lifecycle management using comprehensive monitoring and diagnostics, configuration management, service discovery, CI/CD integration, blue-green deployments, and more. To deploy your application to Azure Spring Apps, see [Deploy your first application to Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/quickstart?tabs=Azure-CLI).
