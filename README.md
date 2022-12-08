[![CodeQL](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/codeql-analysis.yml) [![CI](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/ci.yml) [![Markdown Links Check](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml/badge.svg)](https://github.com/Azure-Samples/azure-spring-boot-samples/actions/workflows/markdown-link-check.yml) 

# Spring Cloud Azure Samples
- This is a sample project for demonstration purposes.   
- The project demonstrates how to use [Spring Cloud Azure](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html) to develop applications.    
- The sample project contains 42 samples, and it still grows.    
- These samples are grouped by Azure services and [Spring Cloud Azure libraries](https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring).    

    > For example: [Use Spring Integration with single Azure Service Bus namespace](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace), it located in the path `/servicebus/spring-cloud-azure-starter-integration-servicebus`.
    >
    >  **servicebus**: The Azure service that the sample integrated with.
    >
    > **spring-cloud-azure-starter-integration-servicebus**: The dependency that the sample depends on.

## Branch Policy
Before Spring Cloud Azure 4.5.0, we use `main` branch as the develop branch while setting the default branch as the latest released branch.
Now we use main branch as the latest released branch and set as default branch.

| Branch Name               | Default Branch | Branch type    |
|---------------------------|----------------|----------------|
| main                      | true           | release branch |

## Current Branch Supported Versions
- [spring-boot-dependencies:2.7.6](https://repo.maven.apache.org/maven2/org/springframework/boot/spring-boot-dependencies/2.7.6/spring-boot-dependencies-2.7.6.pom).
- [spring-cloud-dependencies:2021.0.5](https://repo.maven.apache.org/maven2/org/springframework/cloud/spring-cloud-dependencies/2021.0.5/spring-cloud-dependencies-2021.0.5.pom).

## Running Samples With Terraform
With [terraform](https://www.terraform.io/) scripts and [DefaultAzureCredential](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#defaultazurecredential), most samples in the project can be run with the same 4 steps below:

```shell
# Step1 Initialize Terraform
terraform -chdir=./terraform init

# Step2 Apply your Terraform Configuration
terraform -chdir=./terraform apply -auto-approve

# Step3 Export Environment Valuables
source ./terraform/setup_env.sh

# Step4 Run With Maven
mvn clean spring-boot:run
```
It supports both Bash environment and [PowerShell](https://docs.microsoft.com/en-us/powershell/) environment.   
Please refer to [README.md](servicebus/spring-cloud-azure-starter-integration-servicebus/single-namespace/README.md) under each sample for detailed information.

### Run Samples Based On Spring Native
Two Maven profiles have been defined in this project to support compiling Spring applications to native executables: `buildpack` and `native`. The `buildpack` profile will use [Buildpacks](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-buildpacks) and the `native` profile will use [Native Build Tools](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-native-build-tools). Please follow the [storage-blob-native sample](spring-native/storage-blob-native) for more details.
