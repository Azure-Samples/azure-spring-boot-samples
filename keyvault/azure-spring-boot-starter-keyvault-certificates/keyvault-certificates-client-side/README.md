# Azure Spring Boot Key Vault Certificates

## Key concepts
This sample illustrates how to use [Azure Spring Boot Starter Key Vault Certificates ][azure_spring_boot_starter_key_vault_certificates].

This sample should work together with [azure-spring-boot-sample-keyvault-certificates-server-side].

## Getting started


- Start azure-spring-boot-sample-keyvault-certificates-server-side's SampleApplication.

### Run sample with service principal
1. Option 1 - If you created the resources via the script, you need set environment variables created in `azure-spring-boot-sample-keyvault-certificates-server-side` application by running command:
   ```
   source script/setup.sh
   ```
2. Option 2 - If you created the resource via the Azure Portal, you need configure the application.yml manually, please replace the placeholders with the resources you created.
   > Attention: The service principal must be configured with permissions:   
   > Certificate Permissions: configure with **get and list** permissions.  
   > Key Permissions: configure with **get** permission.  
   > Secret Permissions: configure with **get** permission.

#### Using TLS with service principal 
1. Start azure-spring-boot-sample-keyvault-certificates-client-side's SampleApplication by running command:
   ```
   mvn spring-boot:run
   ```
1. To use RestTemplate, access http://localhost:8080/resttemplate/tls

    Then you will get
    ```text
    Response from restTemplate tls "https://localhost:8443/": Hello World
    ```
1. To use WebClient, access http://localhost:8080/webclient/tls

   Then you will get

   ```text
   Response from webClient tls "https://localhost:8443/": Hello World
   ```

#### Using mTLS with service principal 

1. In the sample `AzureKeyVaultKeyStoreUtil.java`, change the `self-signed` to your certificate alias.
    ```java
    private static final String CLIENT_ALIAS = "self-signed";
    ```
1. Add properties in application.yml of `server side` on the base of current configuration:

    ```yaml
    server:
      ssl:
        client-auth: need        # Used for mTLS
        trust-store-type: AzureKeyVault   # Used for mTLS   
    ```
1. Start azure-spring-boot-sample-keyvault-certificates-client-side's SampleApplication by running command:
   ```
   mvn spring-boot:run
   ```
1. When the mTLS server starts, `restTemplate tls endpoint`(http://localhost:8080/resttemplate/tls) and `webClient tls endpoint`(http://localhost:8080/webclient/tls) will not be able to access the resource. 
2. To use RestTemplate, access http://localhost:8080/resttemplate/mtls

    Then you will get
    ```text
    Response from restTemplate mtls "https://localhost:8443/": Hello World
    ```
1. To use WebClient, access http://localhost:8080/webclient/mtls

   Then you will get

   ```text
   Response from webClient mtls "https://localhost:8443/": Hello World
   ```


### Run sample with managed identity

1. If you are using managed identity instead of service principal, use below properties in your `application.yml`:

    ```yaml
    azure:
      keyvault:
        uri: ${KEY_VAULT_URI}
        managed-identity: # client-id of the user-assigned managed identity to use. If empty, then system-assigned managed identity will be used.
    ```
    Make sure the managed identity can access target Key Vault.
1. Set environment variables created in `azure-spring-boot-sample-keyvault-certificates-server-side` application by running command:
   ```
   source script/setup.sh
   ```

#### Using TLS/mTLS with managed identity
1. Replace the `CREDENTIAL_TYPE` value in `AzureKeyVaultKeyStoreUtil.java` as
    ```java
    private static final CredentialType CREDENTIALTYPE = CredentialType.ManagedIdentity;
    ```
1. Follow the above step of [Using TLS with service principal](#using-tls-with-service-principal) to use TLS.
1. Follow the above step of [Using mTLS with service principal](#using-mtls-with-service-principal) to use mTLS.



### (Optional) Use the KeyVaultKeyStore with local certificates as the trust resources. 
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
[azure-spring-boot-sample-keyvault-certificates-server-side]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-server-side
