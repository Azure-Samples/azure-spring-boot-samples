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

### Environment
jdk 11.0.12 or above

### Run Spring Boot web server with Azure key vault via command line.
1. Firstly, you need start the server side sample. Please refer to server side tutorial: [Run with Command Line Server Side][run_with_command_line_server_side]
2. Open terminal and enter the folder where the pom.xml is and run `mvn package`. In the target folder there is a run-with-command-line-client-1.0.0.jar generated.
3. Get a copy of the JCA configuration file.
    - Linux: <java-home>/lib/security/java.security
    - MacOS Big Sur: <java-home>/conf/security/java.security
    - Windows: <java-home>\lib\security\java.security
4. Edit your copy of the JCA configuration file. Replace the provider section with:
   ```
   security.provider.1=SUN
   security.provider.2=SunRsaSign
   security.provider.3=SunEC
   security.provider.4=SunJSSE
   security.provider.5=SunJCE
   security.provider.6=SunJGSS
   security.provider.7=SunSASL
   security.provider.8=XMLDSig
   security.provider.9=SunPCSC
   security.provider.10=JdkLDAP
   security.provider.11=JdkSASL
   security.provider.12=Apple
   security.provider.13=SunPKCS11
   security.provider.14=com.azure.security.keyvault.jca.KeyVaultJcaProvider
   ```
5. Get the Azure security key vault jca jar. You can download the latest published jar from maven repository [azure-security-keyvault-jca][azure-security-keyvault-jca]. When this document is written, the latest jar is azure-security-keyvault-jca.2.1.0.jar
6. Make a directory, for example, sample_client. Then put the edited copy of java.security file, the run-with-command-line-client-1.0.0.jar, the azure-security-keyvault-jca-2.1.0.jar into sample_client
7. Open terminal and enter the directory sample_client, run the following command:
   ```
   java --module-path ./azure-security-keyvault-jca-2.1.0.jar --add-modules com.azure.security.keyvault.jca \ 
   -Dsecurity.overridePropertiesFile=true -Djava.security.properties==./java.security \ 
   -Djavax.net.ssl.trustStoreType=AzureKeyVault \ 
   -Dazure.keyvault.uri=<yourKeyVaultURI> -Dazure.keyvault.tenant-id=<yourTenantID> -Dazure.keyvault.client-id=<yourClientID> -Dazure.keyvault.client-secret=<yourSecretValue> \ 
   -jar run-with-command-line-client-side-1.0.0.jar
   ```
   If you have run the server side with client authentication needed, please use the following command instead of the above to run the client side:
   ```
   java --module-path ./azure-security-keyvault-jca-2.1.0.jar --add-modules com.azure.security.keyvault.jca \ 
   -Dsecurity.overridePropertiesFile=true -Djava.security.properties==./java.security \ 
   -Djavax.net.ssl.trustStoreType=AzureKeyVault \ 
   -Djavax.net.ssl.keyStoreType=AzureKeyVault -Dazure.keyvault.uri=<yourKeyVaultURI> -Dazure.keyvault.tenant-id=<yourTenantID> -Dazure.keyvault.client-id=<yourClientID> -Dazure.keyvault.client-secret=<yourSecretValue> \ 
   -jar run-with-command-line-client-side-1.0.0.jar
   ```
8 Check the output. The client will be started and connect to the server side after a while, you will see "Hello World!". 
9. (Optional) You can also use the KeyVaultKeyStore with local certificates.
   - For example, there are some well known CAs. You can put them into a folder, then configure the system property azure.cert-path.well-known=\<yourFolderPath>. The certificates in this folder will be loaded by KeyVaultKeystore. If you don't configure such a property, the default well-known path will be `/etc/certs/well-known/`.
   - Besides the well-known path, you can also put your customized certificates into another folder specified by azure.cert-path.custom=\<yourCustomPath>, by default, the custom path is `/etc/certs/custom/`.
   - You can also put certificates under the class path, build a folder named `keyvault` and configure it under the class path, then all the certificates in this folder will be loaded by key vault keystore.

<!-- LINKS -->

[run_with_command_line_server_side]: https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/keyvault/azure-security-keyvault-jca/run-with-command-line-server-side/README.md
[azure-security-keyvault-jca]: https://mvnrepository.com/artifact/com.azure/azure-security-keyvault-jca
