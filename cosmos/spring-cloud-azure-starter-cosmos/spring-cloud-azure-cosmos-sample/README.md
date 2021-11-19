# Sample project for Spring Cloud Azure Starter Cosmos

## Key concepts


## Getting started



### Create an Azure Cosmos DB on Azure

1. Go to [Azure portal](https://portal.azure.com/) and click +New .
2. Click Databases, and then click Azure Cosmos DB to create your database. 
3. Navigate to the database you have created, and click Access keys and copy your URI and access keys for your database.
                                                                                                                                  
## Examples

### Config the sample

1. Navigate to `src/main/resources` and open `application.yml`.
2. replace below properties in `application.yml` with information of your database.

```yaml
spring:
  cloud:
    azure:
      cosmos:
        key: [your-cosmos-key]
        endpoint: [your-cosmos-endpoint]
```

### Run with Maven
```
cd azure-spring-boot-samples/cosmos/spring-cloud-azure-starter-cosmos/spring-cloud-azure-cosmos-sample
mvn spring-boot:run
```

## Troubleshooting


## Next steps


## Contributing

<!-- LINKS -->
