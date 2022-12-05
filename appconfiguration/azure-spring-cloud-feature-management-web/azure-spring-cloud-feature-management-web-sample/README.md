---
page_type: sample
languages:
- java
products:
- azure-app-configuration
name: Managing Features and Get Configurations From App Configuration in Spring Boot Web Application
description: This sample demonstrates how to manage features and how to get configurations from App Configuration to Spring Environment in Spring Boot web application.
---

# Managing Features and Get Configurations From App Configuration in Spring Boot Web Application

This sample describes how to use [azure-spring-cloud-feature-management](https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-cloud-feature-management_2.7.0/sdk/appconfiguration/azure-spring-cloud-feature-management/README.md) to manage features and how to get configurations from App Configuration Service to Spring Environment.

## Key concepts
## Getting started



### How to run without App Configuration Service
Start the application and check the resulting console output to check the returned value.

1. Load features from application.yml
```
mvn spring-boot:run
```

2. Check the returned value. The feature `Beta` has one filter `Random` which defines

### How to run with App Configuration Service

#### Prepare data

1. Create a Configuration Store if not exist.

2. Import the data file src/main/resources/data/sample-data.json into the Configuration Store created above. Under `For language` select `Other`. Under `File type` select `Yaml`.

#### Configure the bootstrap.yaml

Change the connection-string value with the Access Key value of the Configuration Store created above.

#### Run the application
Start the application and access http://localhost:8080 to check the returned value. Different commands for different scenarios are listed below.

1. Load properties similar with from application.properties, i.e., keys starting with /application/
```
mvn spring-boot:run
```

2. Load properties similar with from application_dev.properties, i.e., keys starting with /application_dev
```
mvn -Dspring.profiles.active=dev spring-boot:run
```

3. Load properties similar with from foo.properties, i.e., keys starting with /foo/
```
mvn -Dspring.application.name=foo spring-boot:run
```

4. Load properties similar with from foo_dev.properties, i.e., keys starting with /foo_dev/
```
mvn -Dspring.application.name=foo -Dspring.profiles.active=dev spring-boot:run
```

### More details

Please refer to this [README](https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-cloud-starter-appconfiguration-config_2.8.0/sdk/appconfiguration/azure-spring-cloud-starter-appconfiguration-config/README.md) about more usage details. 

## Examples
## Troubleshooting
## Next steps
## Contributing

<!-- LINKS -->

