# Deploy Spring Microservices using Azure Spring starters for Cosmos DB, Redis, Key Vault

Azure Spring Cloud enables you to easily run a Spring Boot based microservices application on Azure.

This quickstart shows you how to deploy an existing Java Spring Cloud application to Azure. When
you're finished, you can continue to manage the application via the Azure CLI or switch to using the
Azure portal.

## What will you experience

You will:

- Build existing Spring microservices applications
- Provision azure resources required for the application
- Run the application locally with Cosmos DB backend, Azure Redis Cache and using Key Vault for
  storing secrets
- Open the application

## What you will need

In order to deploy a Java app to cloud, you need an Azure subscription. If you do not already have
an Azure subscription, you can activate your
[MSDN subscriber benefits](https://azure.microsoft.com/pricing/member-offers/msdn-benefits-details/)
or sign up for a
[free Azure account]((https://azure.microsoft.com/free/)).

In addition, you will need the following:

- [Azure CLI version 2.0.67 or higher](https://docs.microsoft.com/cli/azure/install-azure-cli?view=azure-cli-latest)
- [Java 8](https://www.azul.com/downloads/azure-only/zulu/?version=java-8-lts&architecture=x86-64-bit&package=jdk)
- [Maven](https://maven.apache.org/download.cgi)
- [MySQL CLI](https://dev.mysql.com/downloads/shell/)
- [Git](https://git-scm.com/)
- [Jq](https://stedolan.github.io/jq/)

## Clone and build the repo

### Clone the sample app repository 

```bash
    git clone https://github.com/Azure-Samples/azure-spring-boot-samples
```

### Change directory and build the project

```bash
    cd azure-spring-boot-samples/spring-petclinic-microservices
    mvn clean package -DskipTests
```

This will take a few minutes.

## Provision Azure resources using Azure CLI

### Login to Azure

Login to the Azure CLI and choose your active subscription. Be sure to choose the active
subscription that is whitelisted for Azure Spring Cloud

```bash
    az login
    az account list -o table
    az account set --subscription ${SUBSCRIPTION}
```

### Prepare your environment for deployments

Create a bash script with environment variables by making a copy of the supplied template:

```bash
    cp .scripts/setup-env-variables-azure-template.sh .scripts/setup-env-variables-azure.sh
```

Open `.scripts/setup-env-variables-azure.sh` and enter the following information:

```bash

export RESOURCE_GROUP=resource-group-name # customize this
export LOCATION=SouthCentralUS  #customize this
export COSMOSDB_NAME=mycosmosdbaccname  # customize this
export REDIS_NAME=myredisname #customize this
export KEYVAULT_NAME=myend2endkv #customize this
export APP_NAME_FOR_KEYVAULT=myappforkeyvault #customize this
    
```

Then, set the environment:

```bash
    source .scripts/setup-env-variables-azure.sh
```

make sure keyvault.env file is created at the root of the repo.

## Starting services locally with docker-compose

In order to start entire infrastructure using Docker, you have to build images by
executing command below from a project root:

```shell
mvn clean install -P buildDocker -DskipTests
```


Once images are ready, you can start them with a single command.


```shell
docker-compose up
```

Containers startup order is coordinated with [`dockerize` script](https://github.com/jwilder/dockerize). After starting services it takes a while for API Gateway to be in sync with service registry, so don't be scared of initial Spring
Cloud Gateway timeouts.   
You can track services availability using Eureka dashboard available by default at `http://localhost:8761`.

## Understanding the Spring Petclinic application

[See the presentation of the Spring Petclinic Framework version](http://fr.slideshare.net/AntoineRey/spring-framework-petclinic-sample-application)

[A blog bost introducing the Spring Petclinic Microsevices](http://javaetmoi.com/2018/10/architecture-microservices-avec-spring-cloud/) (
french language)

You can then access petclinic here: http://localhost:8080/

![Spring Petclinic Microservices screenshot](docs/application-screenshot.png)

**Architecture diagram of the Spring Petclinic Microservices with Cosmos DB**

![Spring Petclinic Microservices architecture](docs/microservices-architecture-diagram-cosmosdb.jpg)

## Navigate to the application

The application could be reached at http://localhost:8080
![](./media/petclinic.jpg)

## Enabling Spring boot starter for Azure Active directory (Optional)

To secure the Java applications in this sample please follow
the [Spring Security Azure Active Directory tutorial](https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-active-directory)
. After setting your Active directory you can enable security on Customers service by uncommenting
the relevant AAD sample code.

## Next Steps

In this quickstart, you've deployed an existing Spring microservices app using Azure CLI. To learn
more about Spring on Azure, go to:

- [Spring on Azure](https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/)
- [Azure for Java Cloud Developers](https://docs.microsoft.com/en-us/azure/java/)

## Credits

This Spring microservices sample is forked from
[spring-petclinic/spring-petclinic-microservices](https://github.com/spring-petclinic/spring-petclinic-microservices)

## Contributing

This project welcomes contributions and suggestions. Most contributions require you to agree to a
Contributor License Agreement (CLA) declaring that you have the right to, and actually do, grant us
the rights to use your contribution. For details, visit https://cla.opensource.microsoft.com.

When you submit a pull request, a CLA bot will automatically determine whether you need to provide a
CLA and decorate the PR appropriately (e.g., status check, comment). Simply follow the instructions
provided by the bot. You will only need to do this once across all repos using our CLA.

This project has adopted
the [Microsoft Open Source Code of Conduct](https://opensource.microsoft.com/codeofconduct/). For
more information see the [Code of Conduct FAQ](https://opensource.microsoft.com/codeofconduct/faq/)
or contact [opencode@microsoft.com](mailto:opencode@microsoft.com) with any additional questions or
comments.
