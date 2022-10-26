## How to implement the Spring Resource from scratch

Spring’s Resource abstraction is a core module in Spring Framework, there are lots of built-in implementations to access low-level resources. Extending a new protocol Resource is to implement `AbstractResource` and `ResourcePatternResolver` interfaces.

This article will introduce the Spring Resource, take Spring Cloud Azure's implementation of Spring Resource as an example to analyze the current limitations and how to improve them.

### Introduction

The official [Spring Resources documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#resources) already provides a detailed description of those Spring Resources that Spring itself implements.

The following are the main implementation steps:
- Implement the interface `Resource` by inheriting `AbstractResource`.
- Implement the interface `ResourcePatternResolver` to resolve the first step resource, and add this resolver to the resolver set in the default resource loader through the `org.springframework.core.io.DefaultResourceLoader#addProtocolResolver` method.
- Register the implementation of `ResourcePatternResolver` as a bean.

When resolving a resource via a resource location, it is actually resolved with a resource loader `ResourceLoader`, which will first use all the protocol pattern resolver set to loop through each resolver to resolve until a non-null resource is returned. If it has not been successfully returned, it will be resolved with the built-in pattern resolvers.

The following are the commonly used [Resource implementations](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#resources-implementations) built in Spring:

- UrlResource
- ClassPathResource
- FileSystemResource
- PathResource
- ServletContextResource
- InputStreamResource
- ByteArrayResource

Of course, each resource will have a corresponding resource resolver, we can learn and refer very well.

### Spring Resource in Spring Cloud Azure

Spring Cloud Azure provides two Spring resource and resource pattern resolver implementations, this time we only discuss the implementation of Azure Storage Blob resource. You can further view the source code [Spring Resources in Spring Cloud Azure](https://github.com/Azure/azure-sdk-for-java/tree/spring-cloud-azure-starter-storage-blob_4.2.0/sdk/spring/spring-cloud-azure-core/src/main/java/com/azure/spring/cloud/core/resource) and documentation [Resource Handling](https://microsoft.github.io/spring-cloud-azure/4.2.0/reference/html/index.html#resource-handling) of Spring Cloud Azure.

NOTE:
We use [Spring Cloud Azure Starter Storage Blob version 4.2.0](https://mvnrepository.com/artifact/com.azure.spring/spring-cloud-azure-starter-storage-blob/4.2.0) for analysis and experiments. 

#### Implementation of `AbstractResource`

There's an abstract implementation `AzureStorageResource` for Spring Cloud Azure, it mainly defines the format of the Azure storage resource protocol and adapts to the unique concepts in the Azure Storage Account service, such as identifying the container name and returning the file name, it is also not coupled with the Azure Storage SDK.
The interface WritableResource will enable the ability to write to the Azure Storage resource.

```java
abstract class AzureStorageResource extends AbstractResource implements WritableResource {

    private boolean isAzureStorageResource(@NonNull String location) {
        ......
    }

    String getContainerName(String location) {
        ......
    }

    String getContentType(String location) {
        ......
    }

    String getFilename(String location) {
        ......
    }

    abstract StorageType getStorageType();
}
```

The resource `StorageBlobResource` is Spring Cloud Azure Storage Blob's implementation of the abstract class `AbstractResource`.
We can see `StorageBlobResource` uses the `BlobServiceClient` of Azure Storage Blob SDK to implement all the abstract method, which strongly relies on a service client to interact with the Azure Storage Blob service.

```java
public final class StorageBlobResource extends AzureStorageResource {
    private final BlobServiceClient blobServiceClient;
    private final BlobContainerClient blobContainerClient;
    private final BlockBlobClient blockBlobClient;
    
    public StorageBlobResource(BlobServiceClient blobServiceClient, String location, Boolean autoCreateFiles,
                               String snapshot, String versionId, String contentType) {
        ......
        this.blobContainerClient = blobServiceClient.getBlobContainerClient(getContainerName(location));
        BlobClient blobClient = blobContainerClient.getBlobClient(getFilename(location));
        this.blockBlobClient = blobClient.getBlockBlobClient();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        try {
            ......
            return this.blockBlobClient.getBlobOutputStream(options);
        } catch (BlobStorageException e) {
            throw new IOException(MSG_FAIL_OPEN_OUTPUT, e);
        }
    }
    
    ......
    
    @Override
    StorageType getStorageType() {
        return StorageType.BLOB;
    }
}
```

#### Implementation of `ResourcePatternResolver`

Spring Cloud Azure has an abstract implementation `AbstractAzureStorageProtocolResolver`, which mainly implements the general processing of the Azure storage resource protocol, abstracts the specific concepts in the Azure Storage Account service, and completes the logic added to the default resource loader. It is not coupled with the Azure Storage SDK.

```java
public abstract class AbstractAzureStorageProtocolResolver implements ProtocolResolver, ResourcePatternResolver,
    ResourceLoaderAware, BeanFactoryPostProcessor {

    protected final AntPathMatcher matcher = new AntPathMatcher();

    protected abstract StorageType getStorageType();

    protected abstract Resource getStorageResource(String location, Boolean autoCreate);

    protected ConfigurableListableBeanFactory beanFactory;

    protected abstract Stream<StorageContainerItem> listStorageContainers(String containerPrefix);

    protected abstract StorageContainerClient getStorageContainerClient(String name);

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        if (resourceLoader instanceof DefaultResourceLoader) {
            ((DefaultResourceLoader) resourceLoader).addProtocolResolver(this);
        } else {
            LOGGER.warn("Custom Protocol using azure-{}:// prefix will not be enabled.", getStorageType().getType());
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        if (AzureStorageUtils.isAzureStorageResource(location, getStorageType())) {
            return getResource(location);
        }
        return null;
    }

    @Override
    public Resource[] getResources(String pattern) throws IOException {
        Resource[] resources = null;

        if (AzureStorageUtils.isAzureStorageResource(pattern, getStorageType())) {
            if (matcher.isPattern(AzureStorageUtils.stripProtocol(pattern, getStorageType()))) {
                String containerPattern = AzureStorageUtils.getContainerName(pattern, getStorageType());
                String filePattern = AzureStorageUtils.getFilename(pattern, getStorageType());
                resources = resolveResources(containerPattern, filePattern);
            } else {
                return new Resource[] { getResource(pattern) };
            }
        }
        if (null == resources) {
            throw new IOException("Resources not found at " + pattern);
        }
        return resources;
    }

    @Override
    public Resource getResource(String location) {
        Resource resource = null;

        if (AzureStorageUtils.isAzureStorageResource(location, getStorageType())) {
            resource = getStorageResource(location, true);
        }

        if (null == resource) {
            throw new IllegalArgumentException("Resource not found at " + location);
        }
        return resource;
    }

    /**
     * Storage container item.
     */
    protected static class StorageContainerItem {
        private final String name;
        ......
    }

    protected static class StorageItem {

        private final String container;
        private final String name;
        private final StorageType storageType;
        ......
    }

    protected interface StorageContainerClient {
        
        ......
    }
}
```

The resource resolver `AzureStorageBlobProtocolResolver` is Spring Cloud Azure Storage Blob's implementation of `ResourcePatternResolver`. It implements the encapsulation of resources according to the location or storage item pattern based on `BlobServiceClient`, and finally returns the resource `StorageBlobResource`.

```java
public final class AzureStorageBlobProtocolResolver extends AbstractAzureStorageProtocolResolver {

    private BlobServiceClient blobServiceClient;
    @Override
    protected StorageType getStorageType() {
        return StorageType.BLOB;
    }

    @Override
    protected Resource getStorageResource(String location, Boolean autoCreate) {
        return new StorageBlobResource(getBlobServiceClient(), location, autoCreate);
    }

    private BlobServiceClient getBlobServiceClient() {
        if (blobServiceClient == null) {
            blobServiceClient = beanFactory.getBean(BlobServiceClient.class);
        }
        return blobServiceClient;
    }
}
```

### Limitations

After learning about Spring Cloud Azure Starter Storage Blob, I found the following usage limitations.

The storage blob resource can support multiple container operations under the same storage account in a single application, the key point is that the blob paths under different containers can be normally resolved into objects `StorageBlobResource`. Combining the above source code `StorageBlobResource`, the blob resource must hold a blob service client,  
if `blobServiceClient.getBlobContainerClient(getContainerName(location))` returns BlobServiceClient successfully, the blob resource can be resolved successfully.

The bean `BlobServiceClient` is an Azure Storage Account level in Azure Storage Blob SDK, so the current implementation does not support simultaneous availability in multiple Azure Storage Accounts.

### Developing an extended version of Spring Cloud Azure Starter Storage Blob

After analyzing the current limitations, I have a workaround to use multiple Storage Accounts to hold its own bean `BlobServiceClient` to operate Containers under different storage accounts.

All changes will be in a new library, let's call it `spring-cloud-azure-starter-storage-blob-extend`, which will only depend on `spring-cloud-azure-starter-storage-blob`.

#### Extend the Storage Blob properties

The goal is to support to configure the multiple storage account, it's recommended to use the same structure with `AzureStorageBlobProperties` to reduce the user learning costs, and do not break Spring Cloud Azure 4.0's out of box authentication features. 

```java
public class ExtendAzureStorageBlobsProperties {

    public static final String PREFIX = "spring.cloud.azure.storage.blobs";

    private boolean enabled = true;

    private final List<AzureStorageBlobProperties> configurations = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<AzureStorageBlobProperties> getConfigurations() {
        return configurations;
    }
}
```

#### Dynamically register storage blob beans

Since there will be multiple Storage Account configurations, it is necessary to add names to the beans corresponding to each storage account, using the account name is the best choice. 

The next step is to dynamically register these beans into the Spring context.

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = { "spring.cloud.azure.storage.blobs.enabled"}, havingValue = "true")
public class ExtendStorageBlobsAutoConfiguration implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;

    public static final String EXTEND_STORAGE_BLOB_PROPERTIES_BEAN_NAME = "extendAzureStorageBlobsProperties";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        AzureGlobalProperties azureGlobalProperties =
            Binder.get(environment)
                  .bind(AzureGlobalProperties.PREFIX, AzureGlobalProperties.class)
                  .orElse(new AzureGlobalProperties());
        ExtendAzureStorageBlobsProperties blobsProperties =
            Binder.get(environment)
                  .bind(ExtendAzureStorageBlobsProperties.PREFIX, ExtendAzureStorageBlobsProperties.class)
                  .orElseThrow(() -> new IllegalArgumentException("Can not bind the azure storage blobs properties."));
        // merge properties
        for (AzureStorageBlobProperties azureStorageBlobProperties : blobsProperties.getConfigurations()) {
            AzureStorageBlobProperties transProperties = new AzureStorageBlobProperties();
            AzureGlobalPropertiesUtils.loadProperties(azureGlobalProperties, transProperties);
            copyAzureCommonPropertiesIgnoreTargetNull(transProperties, azureStorageBlobProperties);
        }

        DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
        registryBeanExtendAzureStorageBlobsProperties(factory, blobsProperties);
        blobsProperties.getConfigurations().forEach(blobProperties -> registryBlobBeans(factory, blobProperties));
    }

    private void registryBeanExtendAzureStorageBlobsProperties(DefaultListableBeanFactory beanFactory,
                                                               ExtendAzureStorageBlobsProperties blobsProperties) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ExtendAzureStorageBlobsProperties.class,
            () -> blobsProperties);
        AbstractBeanDefinition rawBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        beanFactory.registerBeanDefinition(EXTEND_STORAGE_BLOB_PROPERTIES_BEAN_NAME, rawBeanDefinition);
    }

    private void registryBlobBeans(DefaultListableBeanFactory beanFactory, AzureStorageBlobProperties blobProperties) {
        String accountName = getStorageAccountName(blobProperties);
        Assert.hasText(accountName, "accountName can not be null or empty.");
        registryBeanStaticConnectionStringProvider(beanFactory, blobProperties, accountName);
        registryBeanBlobServiceClientBuilderFactory(beanFactory, blobProperties, accountName);
        registryBeanBlobServiceClientBuilder(beanFactory, accountName);
        registryBeanBlobServiceClient(beanFactory, accountName);
        registryBeanBlobContainerClient(beanFactory, blobProperties, accountName);
        registryBeanBlobClient(beanFactory, blobProperties, accountName);
    }

    private void registryBeanBlobServiceClientBuilder(DefaultListableBeanFactory beanFactory,
                                                      String accountName) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(BlobServiceClientBuilder.class,
            () -> {
                BlobServiceClientBuilderFactory builderFactory =
                    beanFactory.getBean(accountName + BlobServiceClientBuilderFactory.class.getSimpleName(),
                        BlobServiceClientBuilderFactory.class);
                return builderFactory.build();
            });
        AbstractBeanDefinition rawBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        beanFactory.registerBeanDefinition(
            accountName + BlobServiceClientBuilder.class.getSimpleName(), rawBeanDefinition);
    }
    
    ......
    
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
```

#### Extend the `AzureStorageBlobProtocolResolver`

This is target to make any container resolvable by the same resource partten resolver. A new storage blob resource location will like this **azure-blob-accountname://containername/test.txt**, the resolver will look up the bean `BlobServiceClient` by Azure Storage Account name.

```java
public class ExtendAzureStorageBlobProtocolResolver extends ExtendAbstractAzureStorageProtocolResolver {
    private final Map<String, BlobServiceClient> blobServiceClientMap = new HashMap<>();

