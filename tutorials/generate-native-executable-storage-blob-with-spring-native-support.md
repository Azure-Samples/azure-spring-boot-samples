# Generate a native executable to use Spring Cloud Azure Storage Blob Starter with Spring Native support

This article demonstrates building a native executable based on Spring Cloud Azure Storage Blob Starter.

In this tutorial, you will learn how to:

* Create a Spring Cloud Azure application using the Azure Spring Initializr
* Use Spring Cloud Azure Storage Blob Starter to interact with Azure Storage Blob
* Build a container containing a native executable on Windows
* Build a native executable on Windows

## Prerequisites

The following prerequisites are required in order to complete the steps in this article:

* An Azure subscription. If you don't already have an Azure subscription, you can activate your [MSDN subscriber benefits] or sign up for a [free Azure account].
* [Java 11 JDK](../fundamentals/java-jdk-install.md)
* [Apache Maven](http://maven.apache.org/), version 3.0 or later.
* [Docker](https://docs.docker.com/installation/#installation) for [Buildpacks](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-buildpacks-system-requirements) usage
* [Native Image](https://www.graalvm.org/22.0/reference-manual/native-image/) for [Native Build Tools](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#getting-started-native-image-system-requirements) usage

## Create a custom application using the Azure Spring Initializr

1. Browse to <https://aka.ms/spring/start>.

2. Specify that you want to generate a **Maven** project with **Java**, select Java version **11**, and enter the **Group** and **Artifact** names for your application.

3. Select the button **ADD DEPENDENCIES** to select **Azure Storage Blob** and **Spring Native [Experimental]** dependencies.

4. Scroll to the bottom of the page and select the button **GENERATE** to download the project source code.

   ![Basic Azure Spring Initializr options][SI01]

   > [!NOTE]
   >
   > The Azure Spring Initializr will do a check and automatically add "com.azure.spring:spring-cloud-azure-native-configuration" dependency when the relevant Spring Cloud Azure libraries and Spring Native dependency are selected.
   >

1. After you have extracted the files on your local system, your custom Spring Boot application will be ready for editing.

   ![Custom Spring Boot project files][SI02]

## Configure the Storage Account resource on Azure

1. Browse to the Azure portal at <https://portal.azure.com/> and select **Create a resource**.

2. select **Storage**, and then select **Storage Account**.

   ![Selecting Storage Account in the Azure portal.][AZ01]

3. On the **Create a storage account** page, specify the following information:

    * Specify your **Subscription**, **Resource group**.
    * Enter the **Storage account name** for your storage account, such as *springnativestorage*
    * Specify your **Region**, **Performance**.

   Select **Review + create**, review your specifications, and select **Create**.

4. Once your storage account has been completed, you will see it listed on your Azure **Dashboard**, as well as under the **All resources**, and **Storage Account** pages. You can select on your storage account on any of those locations to open the properties page for your storage account.

   ![Resource provisioned in the Azure portal.][AZ02]

5. When the page that contains the list of properties for your storage account is displayed, select **Containers**, select **+Container** and select **Create** to create a container.

   ![Create a container in the Azure portal.][AZ03]

   Specify your container **name**, such as *spring-native*, then select **Create** to create the container.

7. Select **Access keys** in left menu, select **Show keys** and copy your access keys for your storage account.

   ![Copy the access keys under the Access keys section.][AZ04]


## Configure your custom Spring Boot application to interact with your Storage Blob

1. Locate the *application.properties* file in the *resources* directory of your app.

2. Open the *application.properties* file in a text editor, copy the below lines to the file, then replace and save the values with the appropriate properties from your storage account.

   ```properties
   # Specify your Storage Account name.
   spring.cloud.azure.storage.blob.account-name=springnativestorage
   # Specify the container name for your Storage Account.
   spring.cloud.azure.storage.blob.container-name=spring-native
   # Specify the access key for your Storage Account.
   spring.cloud.azure.storage.blob.account-key=<your-storage-account-access-key>
   ```

3. Create a new file named *BlobController.java* with the below code, and save it with the file *StorageBlobNativeApplication.java*.

   ```java
   package com.contoso.storageblobnative;
   
   import com.azure.spring.cloud.core.resource.AzureStorageBlobProtocolResolver;
   import org.slf4j.Logger;
   import org.slf4j.LoggerFactory;
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.core.io.Resource;
   import org.springframework.core.io.ResourceLoader;
   import org.springframework.core.io.WritableResource;
   import org.springframework.util.StreamUtils;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.PathVariable;
   import org.springframework.web.bind.annotation.PostMapping;
   import org.springframework.web.bind.annotation.RequestBody;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RestController;
   
   import java.io.IOException;
   import java.io.OutputStream;
   import java.nio.charset.Charset;
   import java.util.List;
   import java.util.stream.Collectors;
   import java.util.stream.Stream;
   
   @RestController
   @RequestMapping("blob")
   public class BlobController {
   
       final static Logger logger = LoggerFactory.getLogger(BlobController.class);
       private final String containerName;
       private final ResourceLoader resourceLoader;
       private final AzureStorageBlobProtocolResolver azureStorageBlobProtocolResolver;
   
       public BlobController(@Value("${spring.cloud.azure.storage.blob.container-name}") String containerName,
                             ResourceLoader resourceLoader,
                             AzureStorageBlobProtocolResolver patternResolver) {
           this.containerName = containerName;
           this.resourceLoader = resourceLoader;
           this.azureStorageBlobProtocolResolver = patternResolver;
       }
   
       /**
        * Using AzureStorageBlobProtocolResolver to get Azure Storage Blob resources with file pattern.
        * @return fileNames in the container match pattern: *.txt
        */
       @GetMapping
       public List<String> listTxtFiles() throws IOException {
           Resource[] resources = azureStorageBlobProtocolResolver.getResources("azure-blob://" + containerName + "/*.txt");
           logger.info("{} resources founded with pattern:*.txt",resources.length);
           return Stream.of(resources).map(Resource::getFilename).collect(Collectors.toList());
       }
   
       @GetMapping("/{fileName}")
       public String readBlobResource(@PathVariable("fileName") String fileName) throws IOException {
           Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + containerName + "/" + fileName);
           return StreamUtils.copyToString(
               storageBlobResource.getInputStream(),
               Charset.defaultCharset());
       }
   
       @PostMapping("/{fileName}")
       public String writeBlobResource(@PathVariable("fileName") String fileName, @RequestBody String data) throws IOException {
           Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + containerName + "/" + fileName);
           try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
               os.write(data.getBytes());
           }
           return "blob was updated";
       }
   }
   ```

5. Create a new file named *SampleDataInitializer* with the below code, and save it with the file *StorageBlobNativeApplication.java* too.

   ```java
   import org.slf4j.Logger;
   import org.slf4j.LoggerFactory;
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.boot.CommandLineRunner;
   import org.springframework.core.io.Resource;
   import org.springframework.core.io.ResourceLoader;
   import org.springframework.core.io.WritableResource;
   import org.springframework.stereotype.Component;
   import org.springframework.util.StreamUtils;
   
   import java.io.OutputStream;
   import java.nio.charset.Charset;
   
   @Component
   public class SampleDataInitializer implements CommandLineRunner {
       final static Logger logger = LoggerFactory.getLogger(SampleDataInitializer.class);
   
       private final ResourceLoader resourceLoader;
   
       private final String containerName;
   
       public SampleDataInitializer(@Value("${spring.cloud.azure.storage.blob.container-name}") String containerName,
                                    ResourceLoader resourceLoader) {
           this.containerName = containerName;
           this.resourceLoader = resourceLoader;
       }
   
       /**
        * This is used to initialize some data in Azure Storage Blob.
        */
       @Override
       public void run(String... args) throws Exception {
           logger.info("StorageApplication data initialization begin ...");
           long millis = System.currentTimeMillis();
           String fileName = "fileName-" + millis + ".txt";
           String filePath = "azure-blob://" + containerName + "/" + fileName;
           Resource storageBlobResource = resourceLoader.getResource(filePath);
           try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
               String data = "data-" + millis;
               os.write(data.getBytes());
               logger.info("Write data to container={}, filePath={}", containerName, filePath);
           }
           String downloadedData = StreamUtils.copyToString(resourceLoader.getResource(filePath).getInputStream(),
               Charset.defaultCharset());
           logger.info("Downloaded data from the azure storage blob resource: {}", downloadedData);
           logger.info("Get the data content through this address 'curl -XGET http://localhost:8080/blob/{}'.",
               fileName);
           logger.info("StorageApplication data initialization end ...");
       }
   }
   ```

6. Use the following Maven command to run local tests against the JVM:

   ```shell
   mvn clean package spring-boot:run
   ```

7. Check out the following console log:

   ```text
   StorageApplication data initialization begin ...
   Write data to container=blobcontainer, filePath=azure-blob://blobcontainer/fileName-*.txt
   Downloaded data from the azure storage blob resource: data-*
   Get the data content through this address 'curl -XGET http://localhost:8080/blob/fileName-*.txt'.
   StorageApplication data initialization end ...
   ```
   
## Build the native application with Buildpacks

This section uses the [Cloud Native Buildpacks](https://docs.spring.io/spring-boot/docs/current/reference/html/container-images.html#container-images.buildpacks) to build a container. [Liberica Native Image Kit](https://bell-sw.com/pages/liberica-native-image-kit/) (NIK) is the native-image compiler distribution used by default with Buildpacks. Make sure your Docker daemon is reachable first.

> [!NOTE]
> On Microsoft Windows, make sure to enable the [Docker WSL 2 backend](https://docs.docker.com/docker-for-windows/wsl/) for better performance. On MacOS, it is recommended to increase the memory allocated to Docker to at least `8GB`, and potentially add more CPUs as well.
> 

### Build the native application

The step will create a Linux container to build the native application using the GraalVM native image compiler. By default, the container image is installed locally.
Go to the project root directory, open the `Command Prompt` tools and execute the following Maven command:
```shell
mvn -DskipTests spring-boot:build-image
```

It will take a long time, please wait patiently to build the container.

![Build image using Command Prompt.][BP01]

After a long wait, it finally echoes that the build is successful.

![Build container complete.][BP02]

### Run the container containing the native executable

To run the application, you can use docker the usual way as shown in the following example:

```bash
docker run --rm -p 8080:8080 storage-blob-native:0.0.1-SNAPSHOT
```

Check out the output console log, it will be the same with the previous local running log.

![Run Docker container.][BP03]

## Build with Native Build Tools

This section uses the [GraalVM native build tools](https://github.com/graalvm/native-build-tools) to build a native executable.

A number of [prerequisites](https://www.graalvm.org/reference-manual/native-image/#prerequisites) are required before installing the GraalVM native-image compiler. 
The prerequisites for using native image on Windows is to install [Visual Studio](https://visualstudio.microsoft.com/vs/) and Microsoft Visual C++ (MSVC). The native-image builder will only work when it is executed from the `x64 Native Tools Command Prompt`.

### Build the native application

This step will create a native executable file.
Go to the project root directory, open `x64 Native Tools Command Prompt` tools and execute the following Maven command:

```shell
mvn -Pnative -DskipTests package
```

It will take a long time, please wait patiently.

![Build image using x64 Native Tools Command Prompt.][NT01]

After a long wait, it finally echoes that the build is successful.

![Build native executable complete.][NT02]

### Run the native executable

To run the application, You can start just like running a Windows executable:

```bash
target\storage-blob-native
```

Check out the output console log, it will be the same with the previous local running log.

![Run native executable.][NT03]

## Clean up resources

When no longer needed, use the [Azure portal](https://portal.azure.com/) to delete the resources created in this article to avoid unexpected charges.

## Next steps

To learn more about Spring and Azure, continue to the Spring on Azure documentation center.

> [!div class="nextstepaction"]
> [Spring on Azure](./index.yml)

### Additional Resources

For more information about using Spring Native support on Azure, see the following resources:

* [Reference docs for Spring Native Support](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#spring-native-support)

* [Samples for Spring Native Support](https://github.com/Azure-Sample/azure-spring-boot-samples#run-samples-based-on-spring-native)

<!-- URL List -->

[MSDN subscriber benefits]: https://azure.microsoft.com/pricing/member-offers/msdn-benefits-details/
[Spring Boot]: https://spring.io/projects/spring-boot/
[Azure Spring Initializr]: https://aka.ms/spring/start
[Spring Framework]: https://spring.io/

<!-- IMG List -->

[SI01]: media/generate-native-executable-storage-blob-with-spring-native-support/SI01.png
[SI02]: media/generate-native-executable-storage-blob-with-spring-native-support/SI02.png
[AZ01]: media/generate-native-executable-storage-blob-with-spring-native-support/AZ01.png
[AZ02]: media/generate-native-executable-storage-blob-with-spring-native-support/AZ02.png
[AZ03]: media/generate-native-executable-storage-blob-with-spring-native-support/AZ03.png
[AZ04]: media/generate-native-executable-storage-blob-with-spring-native-support/AZ04.png
[BP01]: media/generate-native-executable-storage-blob-with-spring-native-support/BP01.png
[BP02]: media/generate-native-executable-storage-blob-with-spring-native-support/BP02.png
[BP03]: media/generate-native-executable-storage-blob-with-spring-native-support/BP03.png
[NT01]: media/generate-native-executable-storage-blob-with-spring-native-support/NT01.png
[NT02]: media/generate-native-executable-storage-blob-with-spring-native-support/NT02.png
[NT03]: media/generate-native-executable-storage-blob-with-spring-native-support/NT03.png
