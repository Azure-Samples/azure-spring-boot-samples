---
page_type: sample
languages:
- java
products:
- azure-app-configuration
name: Loading Configuration Properties From App Configuration in Spring Boot Application
description: This sample demonstrates how to load configuration properties from App Configuration to Spring Environment in Spring Boot application.
---

# Loading Configuration Properties From App Configuration in Spring Boot Application

This sample describes how to use [spring-cloud-azure-starter-appconfiguration-config](https://github.com/Azure/azure-sdk-for-java/blob/main/sdk/spring/spring-cloud-azure-starter-appconfiguration-config/README.md) to load configuration properties from App Configuration Service to Spring Environment.

## Key concepts
## Getting started



### How to run

#### Prepare data

1. Create a Configuration Store if not exist.

2. Import the data file src/main/resources/data/sample-data.json into the Configuration Store created above. Keep the default options as-is when importing json data file.

#### Configure the bootstrap.yaml

Change the connection-string value with the Access Key value of the Configuration Store created above.

#### Run the application
Start the application and access http://localhost:8080 to check the returned value. Different commands for different scenarios are listed below.

1. Load properties similar with from application.properties, i.e., keys starting with /application/

```console
mvn spring-boot:run
```

1. Load properties similar with from application_dev.properties, i.e., keys starting with /application_dev

```console
mvn -Dspring.profiles.active=dev spring-boot:run
```

1. Load properties similar with from foo.properties, i.e., keys starting with /foo/

```console
mvn -Dspring.application.name=foo spring-boot:run
```

1. Load properties similar with from foo_dev.properties, i.e., keys starting with /foo_dev/

```console
mvn -Dspring.application.name=foo -Dspring.profiles.active=dev spring-boot:run
```

### More details

Please refer to this [README](https://github.com/Azure/azure-sdk-for-java/blob/main/sdk/spring/spring-cloud-azure-starter-appconfiguration-config/README.md) about more usage details. 

## Deploy to Azure Spring Apps

Now that you have the Spring Boot application running locally, it's time to move it to production. [Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/overview) makes it easy to deploy Spring Boot applications to Azure without any code changes. The service manages the infrastructure of Spring applications so developers can focus on their code. Azure Spring Apps provides lifecycle management using comprehensive monitoring and diagnostics, configuration management, service discovery, CI/CD integration, blue-green deployments, and more. To deploy your application to Azure Spring Apps, see [Deploy your first application to Azure Spring Apps](https://learn.microsoft.com/azure/spring-apps/quickstart?tabs=Azure-CLI).

## Examples
## Troubleshooting
## Next steps
## Contributing

<!-- LINKS -->

