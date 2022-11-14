# Azure Spring Boot Key Vault Certificates

## Key concepts
This sample illustrates how to use [Azure Spring Boot Starter Key Vault Certificates ][azure_spring_boot_starter_key_vault_certificates].

In this sample, a certificate named `self-signed` is stored into an Azure Key Vault, and a sample Spring application will use its value as a configuration property value.

This sample can work together with [azure-spring-boot-sample-keyvault-certificates-client-side].

## Getting started


- This sample will create a resource group and Azure Key Vault in your specified subscription. 
- This sample will create and store a certificate in your Azure Key Vault.
- This sample will create a service principal to read certificates/keys/secrets from your Azure Key Vault.

### Create resources: Service Principal and Key Vault.
#### Option 1 - via the script
1. Run command `az login` to login to the Azure CLI.
1. Open `scripts/export_environment_variables.sh` and enter the following information:
   ```
    # Set your Azure Subscription id where all required resources will be created.
    export SUBSCRIPTION_ID=
    
    # Set the name for your Azure resource group to be created.
    export RESOURCE_GROUP_NAME=
    
    # Set the region for all resources to be created.
    export REGION_NAME=
    
    # Set the name for your Azure Key Vault to be created.
    export KEY_VAULT_NAME=
   
   # Set the name for your certificate to be created.
    export CERTIFICATE_NAME=
    
    # Set the name for your Service Principal to be created. It should be NULL if using managed identity. ====
    export SERVICE_PRINCIPAL_NAME=
   ```
1. Build up required Azure resources by running command. 
   ```
   source script/setup.sh
   ```
  
#### Option 2  - via Azure Portal 
You can also create resources manually via Azure Portal. Please follow:
1. Obtain a Service Principal. There are two ways to obtain a service principal:
   - Recommended: enable a managed identity for the application. For more information, see [the Managed identity overview][the_managed_identity_overview].
   - If you cannot use managed identity, you can register your application with AAD, see [register app with AAD][register_app_with_AAD]. The registration also creates a second application object that identifies your app.
2. Create the key vault and certificates. Please refer to [create key vault and certificates][create_key_vault_and_certificates]
3. Make the key vault accessible to your service principal. Please refer to [assign key vault access policy][assign_key_vault_access_policy]
   > Attention: The service principal must be configured with permissions:   
   > Certificate Permissions: configure with **get and list** permissions.  
   > Key Permissions: configure with **get** permission.  
   > Secret Permissions: configure with **get** permission.
5. You need manually configure the application.yml, replace the placeholders with the resources you created in the Azure Portal.

If you used the script to create the resources, or you created the resources via Azure Portal and created the Service Principal in App Registration way.
### Run sample Using TLS. 
1. Run command `mvn spring-boot:run`
1. Access https://localhost:8443/

Then you will get
```text
Hello World
``` 

### Run sample Using mTLS 

1. Add properties in application.yml on the base of current configuration:
```yaml
server:
  ssl:
    client-auth: need        # Used for mTLS
    trust-store-type: AzureKeyVault   # Used for mTLS   

```
1. Run command `mvn spring-boot:run`
1. mTLS for mutual authentication. So your client needs to have a trusted CA certificate.([azure-spring-boot-sample-keyvault-certificates-client-side]is a trusted client sample.)
1. Your client access https://localhost:8443/

Then the client or server will get
```text
Hello World
``` 

If you created resources via Azure Portal and created the Service Principal in Managed Identity way.
### Run Sample with managed identity
If you are using managed identity instead of service principal, use below properties in your `application.yml`:

```yaml
azure:
  keyvault:
    uri: ${KEY_VAULT_URI}
#    managed-identity: # client-id of the user-assigned managed identity to use. If empty, then system-assigned managed identity will be used.
server:
  ssl:
    key-alias: ${CERTIFICATE_NAME} 
    key-store-type: AzureKeyVault
```
Make sure the managed identity can access target Key Vault.

1. Run command `az login` to login to the Azure CLI.
1. Open `scripts/export_environment_variables.sh` and enter the following information:
   ```
    # Set your Azure Subscription id where all required resources will be created.
    export SUBSCRIPTION_ID=
    
    # Set the name for your Azure resource group to be created.
    export RESOURCE_GROUP_NAME=
    
    # Set the region for all resources to be created.
    export REGION_NAME=
    
    # Set the name for your Azure Key Vault to be created.
    export KEY_VAULT_NAME=
   ```
1. Build up required Azure resources by running command
   ```
   source script/setup.sh
   ```

1. Follow the above step of [Using TLS with service principal](#using-tls-with-service-principal) or [Using mTLS with service principal](#using-mtls-with-service-principal).


### (Optional) Use the KeyVaultKeyStore with local certificates as the trust resources. This is only useful when server needs to trust its party, for example when the server opens mTLS mode.
- For example, there are some well known CAs. You can put them into a folder, then configure in the application.yml the  azure:cert-path:well-known=\<yourFolderPath>. The certificates in this folder will be loaded by KeyVaultKeystore. If you don't configure such a property, the default well-known path will be `/etc/certs/well-known/`.
- Besides the well-known path, you can also put your customized certificates into another folder specified by azure:cert-path:custom=\<yourCustomPath>, by default, the custom path is `/etc/certs/custom/`.
- You can also put certificates under the class path, build a folder named `keyvault` and configure it under the class path, then all the certificates in this folder will be loaded by key vault keystore.

To configure the local certificates, please uncomment and configure the optional local certificates path.
```yaml
azure:
  #cert-path: 
    #well-known:  # Optional local certificates path. Your local path that holds the well-known certificates.
    #custom: # Optional local certificates path. Your local path that holds your customized certificates. 
```

<!-- LINKS -->

[azure_spring_boot_starter_key_vault_certificates]: https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-boot_3.14.0/sdk/spring/azure-spring-boot-starter-keyvault-certificates/README.md
[steps_to_store_certificate]: https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-boot_3.14.0/sdk/spring/azure-spring-boot-starter-keyvault-certificates/README.md#create-an-azure-key-vault
[azure-spring-boot-sample-keyvault-certificates-client-side]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-client-side
[the_managed_identity_overview]: https://docs.microsoft.com/en-us/azure/active-directory/managed-identities-azure-resources/overview
[register_app_with_AAD]: https://docs.microsoft.com/en-us/azure/active-directory/develop/quickstart-register-app
[create_key_vault_and_certificates]: https://docs.microsoft.com/en-us/azure/key-vault/certificates/quick-create-portal
[assign_key_vault_access_policy]: https://docs.microsoft.com/en-us/azure/key-vault/general/assign-access-policy?tabs=azure-portal
