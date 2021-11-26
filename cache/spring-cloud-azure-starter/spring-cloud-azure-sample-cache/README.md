# Spring Cloud Azure Sample redis cache

## Key concepts

Redis support is based on spring-boot-starter-data-redis, which obtains and
Automatic configuration of redis properties through Azure Redis Cache Management SDK.


## Getting started

Running this sample will be charged by Azure. You can check the usage and bill at [this link][azure-account].



### Coordinates

Maven coordinates:

```xml
<dependency>
  <groupId>com.azure.spring</groupId>
  <artifactId>spring-cloud-azure-starter</artifactId>
</dependency>
<dependency>
  <groupId>com.azure.spring</groupId>
  <artifactId>spring-cloud-azure-resourcemanager</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### Create an Azure Cache for Redis instance

1. Create a service principal for use in by your app. Please follow [create service principal from Azure CLI][create-sp-using-azure-cli].

1. Create an Azure Cache for Redis instance. Please follow [create-azure-cache-for-redis].


## Examples

1.  Update `src/main/resources/application.yaml` to specify
    resource group, service principal, and cache instance name:

    ```yaml
    spring:
      cloud:
        azure:
          credential:
            client-id: [service-principal-id]
            client-secret: [service-principal-secret]
          profile:
            tenant-id: [tenant-id]
            subscription-id: [subscription-id]
          redis:
            name: [azure-cache-for-redis-instance-name]
            resource:
              resource-group: [resource-group]
    ```
    > :notes: Please note that currently we do not support the automatic creation of Azure Cache resources.
    
1.  Run the application using the `$ mvn spring-boot:run` command.

1.  Send a GET request to check, where `name` could be any string:

        $ curl -XGET http://localhost:8080/{name}

1.  Confirm from Azure Redis Cache console in Azure Portal

        $ keys *

1.  Delete the resources on [Azure Portal][azure-portal] to avoid unexpected charges.


## Troubleshooting

## Next steps

## Contributing

<!-- LINKS -->
[azure-account]: https://azure.microsoft.com/account/
[azure-portal]: https://ms.portal.azure.com/
[create-azure-cache-for-redis]: https://docs.microsoft.com/azure/azure-cache-for-redis/quickstart-create-redis
[create-sp-using-azure-cli]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/create-sp-using-azure-cli.md
