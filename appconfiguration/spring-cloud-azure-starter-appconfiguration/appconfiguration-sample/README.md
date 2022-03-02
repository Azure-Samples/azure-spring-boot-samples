# Spring Cloud Azure Config Sample client library for Java

This sample describes how to use [spring-cloud-azure-starter-appconfiguration](https://github.com/Azure/azure-sdk-for-java/tree/feature/azure-spring-cloud-4.0/sdk/spring/spring-cloud-azure-starter-appconfiguration) to load configuration keys from App Configuration.

## Key concepts
## Getting started

### How to run

#### Prepare data

1. Create a Configuration Store if not exist.

2. Import the data file src/main/resources/data/sample-data.json into the Configuration Store created above. Keep the default options as-is when importing json data file.

#### Configure the bootstrap.yaml

Change the connection-string value with the Access Key value of the Configuration Store created above.

#### Run the application
Start the application, and a message similar to the following will be printed out.

```text
INFO 15116 --- [           main] .s.s.a.AppConfigurationSampleApplication : Returned the from Azure App Configuration: sample-key, hello from default application
```

## Examples
## Troubleshooting
## Next steps
## Contributing

<!-- LINKS -->

