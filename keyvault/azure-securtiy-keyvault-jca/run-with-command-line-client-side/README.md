# Use azure-security-keyvault-jca.jar via Command Line in client side

## Key concepts
This sample illustrates how to use azure-security-keyvault-jca.jar via command
line in client side.

## Getting started

- This sample contains a simple web client function. 

### Environment
jdk 11.0.12 or above

### Run Spring Boot web server with azure-security-keyvault-jca.jar via command line.
1. Start the server side sample. Please refer to [server side tutorial](../run-with-command-line-server-side/README.md).
1. Open terminal and enter the folder where the pom.xml is and run `mvn package`. In the target 
   folder there is a run-with-command-line-client-1.0.0.jar generated.
1. Get a copy of the JCA configuration file.
    - Linux: <java-home>/lib/security/java.security
    - MacOS Big Sur: <java-home>/conf/security/java.security
    - Windows: <java-home>\conf\security\java.security
1. Edit your copy of the JCA configuration file. Add a new item: KeyVaultJcaProvider
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
   # Next line is the new added item.
   security.provider.14=com.azure.security.keyvault.jca.KeyVaultJcaProvider
   ```
1. Get the azure-security-keyvault-jca.jar. You can download the latest published jar from maven
   repository [azure-security-keyvault-jca](https://mvnrepository.com/artifact/com.azure/azure-security-keyvault-jca). When this document is 
   written, the latest jar is azure-security-keyvault-jca-2.7.0.jar
1. Make a directory, for example, sample_client. Then put the 3 files into sample_client folder
    - java.security 
    - run-with-command-line-client-side-1.0.0.jar
    - azure-security-keyvault-jca-2.7.0.jar 
1. Create the key vault and certificates, please refer to [create key vault and certificates](https://docs.microsoft.com/en-us/azure/key-vault/certificates/quick-create-portal). Create service principal and add a secret, please refer to [register app with AAD](https://docs.microsoft.com/azure/active-directory/develop/quickstart-register-app).
1. Create a new **Access policy** for the service principal created in the previous step, including the *Get* and *List* permissions of the **Secret permissions**, and the *Get* and *List* permissions of the **Certificate permissions**.
1. Replace properties `<yourAzureKeyVaultUri>`, `<yourTenantID>`, `<youClientID>`, `<yourSecretValue>` with your created resources in the following command, open terminal and enter the directory sample_client, run the changed command:
   ```
   java \
   --module-path ./azure-security-keyvault-jca-2.7.0.jar \
   --add-modules com.azure.security.keyvault.jca \
   -Dsecurity.overridePropertiesFile=true \
   -Djava.security.properties==./java.security \
   -Djavax.net.ssl.trustStoreType=AzureKeyVault \
   -Dazure.keyvault.uri=<yourKeyVaultURI> \
   -Dazure.keyvault.tenant-id=<yourTenantID> \
   -Dazure.keyvault.client-id=<yourClientID> \
   -Dazure.keyvault.client-secret=<yourSecretValue> \
   -jar run-with-command-line-client-side-1.0.0.jar
   ```
   If you have run the server side with client authentication needed, please use the following 
   command instead of the above to run the client side:
   ```
   java \
   --module-path ./azure-security-keyvault-jca-2.7.0.jar \
   --add-modules com.azure.security.keyvault.jca \
   -Dsecurity.overridePropertiesFile=true \
   -Djava.security.properties==./java.security \
   -Djavax.net.ssl.trustStoreType=AzureKeyVault \
   -Djavax.net.ssl.keyStoreType=AzureKeyVault \
   -Dazure.keyvault.uri=<yourKeyVaultURI> \
   -Dazure.keyvault.tenant-id=<yourTenantID> \
   -Dazure.keyvault.client-id=<yourClientID> \
   -Dazure.keyvault.client-secret=<yourSecretValue> \
   -jar run-with-command-line-client-side-1.0.0.jar
   ```
1. Check the output. The client will be started and connect to the server side after a while, you 
   will see "Hello World!". 
1. (Optional) You can also use the KeyVaultKeyStore with local certificates.
   - For example, there are some well known CAs. You can put them into a folder, then configure the
     system property azure.cert-path.well-known=\<yourFolderPath>. The certificates in this folder 
     will be loaded by KeyVaultKeystore. If you don't configure such a property, the default 
     well-known path will be `/etc/certs/well-known/`.
   - Besides, the well-known path, you can also put your customized certificates into another folder
     specified by azure.cert-path.custom=\<yourCustomPath>, by default, the custom path is
     `/etc/certs/custom/`.
   - You can also put certificates under the class path, build a folder named `keyvault` and 
     configure it under the class path, then all the certificates in this folder will be loaded by 
     key vault keystore.

