---
page_type: sample
languages:
- java
products:
- azure-app-configuration
name: Refreshing Configuration Properties From App Configuration in Spring Boot Application
description: This sample demonstrates how to refresh configuration properties from App Configuration in Spring Boot application.
---

# Refreshing Configuration Properties From App Configuration in Spring Boot Application

## Prerequisite

* A [Java Development Kit (JDK)](https://docs.microsoft.com/java/azure/jdk/?view=azure-java-stable), version 8.
* [Apache Maven](http://maven.apache.org/), version 3.0 or later.

## How to run

### Setup your App Configuration Store

1. To create your Azure App Configuration store, you can use:

    ```azurecli
    az appconfig create --resource-group <your-resource-group> --name <name-of-your-new-store> --sku Standard
    ```

1. Create the test key in your new store:

    ```azurecli
    az appconfig kv set --key /application/config.message --value testKey --name <name-of-your-new-store> --yes
    ```

1. Create monitor trigger.

    ```azurecli
    az appconfig kv set --key sentinel --value 1 --name <name-of-your-new-store> --yes
    ```

This value should match the `spring.cloud.azure.appconfiguration.stores[0].monitoring.triggers[0].key` value in `bootstrap.properties`.

### Run the application

1. Build the application

    ```console
    mvn clean package
    ```

1. Run the application

    ```console
    mvn spring-boot:run
    ```

1. Go to `localhost:8080` which will display the value `testKey`.

1. Update key to new value.

    ```azurecli
    az appconfig kv set --key /application/config.message --value updatedTestKey --name <name-of-your-new-store> --yes
    ```

1. Update monitor trigger, to trigger refresh.

    ```azurecli
    az appconfig kv set --key sentinel --value 2 --name <name-of-your-new-store> --yes
    ```

1. Refresh page, this will trigger the refresh update.

1. After a couple seconds refresh again, this time the new value `updatedTestKey` will show.

## Deploy to Azure Spring Apps

Now that you have the Spring Boot application running locally, it's time to move it to production. [Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/overview) makes it easy to deploy Spring Boot applications to Azure without any code changes. The service manages the infrastructure of Spring applications so developers can focus on their code. Azure Spring Apps provides lifecycle management using comprehensive monitoring and diagnostics, configuration management, service discovery, CI/CD integration, blue-green deployments, and more. To deploy your application to Azure Spring Apps, see [Deploy your first application to Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/quickstart?tabs=Azure-CLI).