    @Override
    protected Resource getStorageResource(String location, Boolean autoCreate) {
        return new ExtendStorageBlobResource(getBlobServiceClient(location), location, autoCreate);
    }

    private BlobServiceClient getBlobServiceClient(String locationPrefix) {
        String storageAccount = ExtendAzureStorageUtils.getStorageAccountName(locationPrefix, getStorageType());
        Assert.notNull(storageAccount, "storageAccount can not be null.");
        String accountKey = storageAccount.toLowerCase(Locale.ROOT);
        if (blobServiceClientMap.containsKey(accountKey)) {
            return blobServiceClientMap.get(accountKey);
        }

        BlobServiceClient blobServiceClient = beanFactory.getBean(
            accountKey + BlobServiceClient.class.getSimpleName(), BlobServiceClient.class);
        Assert.notNull(blobServiceClient, "blobServiceClient can not be null.");
        blobServiceClientMap.put(accountKey, blobServiceClient);
        return blobServiceClient;
    }
}
```

Again, you need to add the bean `ExtendAzureStorageBlobProtocolResolver` to the Spring context.

### Testing the Spring Cloud Azure Starter Storage Blob Extend

You can use [start.spring.io](https://start.spring.io/) to generate a Spring Boot 2.6.7 project with *Azure Storage* support (Or modify based on this [storage blob sample](https://github.com/Azure-Samples/azure-spring-boot-samples/tree/spring-cloud-azure_v4.2.0/storage/spring-cloud-azure-starter-storage-blob/storage-blob-sample) instead).


Add the extending starter dependency to the **pom.xml** file:

```xml
<dependency>
  <groupId>com.azure.spring.extend</groupId>
  <artifactId>spring-cloud-azure-starter-storage-blob-extend</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

