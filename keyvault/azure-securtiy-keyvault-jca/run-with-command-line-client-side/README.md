---
page_type: sample
languages:
- java
products:
- azure-key-vault
description: "Sample for running executable jar with Azure key vault"
urlFragment: "azure-spring-boot-sample-run-with-command-line-client-side"
---

# Run Key-vault-agnostic Web Client Executable Jar with Azure Key Vault JCA via Command Line 

## Key concepts
This sample illustrates how to run a web client with Azure key vault jca via command line.

## Getting started


- This sample contains a simple web client function. 

### Run Spring Boot web server with Azure key vault via command line.
1. Open terminal and enter the folder where the pom.xml is and run `mvn package`. In the target folder there is a run-with-command-line-client-1.0.0.jar generated.
2. Get a copy of the JCA configuration file.
    - Linux: <java-home>/lib/security/java.security
    - MacOS Big Sur: <java-home>/conf/security/java.security
    - Windows: <java-home>\lib\security\java.security
3. Edit your copy of the JCA configuration file. Replace the provider section with:
   ```
   security.provider.1=SUN
   security.provider.2=SunRsaSign
   security.provider.3=SunEC
   security.provider.4=com.azure.security.keyvault.jca.KeyVaultTrustManagerFactoryProvider
   security.provider.5=SunJSSE
   security.provider.6=SunJCE
   security.provider.7=SunJGSS
   security.provider.8=SunSASL
   security.provider.9=XMLDSig
   security.provider.10=SunPCSC
   security.provider.11=JdkLDAP
   security.provider.12=JdkSASL
   security.provider.13=Apple
   security.provider.14=SunPKCS11
   security.provider.15=com.azure.security.keyvault.jca.KeyVaultJcaProvider
   ```
4. Get the Azure security key vault jca jar. You can build the latest one by yourself then you will get, for example, azure-security-keyvault-jca-2.0.0-beta.1.jar. You can also download the latest published jar from maven repository, then you will get, for example, azure-security-keyvault-jca.1.0.1.jar.
5. Make a directory, for example, sample_client. Then put the edited copy of java.security file, the run-with-command-line-client-1.0.0.jar, the azure-security-keyvault-jca-2.0.0-beta.1.jar into sample_client
6. Start the server side. Please refer to server side tutorial: [Run with Command Line Server Side][run_with_command_line_server_side]  
7. Open terminal and enter the directory sample_client, run the following command:
   ```
   java --module-path ./azure-security-keyvault-jca-2.0.0-beta.1.jar --add-modules com.azure.security.keyvault.jca -Dsecurity.overridePropertiesFile=true -Djava.security.properties==./java.security -Dazure.keyvault.uri=<yourKeyVaultURI> -Dazure.keyvault.tenant-id=<yourTenantID> -Dazure.keyvault.client-id=<yourClientID> -Dazure.keyvault.client-secret=<yourSecretValue> -jar run-with-command-line-client-side-1.0.0.jar
   ```
   The client will be started and connect to the server side. If you have run the server side with client authentication needed, please use the following command instead of the above to run the client side:
   ```
   java --module-path ./azure-security-keyvault-jca-2.0.0-beta.1.jar --add-modules com.azure.security.keyvault.jca -Dsecurity.overridePropertiesFile=true -Djava.security.properties==./java.security -Djavax.net.ssl.keyStoreType=AzureKeyVault -Dazure.keyvault.uri=<yourKeyVaultURI> -Dazure.keyvault.tenant-id=<yourTenantID> -Dazure.keyvault.client-id=<yourClientID> -Dazure.keyvault.client-secret=<yourSecretValue> -jar run-with-command-line-client-side-1.0.0.jar
   ```
8. (Optional) You can also use the KeyVaultKeyStrore with local certificates.
   - For example, there are some well known CAs. You can put them into a folder, then configure the system property azure.cert-path.well-known=\<yourFolderPath>. The certificates in this folder will be loaded by KeyVaultKeystore. If you don't configure such a property, the default well-known path will be `/etc/certs/well-known/`.
   - Besides the well-known path, you can also put your customized certificates into another folder specified by azure.cert-path.custom=\<yourCustomPath>, by default, the custom path is `/etc/certs/custom/`.
   - You can also put certificates under the class path, build a folder named `keyvault` and configure it under the class path, then all the certificates in this folder will be loaded by key vault keystore.


## Examples
## Troubleshooting
## Next steps
## Run with Maven
## Contributing

<!-- LINKS -->

[run_with_command_line_server_side]: https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/keyvault/azure-security-keyvault-jca/run-with-command-line-server-side/README.md