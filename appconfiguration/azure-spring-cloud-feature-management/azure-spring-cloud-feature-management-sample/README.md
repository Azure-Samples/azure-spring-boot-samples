---
page_type: sample
languages:
- java
products:
- azure-app-configuration
name: Managing Features and Get Configurations From App Configuration in Spring Boot Application
description: This sample demonstrates how to manage features and how to get configurations from App Configuration to Spring Environment in Spring Boot application.
---

# Managing Features and Get Configurations From App Configuration in Spring Boot Application

This sample describes how to use [azure-spring-cloud-feature-management](https://github.com/Azure/azure-sdk-for-java/tree/azure-spring-cloud-feature-management_2.7.0/sdk/appconfiguration/azure-spring-cloud-feature-management/README.md) to manage features and how to get configurations from App Configuration Service to Spring Environment.

## Key concepts
## Getting started

`azure-spring-cloud-feature-management` doesn't require use of the App Configuration service, but can be integrated with it. The next section shows how to use the library without the App Configuration service, the section after shows how to update the example to use it with the App Configuration service.

### How to run without App Configuration Service
Start the application and check the resulting console output to check the returned value.

1. Load features from application.yml
```
mvn spring-boot:run
```

1. Check the returned value. The feature `Beta` has one filter `Random` which defines the Beta feature will be activated randomly with some chance value. You should see the following information displayed: **RUNNING : application** or **RUNNING : beta**.

### How to run with Azure Configuration Service

#### Prepare data

1. Create a Configuration Store if not exist.

```azurecli
az appconfig create --resource-group <your-resource-group> --name <name-of-your-new-store> --sku Standard
```

1. Import the data file src/main/resources/data/sample-data.json into the Configuration Store created above. Under `For language` select `Other`. Under `File type` select `Yaml` or using the azure cli:

```azurecli
az appconfig kv import -n <name-of-your-new-store> -s file --path <location-of-your-properties-file> --format properties --prefix /application/
```

It will have you confirm the feature flag before loading it.

#### Updating the application

1. Add the azure-spring-cloud-appconfiguration-config dependency,

```xml
<dependency>
    <groupId>com.azure.spring</groupId>
    <artifactId>azure-spring-cloud-appconfiguration-config</artifactId>
    <version>2.8.0</version>
</dependency>
```

2. Create a file named bootstrap.properties in the resources folder and add the connection string to it.

```properties
spring.cloud.azure.appconfiguration.stores[0].connection-string= <your-connection-string>
```

Note: You can get your connection string from the portal from Access Keys or using the cli:

```azurecli
az appconfig credential list --resource-group <your-resource-group> --name <name-of-your-new-store>
```

3. Delete application.yml.

#### Run the application

1. Load features from application.yml
```
mvn spring-boot:run
```

1. Check the returned value. The feature `Beta` has one filter `Random` which defines the Beta feature will be activated randomly with some chance value. You should see the following information displayed: **RUNNING : application** or **RUNNING : beta**.

### More details

Please refer to this [README](https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-cloud-starter-appconfiguration-config_2.8.0/sdk/appconfiguration/azure-spring-cloud-starter-appconfiguration-config/README.md) about more usage details. 

## Examples
## Troubleshooting
## Next steps
## Contributing

<!-- LINKS -->