Delete the src/main/resources/application.properties file, or add the following configuration file *application-extend.yml*, which enables multiple storage account usage:

**application-extend.yml**
```yaml
spring:
  cloud:
    azure:
      storage:
        blob:
          enabled: false
        blobs:
          enabled: true
          configurations:
            - account-name: ${FIRST_ACCOUNT}
              container-name: ${FIRST_CONTAINER}
              account-key: ${ACCOUNT_KEY_OF_FIRST_ACCOUNT}
            - account-name: ${SECOND_ACCOUNT}
              container-name: ${SECOND_CONTAINER}
              account-key: ${ACCOUNT_KEY_OF_SECOND_ACCOUNT}
```

Set or replace these environment variable values with specific Azure Storage Account resource information.

Add class `com.azure.spring.extend.sample.storage.resource.extend.SampleDataInitializer` contents with following:

```java
@Profile("extend")
@Component
public class SampleDataInitializer implements CommandLineRunner {
    final static Logger logger = LoggerFactory.getLogger(SampleDataInitializer.class);

    private final ConfigurableEnvironment env;

    private final ExtendAzureStorageBlobProtocolResolver resolver;
    private final ExtendAzureStorageBlobsProperties properties;

    public SampleDataInitializer(ConfigurableEnvironment env, ExtendAzureStorageBlobProtocolResolver resolver,
                                 ExtendAzureStorageBlobsProperties properties) {
        this.env = env;
        this.resolver = resolver;
        this.properties = properties;
    }

    /**
     * This is used to initialize some data for each Azure Storage Account Blob container.
     */
    @Override
    public void run(String... args) {
        properties.getConfigurations().forEach(this::writeDataByStorageAccount);
    }

    private void writeDataByStorageAccount(AzureStorageBlobProperties blobProperties) {
        String containerName = blobProperties.getContainerName();
        if (!StringUtils.hasText(containerName) || blobProperties.getAccountName() == null) {
            return;
        }

        String accountName = getStorageAccountName(blobProperties);
        logger.info("Begin to initialize the {} container of the {} account", containerName, accountName);
        long currentTimeMillis = System.currentTimeMillis();
        String fileName = "fileName-" + currentTimeMillis;
        String data = "data" + currentTimeMillis;
        Resource storageBlobResource = resolver.getResource("azure-blob-" + accountName + "://" + containerName +"/" + fileName + ".txt");
        try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
            os.write(data.getBytes());
            logger.info("Write data to container={}, fileName={}.txt", containerName, fileName);
        } catch (IOException e) {
            logger.error("Write data exception", e);
        }
        logger.info("End to initialize the {} container of the {} account", containerName, accountName);
    }
}
```

