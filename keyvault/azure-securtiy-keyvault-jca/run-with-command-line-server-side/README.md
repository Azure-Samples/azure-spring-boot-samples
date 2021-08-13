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

### Run Spring Boot web server with Azure key vault via command line. 
1. Run command `mvn package` in the folder where the pom.xml. In the target folder there is a run-with-command-line-server-1.0.0.jar generated.  
1. Get a copy of the JCA configuration file.
   - Linux: <java-home>/lib/security/java.security
   - MacOS Big Sur: <java-home>/conf/security/java.security
   - Windows: <java-home>\lib\security\java.security 
1. Edit your copy of the JCA configuration file. Replace the provider section with: 
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
1. Get the Azure security key vault jca jar. You can build the latest one by yourself then you will get, for example, azure-security-keyvault-jca-2.0.0-beta.1.jar. You can also download the latest published jar from maven repository, then you will get, for example, azure-security-keyvault-jca.1.0.1.jar.
1. Make a directory, for example, sample_server. Then put the edited copy of java.security file, the run-with-command-line-server-1.0.0.jar, the azure-security-keyvault-jca-2.0.0-beta.1.jar into sample_server
1. Open terminal and enter the directory sample_server, run the following command:
   ```
   java --module-path ./azure-security-keyvault-jca-2.0.0-beta.1.jar --add-modules com.azure.security.keyvault.jca -Dsecurity.overridePropertiesFile=true -Djava.security.properties==./java.security -Dazure.keyvault.uri=<yourAzureKeyVaultUri> -Dazure.keyvault.tenant-id=<yourTenantID> -Dazure.keyvault.client-id=<youClientID> -Dazure.keyvault.client-secret=<yourSecretValue> -jar run-with-command-line-server-side-1.0.0.jar --server.port=8443 --server.ssl.enabled=true --server.ssl.key-alias=<yourCertificatName> --server.ssl.keystore-type=AzureKeyVault --server.ssl.key-store=.
   ```
   The server will be started without needing client side authentication. If you want to enable the client side authentication, please append ` --server.ssl.client-auth=need` to the above command.


## Examples
## Troubleshooting
## Next steps
## Run with Maven
## Contributing
