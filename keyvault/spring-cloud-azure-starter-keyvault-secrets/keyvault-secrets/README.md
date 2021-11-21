
# Spring Cloud Azure Starter Key Vault Secrets Sample

This sample illustrates the simplest usage of `spring-cloud-azure-starter-keyvault-secrets`. To learn all features, please refer to [reference doc](https://microsoft.github.io/spring-cloud-azure/docs/4.0.0-beta.1/reference/html/index.html).

## Create Azure resources

1. Read [document about register an application](https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app), register an application. get the `Application (client) ID`: **${client-id}**, and create a secret, get the `Client Secret Value`: **${client-secret}**.
2. Read [document about create key vault](https://docs.microsoft.com/en-us/azure/key-vault/general/quick-create-portal), get the `Directory ID`: **${tenant-id}** and `Vault URI` **${endpoint}**.
3. Read [document about assign Key Vault access policy](https://docs.microsoft.com/en-us/azure/key-vault/general/assign-access-policy?tabs=azure-portal), assign `Secret Management` template to the client(or principal) we created in step 1.

## Fill the values in application.yml
Fill these values in application.yml: **${tenant-id}**, **${client-id}**, **${client-secret}**, **${endpoint}**.

## Start application
Start the application, you will see a log like this:
```text
property springDataSourceUrl in Azure Key Vault: <spring-data-source-url-value>
```

