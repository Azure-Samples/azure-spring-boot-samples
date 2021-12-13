# Azure Spring Boot Key Vault Certificates client library for Java

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
#### Using TLS with service principal created via App registration.
1. Start azure-spring-boot-sample-keyvault-certificates-client-side's SampleApplication by running command:
   ```
   mvn spring-boot:run
   ```
1. Access http://localhost:8080/tls

    Then you will get
    ```text
    Response from "https://localhost:8443/": Hello World
    ```

#### Using mTLS with service principal created via App registration.
1. In the sample `ApplicationConfiguration.class`, change the `self-signed` to your certificate alias.
    <!-- embedme ../azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-client-side/src/main/java/com/azure/spring/security/keyvault/certificates/sample/client/side/SampleApplicationConfiguration.java#L70-L75 -->
    ```java
    private static class ClientPrivateKeyStrategy implements PrivateKeyStrategy {
        @Override
        public String chooseAlias(Map<String, PrivateKeyDetails> map, Socket socket) {
            return "self-signed"; // It should be your certificate alias used in client-side
        }
    }
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
1. When the mTLS server starts, `tls endpoint`(http://localhost:8080/tls) will not be able to access the resource. Access http://localhost:8080/mTLS

    Then you will get
    ```text
    Response from "https://localhost:8443/": Hello World
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

#### Using TLS with managed identity
1. Replace the `restTemplateWithTLS` bean in `SampleApplicationConfiguration.java` as
    <!-- embedme ../azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-client-side/src/main/java/com/azure/spring/security/keyvault/KeyVaultJcaManagedIdentitySample.java#L22-L40 -->
    ```java
    @Bean
    public RestTemplate restTemplateWithTLS() throws Exception {
        KeyStore trustStore = KeyStore.getInstance("AzureKeyVault");
        KeyVaultLoadStoreParameter parameter = new KeyVaultLoadStoreParameter(
            System.getProperty("azure.keyvault.uri"),
            System.getProperty("azure.keyvault.managed-identity"));
        trustStore.load(parameter);
        SSLContext sslContext = SSLContexts.custom()
                                           .loadTrustMaterial(trustStore, null)
                                           .build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,
            (hostname, session) -> true);
        CloseableHttpClient httpClient = HttpClients.custom()
                                                    .setSSLSocketFactory(socketFactory)
                                                    .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
    
        return new RestTemplate(requestFactory);
    }
    ```
1. Follow the above step of [Using TLS with service principal](#using-tls-with-service-principal).

#### Using mTLS with managed identity
1. Replace the `restTemplateWithMTLS` bean in `SampleApplicationConfiguration.java` as
    <!-- embedme ../azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-client-side/src/main/java/com/azure/spring/security/keyvault/KeyVaultJcaManagedIdentitySample.java#L42-L61 -->
    ```java
    @Bean
    public RestTemplate restTemplateWithMTLS() throws Exception {
        KeyStore azureKeyVaultKeyStore = KeyStore.getInstance("AzureKeyVault");
        KeyVaultLoadStoreParameter parameter = new KeyVaultLoadStoreParameter(
            System.getProperty("azure.keyvault.uri"),
            System.getProperty("azure.keyvault.managed-identity"));
        azureKeyVaultKeyStore.load(parameter);
        SSLContext sslContext = SSLContexts.custom()
                                           .loadTrustMaterial(azureKeyVaultKeyStore, null)
                                           .loadKeyMaterial(azureKeyVaultKeyStore, "".toCharArray(), new ClientPrivateKeyStrategy())
                                           .build();
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,
            (hostname, session) -> true);
        CloseableHttpClient httpClient = HttpClients.custom()
                                                    .setSSLSocketFactory(socketFactory)
                                                    .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
    
        return new RestTemplate(requestFactory);
    }
    ```
1. Follow the above step of [Using mTLS with service principal](#using-mtls-with-service-principal).


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

[azure_spring_boot_starter_key_vault_certificates]: https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-boot_3.6.0/sdk/spring/azure-spring-boot-starter-keyvault-certificates/README.md
[steps_to_store_certificate]: https://github.com/Azure/azure-sdk-for-java/blob/azure-spring-boot_3.6.0/sdk/spring/azure-spring-boot-starter-keyvault-certificates/README.md#creating-an-azure-key-vault
[azure-spring-boot-sample-keyvault-certificates-server-side]: https://github.com/Azure-Samples/azure-spring-boot-samples/blob/main/keyvault/azure-spring-boot-starter-keyvault-certificates/keyvault-certificates-server-side
