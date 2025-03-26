# Spring Boot application with Azure Key Vault JCA

This repo demonstrates how to use [Java Crypto Architecture (JCA) Provider] for Azure Key Vault in [Spring Boot] application by Spring Boot [SSL Bundles](https://docs.spring.io/spring-boot/reference/features/ssl.html). We support using Key Vault SSL Bundles to enable embedded Web Server SSL, RestTemplate SSL, and WebClient SSL. There are three specific scenarios.

[Java Crypto Architecture (JCA) Provider]: https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/keyvault/azure-security-keyvault-jca
[Spring Boot]: https://spring.io/projects/spring-boot

## Server SSL and RestTemplate SSL

This is the scenario to enable server SSL and `RestTemplate` SSL in a web application. It only requires running `ssl-bundles-server` standalone. You can verify it by following [ssl-bundles-server/README.md](ssl-bundles-server/README.md).

## mTLS for Server SSL and RestTemplate SSL

This mTLS scenario uses a web app as Server that enabled server SSL and needs client auth, and another web app with `RestTemplate` SSL enabled. You need to use the `ssl-bundles-server` and `ssl-bundles-rest-template` samples together. Azure resources created by either of these two sample projects can be shared with each other, so you don't need to create both.

### Server side mTLS:

Please refer to the [ssl-bundles-server/README.md](ssl-bundles-server/README.md) to configure Azure Key Vault resources, and setup environment variables.

Then run the following command to run Server side app locally:

```shell
mvn clean spring-boot:run -Dspring-boot.run.profiles=client-auth
```

### Client side mTLS

Use the following steps to set environment variables for `ssl-bundles-rest-template`:

1. Open a new terminal and navigate to the `spring-cloud-azure-starter-keyvault-jca/ssl-bundles-server` directory.
2. Run the following command to set the `ssl-bundles-server` environment variables to the current terminal:
   
   ```shell
   source ./terraform/setup_env.sh
   ```

3. Change directory to `ssl-bundles-rest-template` project and follow [Run Locally](ssl-bundles-rest-template/README.md/#run-locally) to run and verify.

## mTLS for Server SSL and WebClient SSL

This mTLS scenario uses a web app as Server that enabled server SSL and needs client auth, and another reactive web app with `WebClient` SSL enabled. You need to use the `ssl-bundles-server` and `ssl-bundles-web-client` samples together. Azure resources created by either of these two sample projects can be shared with each other, so you don't need to create both.

### Server side mTLS:

Please refer to the [ssl-bundles-server/README.md](ssl-bundles-server/README.md) to configure Azure Key Vault resources, and setup environment variables.

Then run the following command to run Server side app locally:

```shell
mvn clean spring-boot:run -Dspring-boot.run.profiles=client-auth
```

### Client side mTLS

Use the following steps to set environment variables for `ssl-bundles-web-client`:

1. Open a new terminal and navigate to the `spring-cloud-azure-starter-keyvault-jca/ssl-bundles-server` directory.
2. Run the following command to set the `ssl-bundles-server` environment variables to the current terminal:

   ```shell
   source ./terraform/setup_env.sh
   ```

3. Change directory to `ssl-bundles-web-client` project and follow [Run Locally](ssl-bundles-web-client/README.md/#run-locally) to run and verify.
