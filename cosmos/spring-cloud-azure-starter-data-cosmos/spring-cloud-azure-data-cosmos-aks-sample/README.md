---
page_type: sample
languages:
- java
products:
- azure-cosmos-db
name: Using Azure Cosmos DB in Spring Boot Application And Deploy To Azure Kubernetes Service
description: This sample demonstrates how to use Azure Cosmos DB in Spring Boot application and deploy to AKS.
---

# Using Azure Cosmos DB in Spring Boot Application And Deploy To Azure Kubernetes Service

In this tutorial, you will setup and build a Spring Boot application to perform operations on data in an [Azure Cosmos DB](https://learn.microsoft.com/azure/cosmos-db/introduction) SQL API account. You will then package the image using [Docker](https://docs.docker.com/), push it to [Azure Container Registry](https://azure.microsoft.com/products/container-registry/). Finally, you will deploy to [Azure Kubernetes Service](https://azure.microsoft.com/products/kubernetes-service/) and access the REST APIs exposed by the application.

## Pre-requisites

- An Azure account with an active subscription. Create a [free account](https://azure.microsoft.com/free/?ref=microsoft.com&utm_source=microsoft.com&utm_medium=docs&utm_campaign=visualstudio) or [try Azure Cosmos DB for free](https://azure.microsoft.com/try/cosmosdb/) without an Azure subscription.
- [Java Development Kit (JDK) 8](https://www.azul.com/downloads/azure-only/zulu/?&version=java-8-lts&architecture=x86-64-bit&package=jdk). Point your `JAVA_HOME` environment variable to the path where the JDK is installed.
- [Azure CLI](https://docs.microsoft.com/cli/azure/install-azure-cli) to provision Azure services.
- [Docker](https://docs.docker.com/engine/install/) to manage images and containers.
- A recent version of [Maven](https://maven.apache.org/download.cgi).
- [Git](https://www.git-scm.com/downloads).
- [curl](https://curl.se/download.html) to invoke REST APIs exposed the applications.

## Create a resource group for the Azure resources used in this tutorial

1. Sign in to your Azure account using Azure CLI:

   ```azurecli
   az login
   ```

1. Choose your Azure Subscription:

   ```azurecli
   az account set -s <enter subscription ID>
   ```

1. Create a resource group.

   ```azurecli
   az group create --name=cosmosdb-springboot-aks-rg --location=eastus
   ```

## Create an Azure Cosmos DB SQL API database account

Use this command to create an [Azure Cosmos DB SQL API database account](manage-with-clihttps://learn.microsoft.com/azure/cosmos-db/nosql/manage-with-cli#create-an-azure-cosmos-db-account) using the Azure CLI.

```azurecli
az cosmosdb create --name <enter account name> --resource-group <enter resource group name>
```

## Create a private Azure Container Registry using the Azure CLI

> [!NOTE]
> Replace `cosmosdbspringbootregistry` with a unique name for your registry.

```azurecli
az acr create --resource-group cosmosdb-springboot-aks-rg --location eastus \
    --name cosmosdbspringbootregistry --sku Basic
```

## Create an AKS cluster using the Azure CLI

1. The following command creates a Kubernetes cluster in the *cosmosdb-springboot-aks-rg* resource group, with *cosmosdb-springboot-aks* as the cluster name, with Azure Container Registry (ACR) `cosmosdbspringbootregistry` attached:

    ```azurecli
    az aks create \
        --resource-group cosmosdb-springboot-aks-rg \
        --name cosmosdb-springboot-aks \
        --node-count 1 \
        --generate-ssh-keys \
        --attach-acr cosmosdbspringbootregistry \
        --dns-name-prefix=cosmosdb-springboot-aks-app
    ```

    This command may take a while to complete.

1. If you don't have `kubectl` installed, you can do so using the Azure CLI.

   ```azurecli
   az aks install-cli
   ```

1. Download the AKS cluster configuration information.

   ```azurecli
   az aks get-credentials --resource-group=cosmosdb-springboot-aks-rg --name=cosmosdb-springboot-aks
   
   kubectl get nodes
   ```

## Build the application

1. Clone the application and change into the right directory.

    ```bash
    git clone https://github.com/Azure-Samples/azure-spring-boot-samples.git
    
    cd cosmos/spring-cloud-azure-starter-data-cosmos/spring-cloud-azure-data-cosmos-aks-sample
    ```

1. Use Maven to build the application.

   ```bash
   mvn clean install
   ```

## Run the application locally

> [!NOTE]
> If you intend to run the application on AKS, skip this section and move to [Push Docker image to Azure Container Registry](#push-docker-image-to-azure-container-registry)

1. Before you run the application, update the `src/main/resources/application.yaml` file with the details of your Azure Cosmos DB account. Replace `<ACCOUNT_NAME>` with Cosmos DB account name, `<PRIMARY_KEY>` with Cosmos DB primary key, and `<DATABASE_NAME>` with a database name.

   ```yaml
    azure:
      cosmos:
        uri: https://<ACCOUNT_NAME>.azure.com:443/
        key: <PRIMARY_KEY>
        database: <DATABASE_NAME>
        queryMetricsEnabled: true
        responseDiagnosticsEnabled: true
   ```

   > [!NOTE]
   > The database and a container (`users`) will get created automatically once you start the application.

1. Run the application locally.

   ```bash
   java -jar target/*.jar
   ```

2. To access the application, move to Step 2 in section [Access the application endpoint](#access-the-application-endpoint)

## Push Docker image to Azure Container Registry

1. Build the Docker image

   ```bash
   docker build -t cosmosdbspringbootregistry.azurecr.io/spring-cosmos-app:v1 .
   ```

   > [!NOTE]
   > `cosmosdbspringbootregistry.azurecr.io` is the Azure Container Registry server name

1. Log into Azure Container Registry.

   ```bash
   az acr login -n cosmosdbspringbootregistry
   ```

1. Push image to Azure Container Registry and confirm.

   ```bash
   docker push cosmosdbspringbootregistry.azurecr.io/spring-cosmos-app:v1

   az acr repository list --name cosmosdbspringbootregistry --output table
   ```

## Deploy application to Azure Kubernetes Service

1. Edit the `Secret` in `app.yaml` with the details of your Azure Cosmos DB setup.

    ```yml
    ...
    apiVersion: v1
    kind: Secret
    metadata:
      name: app-config
    type: Opaque
    stringData:
      application.properties: |
        azure.cosmos.uri=https://<enter cosmos db account name>.azure.com:443/
        azure.cosmos.key=<enter cosmos db primary key>
        azure.cosmos.database=<enter cosmos db database name>
        azure.cosmos.populateQueryMetrics=false
    ...
    ```

    > [!NOTE]
    > The database and a container (`users`) will get created automatically once you start the application.

2. Deploy to Kubernetes and wait for the `Pod` to transition to `Running` state:

    ```bash
    kubectl apply -f deploy/app.yaml
    kubectl get pods -l=app=spring-cosmos-app -w
    ```

   > [!NOTE]
   > You can check application logs using: `kubectl logs -f $(kubectl get pods -l=app=spring-cosmos-app -o=jsonpath='{.items[0].metadata.name}') -c spring-cosmos-app`


## Access the application endpoint

In this section, you will test the application by invoking its REST endpoints.

1. Run this command to access the application locally over port `8080`:

    ```bash
    kubectl port-forward svc/spring-cosmos-app-internal 8080:8080
    ```

1. Create new users:

    ```bash
    curl -i -X POST -H "Content-Type: application/json" -d '{"email":"john.doe@foobar.com", "firstName": "John", "lastName": "Doe", "city": "NYC"}' http://localhost:8080/users
    
    curl -i -X POST -H "Content-Type: application/json" -d '{"email":"mr.jim@foobar.com", "firstName": "mr", "lastName": "jim", "city": "Seattle"}' http://localhost:8080/users
    ```

1. Update user:

    ```bash
    curl -i -X POST -H "Content-Type: application/json" -d '{"email":"john.doe@foobar.com", "firstName": "John", "lastName": "Doe", "city": "NYC"}' http://localhost:8080/users
    ```

1. List all users.

    ```bash
    curl -i http://localhost:8080/users
    ```

1. List an exiting user.

    ```bash
    curl -i http://localhost:8080/users/john.doe@foobar.com
    ```

1. List a user that does not exist.

    ```bash
    curl -i http://localhost:8080/users/not.there@foobar.com
    ```

1. Replace user:

    ```bash
    curl -i -X PUT -H "Content-Type: application/json" -d '{"email":"john.doe@foobar.com", "firstName": "john", "lastName": "doe","city": "New Jersey"}' http://localhost:8080/users/
    ```

1. Try to replace user that does not exist:

    ```bash
    curl -i -X PUT -H "Content-Type: application/json" -d '{"email":"not.there@foobar.com", "firstName": "john", "lastName": "doe","city": "New Jersey"}' http://localhost:8080/users/
    ```

1. Delete a user:

    ```bash
    curl -i -X DELETE http://localhost:8080/users/first1.last1@foo.com
    ```

1. Delete a user that does not exist:

    ```bash
    curl -X DELETE http://localhost:8080/users/go.nuts@foobar.com
    ```

### Access the application using a public IP address

1. Create a Kubernetes Service of type `LoadBalancer`

    > [!NOTE]
    > This will create an Azure Load Balancer with a public IP address.

    ```bash
    kubectl apply -f deploy/load-balancer-service.yaml
    ```

1. Wait for the Azure Load Balancer to get created. Until then, the `EXTERNAL-IP` for the Kubernetes Service will remain in `<pending>` state.

    ```bash
    kubectl get service spring-cosmos-app -w
    
    NAME                TYPE           CLUSTER-IP   EXTERNAL-IP   PORT(S)          AGE
    spring-cosmos-app   LoadBalancer   10.0.68.83   <pending>     8080:31523/TCP   6s
    ```

    > [!NOTE]
    > `CLUSTER-IP` value may differ in your case

1. Once Azure Load Balancer creation completes, the `EXTERNAL-IP` will be available.

    ```bash
    NAME                TYPE           CLUSTER-IP   EXTERNAL-IP   PORT(S)          AGE
    spring-cosmos-app   LoadBalancer   10.0.68.83   20.81.108.180   8080:31523/TCP   18s
    ```
   
   > `EXTERNAL-IP` value may differ in your case

1. Access the application using public IP

   Stop the `kubectl watch` process and repeat the above `curl` commands with the public IP address along with port `8080`. For example, to list all users:

   ```bash
    curl -i http://20.81.108.180:8080/users
   ```
    
   > [!NOTE]
   > `20.81.108.180` is the public IP address - replace it with the value for your setup

## Check data in Azure Cosmos DB

Navigate to the `Data Explorer` menu of the Azure Cosmos DB account in the Azure portal. Access the `users` container to confirm. 

## Clean up resources

Delete the resource group to de-provision the resources created for this tutorial.