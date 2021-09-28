---
page_type: sample
languages:
- java
products:
- azure-key-vault
description: "Sample for running Spring Boot web server application executable jar with Azure key vault"
urlFragment: "azure-spring-boot-sample-run-with-command-line-server-side"
---

# Run Key-vault-agnostic Spring Boot Web Application with Azure Key Vault JCA via Command Line 

## Key concepts
This sample illustrates how to run a Spring Boot web server with Azure key vault jca via command line.

## Getting started


- This sample contains a simple and pure Spring Boot web application.

### Environment
jdk 11.0.12 or above

### Run Spring Boot web server with Azure key vault via command line. 
1. Open terminal and enter the folder where the pom.xml is and run `mvn package`. In the target folder there is a run-with-command-line-server-1.0.0.jar generated.  
2. Get a copy of the JCA configuration file.
   - Linux: <java-home>/lib/security/java.security
   - MacOS Big Sur: <java-home>/conf/security/java.security
   - Windows: <java-home>\lib\security\java.security 
3. Edit your copy of the JCA configuration file. Replace the provider section with: 
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
4. Get the Azure security key vault jca jar. You can build the latest one by yourself then you will get, for example, azure-security-keyvault-jca-2.0.0-beta.1.jar. You can also download the latest published jar from maven repository, then you will get, for example, azure-security-keyvault-jca.1.0.1.jar.
5. Make a directory, for example, sample_server. Then put the edited copy of java.security file, the run-with-command-line-server-1.0.0.jar, the azure-security-keyvault-jca-2.0.0-beta.1.jar into sample_server
6. Open terminal and enter the directory sample_server, run the following command:
   ```
   java --module-path ./azure-security-keyvault-jca-2.0.0-beta.1.jar --add-modules com.azure.security.keyvault.jca \
    -Dsecurity.overridePropertiesFile=true -Djava.security.properties==./java.security \
    -Dazure.keyvault.uri=<yourAzureKeyVaultUri> -Dazure.keyvault.tenant-id=<yourTenantID> -Dazure.keyvault.client-id=<youClientID> -Dazure.keyvault.client-secret=<yourSecretValue> \
    -jar run-with-command-line-server-side-1.0.0.jar \ 
    --server.port=8443 --server.ssl.enabled=true --server.ssl.key-alias=<yourCertificatName> --server.ssl.keystore-type=DKS --server.ssl.keyStoreProvider=AzureKeyVault --server.ssl.key-store=classpath:keyvault.dummy 
   ```
   The server will be started without needing client side authentication, you can visit https://localhost:8443 and see "Hello World!". If you want to enable the client side authentication, please insert `-Djavax.net.ssl.trustStoreType=AzureKeyVault` into the above command and append ` --server.ssl.client-auth=need` to the above command.
7. (Optional) You can also use the KeyVaultKeyStore with local certificates. 
    - For example, there are some well known CAs. You can put them into a folder, then configure the system property azure.cert-path.well-known=\<yourFolderPath>. The certificates in this folder will be loaded by KeyVaultKeystore. If you don't configure such a property, the default well-known path will be `/etc/certs/well-known/`.
    - Besides the well-known path, you can also put your customized certificates into another folder specified by azure.cert-path.custom=\<yourCustomPath>, by default, the custom path is `/etc/certs/custom/`.
    - You can also put certificates under the class path, build a folder named `keyvault` and configure it under the class path, then all the certificates in this folder will be loaded by key vault keystore.
