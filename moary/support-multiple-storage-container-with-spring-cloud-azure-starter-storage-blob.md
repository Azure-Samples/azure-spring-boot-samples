## Support multiple Storage Container with Spring Cloud Azure Starter Storage Blob

This blog will demonstrate multiple Azure Storage containers operation based on `spring-cloud-azure-starter-storage-blob` in a single Spring Boot application, these containers can be in the same Storage Account or different Storage Account.

### Brief introduction for Spring Cloud Azure Starter Storage Blob

Spring Cloud Azure Starter Storage Blob implements the [Spring Resources](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#resources) for Azure Storage services which allows you to interact with Azure Storage Blob using Spring programming model, and provides a unified [authentication](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#authentication) method out of the box for Spring Cloud Azure to quickly integrate applications into Azure Services, as well as HTTP Client options, Log options, etc. See [Resource Handling for Spring Cloud Azure](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#resource-handling) for details.

You can try the [storage-blob-sample](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/main/storage/spring-cloud-azure-starter-storage-blob/storage-blob-sample) example.

### Limitations

This starter can support multiple storage container operations under the same storage account in a single application, the key point is that the blob paths under different containers can be normally resolved into objects `StorageBlobResource`. Looking at the source code, you can see the following logic:

```java
protected Resource getStorageResource(String location, Boolean autoCreate) {
    return new StorageBlobResource(this.getBlobServiceClient(), location, autoCreate);
}
```

If `blobServiceClient.getBlobContainerClient(getContainerName(location))` returns BlobServiceClient successfully, the blobs can be parsed successfully.

```java
public StorageBlobResource(BlobServiceClient blobServiceClient, String location, Boolean autoCreateFiles,
    String snapshot, String versionId, String contentType) {
    assertIsAzureStorageLocation(location);
    this.autoCreateFiles = autoCreateFiles == null ? isAutoCreateFiles(location) : autoCreateFiles;
    this.blobServiceClient = blobServiceClient;
    this.location = location;
    this.snapshot = snapshot;
    this.versionId = versionId;
    this.contentType = StringUtils.hasText(contentType) ? contentType : getContentType(location);
    Assert.isTrue(!(StringUtils.hasText(versionId) && StringUtils.hasText(snapshot)),
    "'versionId' and 'snapshot' can not be both set");
    this.blobContainerClient = blobServiceClient.getBlobContainerClient(getContainerName(location));
    BlobClient blobClient = blobContainerClient.getBlobClient(getFilename(location));
    if (StringUtils.hasText(versionId)) {
    blobClient = blobClient.getVersionClient(versionId);
    }
    if (StringUtils.hasText(snapshot)) {
    blobClient = blobClient.getSnapshotClient(snapshot);
    }
    this.blockBlobClient = blobClient.getBlockBlobClient();
}
```

The bean `BlobServiceClient` is a Storage Account level, so the current implementation does not support simultaneous availability in multiple Storage Accounts.

### How to support multiple Blob containers?

#### Multiple container clients with the same Storage Account

This scenario has already supported, the developers can use the bean `AzureStorageBlobProtocolResolver` or `ResourceLoader` to get the Azure blob resources.
Below is the sample code to read and write blobs in multiple containers under the same Storage Account.

##### Configuration

```yaml
spring:
  cloud:
    azure:
      storage:
        blob:
          account-name: ${FIRST_ACCOUNT}
          container-name: ${FIRST_CONTAINER}
          account-key: ${ACCOUNT_KEY_OF_FIRST_ACCOUNT}

extend:
  second-container: ${SECOND_CONTAINER}
```

Replace with your own variables.

##### Source code

```java
@RestController
@RequestMapping("/blob")
public class StorageAccountBlobController {

    final static Logger logger = LoggerFactory.getLogger(StorageAccountBlobController.class);
    private final String firstContainerName;
    private final String secondContainerName;
    private final ResourceLoader resourceLoader;
    private final AzureStorageBlobProtocolResolver azureStorageBlobProtocolResolver;

    public StorageAccountBlobController(@Value("${spring.cloud.azure.storage.blob.container-name}") String firstContainerName,
                                        @Value("${extend.second-container}") String secondContainerName,
                                        ResourceLoader resourceLoader,
                                        AzureStorageBlobProtocolResolver patternResolver) {
        this.firstContainerName = firstContainerName;
        this.secondContainerName = secondContainerName;
        this.resourceLoader = resourceLoader;
        this.azureStorageBlobProtocolResolver = patternResolver;
    }

    /**
     * Using AzureStorageBlobProtocolResolver to get Azure Storage Blob resources with file pattern.
     *
     * @return fileNames in the container match pattern: *.txt
     */
    @GetMapping("/first")
    public List<String> listTxtFiles() throws IOException {
        Resource[] resources = azureStorageBlobProtocolResolver.getResources("azure-blob://" + firstContainerName + "/*.txt");
        logger.info("{} resources founded with pattern:*.txt",resources.length);
        return Stream.of(resources).map(Resource::getFilename).collect(Collectors.toList());
    }

    @GetMapping("/first/{fileName}")
    public String readBlobResource(@PathVariable("fileName") String fileName) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + firstContainerName + "/" + fileName);
        return StreamUtils.copyToString(
            storageBlobResource.getInputStream(),
            Charset.defaultCharset());
    }

    @PostMapping("/first/{fileName}")
    public String writeBlobResource(@PathVariable("fileName") String fileName, @RequestBody String data) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + firstContainerName + "/" + fileName);
        try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "blob was updated";
    }

    @GetMapping("/second")
    public List<String> secondContainerListTxtFiles() throws IOException {
        Resource[] resources = azureStorageBlobProtocolResolver.getResources("azure-blob://" + secondContainerName + "/*.txt");
        logger.info("{} resources founded with pattern:*.txt",resources.length);
        return Stream.of(resources).map(Resource::getFilename).collect(Collectors.toList());
    }

    @GetMapping("/second/{fileName}")
    public String secondContainerReadBlobResource(@PathVariable("fileName") String fileName) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + secondContainerName + "/" + fileName);
        return StreamUtils.copyToString(
            storageBlobResource.getInputStream(),
            Charset.defaultCharset());
    }

    @PostMapping("/second/{fileName}")
    public String secondContainerWriteBlobResource(@PathVariable("fileName") String fileName, @RequestBody String data) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + secondContainerName + "/" + fileName);
        try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "blob was updated";
    }
}
```

The demo project is host in `multiple-storage-containter-sample`, once you configured the YAML file, you can use the maven command to run this scenario:

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=current
```

#### Multiple container clients with different Storage Account

After analyzing the current limitations, there's a simple solution is to use different Storage Accounts to hold different bean `BlobServiceClient` to operate different Containers under different Storage Account.

##### Extend the `AzureStorageBlobProtocolResolver` to support lookup bean `BlobServiceClient` by bean name.

Currently, the bean `BlobServiceClient` is only found by the class type, it is necessary to make it lookup by the bean name.

A bean name for `BlobServiceClient` will be added, then the resolver will look for `BlobServiceClient` by bean name with high priority.

```java
public final class ExtendAzureStorageBlobProtocolResolver extends AbstractAzureStorageProtocolResolver {

    private BlobServiceClient blobServiceClient;
    private String blobServiceClientBeanName;
    
    ......

    private BlobServiceClient getBlobServiceClient() {
        if (blobServiceClient == null) {
            if (StringUtils.hasText(blobServiceClientBeanName)) {
                blobServiceClient = beanFactory.getBean(blobServiceClientBeanName, BlobServiceClient.class);
            } else {
                blobServiceClient = beanFactory.getBean(BlobServiceClient.class);
            }
        }
        return blobServiceClient;
    }

    public void setBlobServiceClient(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    public void setBlobServiceClientBeanName(String blobServiceClientBeanName) {
        this.blobServiceClientBeanName = blobServiceClientBeanName;
    }
}
```

##### Override the bean `AzureStorageBlobProtocolResolver` for the default Storage Account

The default `AzureStorageBlobProtocolResolver` should be overridden to support lookup capability of the bean `BlobServiceClient`, this is used for the first Storage Account.

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ ExtendAzureStorageBlobProtocolResolver.class })
@ConditionalOnProperty(value = "spring.cloud.azure.storage.blob.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnAnyProperty(prefix = "spring.cloud.azure.storage.blob", name = { "account-name", "endpoint", "connection-string" })
public class DefaultAzureStorageBlobResourceAutoConfiguration {

    public static final String CURRENT_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME = "azureStorageBlobProtocolResolver";
    public static final String CURRENT_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME = "blobServiceClient";

    @Bean(CURRENT_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME)
    @ConditionalOnMissingBean(name = CURRENT_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME)
    public ExtendAzureStorageBlobProtocolResolver azureStorageBlobProtocolResolver() {
        ExtendAzureStorageBlobProtocolResolver azureStorageBlobProtocolResolver = new ExtendAzureStorageBlobProtocolResolver();
        azureStorageBlobProtocolResolver.setBlobServiceClientBeanName(CURRENT_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME);
        return azureStorageBlobProtocolResolver;
    }
}
```

The below blob protocol resolver is used for the second Storage Account.

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "extend.spring.cloud.azure.storage.blob.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnAnyProperty(prefix = "extend.spring.cloud.azure.storage.blob", name = { "account-name", "endpoint", "connection-string" })
public class ExtendAzureStorageBlobResourceAutoConfiguration {

    public static final String EXTEND_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME_PREFIX = "extendAzureStorageBlobProtocolResolver";

    @Bean(EXTEND_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME_PREFIX)
    @ConditionalOnMissingBean(name = EXTEND_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME_PREFIX)
    public ExtendAzureStorageBlobProtocolResolver extendAzureStorageBlobProtocolResolver() {
        ExtendAzureStorageBlobProtocolResolver extendAzureStorageBlobProtocolResolver =
            new ExtendAzureStorageBlobProtocolResolver();
        extendAzureStorageBlobProtocolResolver.setBlobServiceClientBeanName(EXTEND_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME);
        return extendAzureStorageBlobProtocolResolver;
    }
}
```

##### Add the Storage Blob beans for the second Storage Blob

The configuration `AzureStorageBlobAutoConfiguration` is used for the default bean `azureStorageBlobProtocolResolver`, the 