Run the sample with following Maven command:

```shell
mvn clean spring-boot:run -Dspring-boot.run.profiles=extend
```

Verify this sample with below output:

```text
c.a.s.e.s.s.r.e.SampleDataInitializer    : Begin to initialize the container first of the account firstaccount.
c.a.s.e.s.s.r.e.SampleDataInitializer    : Write data to container=first, fileName=fileName-1656641340271.txt
c.a.s.e.s.s.r.e.SampleDataInitializer    : End to initialize the container first of the account firstaccount.
c.a.s.e.s.s.r.e.SampleDataInitializer    : Begin to initialize the container second of the account secondaccount.
c.a.s.e.s.s.r.e.SampleDataInitializer    : Write data to container=second, fileName=fileName-1656641343572.txt
c.a.s.e.s.s.r.e.SampleDataInitializer    : End to initialize the container second of the account secondaccount.
```

You can see the full sample project code [spring-cloud-azure-starter-storage-blob-extend-sample](https://github.com/moarychan/spring-cloud-azure-starter-storage-blob-extend-sample/tree/main/spring-cloud-azure-starter-storage-blob-extend-sample) here.

You can still use the original usage of Spring Cloud Azure Starter Storage Blob, add the following configuration file *application-current.yml*, which enables single storage account usage:

```yaml
spring:
  cloud:
    azure:
      storage:
        blob:
          account-name: ${FIRST_ACCOUNT}
          container-name: ${FIRST_CONTAINER}
          account-key: ${ACCOUNT_KEY_OF_FIRST_ACCOUNT}
current:
  second-container: ${SECOND_CONTAINER}
```

Set or replace these environment variable values with specific Azure Storage Account resource information.

Run the sample with following Maven command:

```shell
mvn clean spring-boot:run -Dspring-boot.run.profiles=current
```

Verify this sample with below output:

```text
c.a.s.e.s.s.r.c.SampleDataInitializer    : StorageApplication data initialization of 'first-container' begin ...
c.a.s.e.s.s.r.c.SampleDataInitializer    : Write data to container=first-container, fileName=fileName1656641162614.txt
c.a.s.e.s.s.r.c.SampleDataInitializer    : StorageApplication data initialization of 'first-container' end ...
c.a.s.e.s.s.r.c.SampleDataInitializer    : StorageApplication data initialization of 'second-container' begin ...
c.a.s.e.s.s.r.c.SampleDataInitializer    : Write data to container=second-container, fileName=fileName1656641165411.txt
c.a.s.e.s.s.r.c.SampleDataInitializer    : StorageApplication data initialization of 'second-container' end ...
```

### Conclusion

Implementing a specific resource type and corresponding pattern resolver is actually relatively simple because the official documentation is very clear, there are many built-in implementations, and they are widely used in the Spring technology stack.

I think another point that may need to be considered is the definition of the protocol of the resource, just like the Azure Storage Blob Resource, whether we use **azure-blob://** or **azure-blob-[account-name]://** is still very different, and the former limitation is relatively large. For the identifier of a network resource, we should consider the uniqueness of the resource, but the latter may lead to a long name and expose the name of the storage account. These need to be considered according to their own situation and then make decisions.

References & Useful Resources
- [The latest Spring Resources documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#resources)
- [My extended starter for Spring Cloud Azure Starter Storage Blob and sample on GitHub](https://github.com/moarychan/spring-cloud-azure-starter-storage-blob-extend-sample)
