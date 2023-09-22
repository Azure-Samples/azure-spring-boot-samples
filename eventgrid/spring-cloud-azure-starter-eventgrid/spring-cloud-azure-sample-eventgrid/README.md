---
page_type: sample
languages:
- java
products:
- event-grid
name: Use EventGrid to send EventGridEvent and use Service Bus Queue as Event Handler to receive in a Spring Boot application.
description: This sample demonstrates how to use EventGrid to send EventGridEvent and use Service Bus Queue as Event Handler to receive in a Spring Boot application.
---

# Use EventGrid to send EventGridEvent and use Service Bus Queue as Event Handler to receive in a Spring Boot application

This sample demonstrates how to use EventGrid to send EventGridEvent to a topic and use Service Bus Queue as Event Handler to receive.

## What You Will Build
You will build an application with EventGrid to send EventGridEvent to a topic and use Service Bus Queue as Event Handler to receive.

## What You Need

- [An Azure subscription](https://azure.microsoft.com/free/)
- [JDK8](https://www.oracle.com/java/technologies/downloads/) or later
- Maven
- An Event Grid Topic instance. If you don't have one, see [Create a custom topic or a domain in Azure Event Grid](https://learn.microsoft.com/azure/event-grid/create-custom-topic).
- A Service Bus Queue instance. If you don't have one, see [Create a queue in the Azure portal](https://docs.microsoft.com/azure/service-bus-messaging/service-bus-quickstart-portal).
- You can also import the code straight into your IDE:
    - [IntelliJ IDEA](https://www.jetbrains.com/idea/download)

## Subscribe to custom topic

Use the following steps to create an event subscription to tell the Event Grid to send events to the Service Bus Queue:

1. In the Azure portal, navigate to your Event Grid Topic instance.
1. Select **Event Subscriptions** on the toolbar.
1. On the **Create Event Subscription page**, enter a **name** value for the event subscription.
1. For **Endpoint Type**, select **Service Bus Queue**.
1. Choose **Select an endpoint** and then select the Service Bus Queue instance you created earlier.

## Configure the sample

```yaml
spring:
  cloud:
    azure:
      eventgrid:
        endpoint: ${AZURE_EVENTGRID_ENDPOINT}
        key: ${AZURE_EVENTGRID_KEY}
      servicebus:
        connection-string: ${AZURE_SERVICEBUS_CONNECTION_STRING}
    function:
      definition: consume
    stream:
      bindings:
        consume-in-0:
          destination: ${AZURE_SERVICEBUS_QUEUE_NAME}
      servicebus:
        bindings:
          consume-in-0:
            consumer:
              auto-complete: false
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
curl http://localhost:8080/publish
```

2. Verify in your app’s logs that similar messages were posted:

```text
New event received: '"FirstName: John, LastName: James"'
```

## Deploy to Azure Spring Apps

Now that you have the Spring Boot application running locally, it's time to move it to production. [Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/overview) makes it easy to deploy Spring Boot applications to Azure without any code changes. The service manages the infrastructure of Spring applications so developers can focus on their code. Azure Spring Apps provides lifecycle management using comprehensive monitoring and diagnostics, configuration management, service discovery, CI/CD integration, blue-green deployments, and more. To deploy your application to Azure Spring Apps, see [Deploy your first application to Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/quickstart?tabs=Azure-CLI).
