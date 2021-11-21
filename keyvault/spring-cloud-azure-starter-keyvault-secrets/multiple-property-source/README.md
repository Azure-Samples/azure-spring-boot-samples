
# Spring Cloud Azure Starter Key Vault Secrets Sample

This sample illustrates the simplest usage of `spring-cloud-azure-starter-keyvault-secrets`. To learn all features, please refer to [reference doc](https://microsoft.github.io/spring-cloud-azure/docs/4.0.0-beta.1/reference/html/index.html).

## Create Azure resources

1. Read [document about register an application](https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app), register an application. get the `Application (client) ID`: **${AZURE_CLIENT_ID_1}**, and create a secret, get the `Client Secret Value`: **${AZURE_CLIENT_SECRET_1}**.
2. Read [document about create key vault](https://docs.microsoft.com/en-us/azure/key-vault/general/quick-create-portal), get the `Directory ID`: **${AZURE_TENANT_ID_1}** and `Vault URI` **${ENDPOINT_1}**.
3. Read [document about assign Key Vault access policy](https://docs.microsoft.com/en-us/azure/key-vault/general/assign-access-policy?tabs=azure-portal), assign `Secret Management` template to the client(or principal) we created in step 1.
4. Same to previous steps, get **${AZURE_CLIENT_ID_2}**, **${AZURE_CLIENT_SECRET_2}**, **${AZURE_TENANT_ID_2}** and **${ENDPOINT_1}**. assign `Secret Management` template to the **${AZURE_CLIENT_ID_2}. 

## Fill the values in application.yml
Fill these values in application.yml: **${AZURE_TENANT_ID_1}**, **${AZURE_CLIENT_ID_1}**, **${AZURE_CLIENT_SECRET_1}**, **${ENDPOINT_1}**, **${AZURE_TENANT_ID_2}**, **${AZURE_CLIENT_ID_2}**, **${AZURE_CLIENT_SECRET_2}**, **${ENDPOINT_2}**.

## Start application
Start the application, you will see a log like this:
```text
secretNameInKeyVault1: secret-value-1
secretNameInKeyVault2: secret-name-in-key-vault-2-value-2
secretNameInKeyVaultBoth: secret-value-1
```

We can see that property-source-1 have higher priority.

