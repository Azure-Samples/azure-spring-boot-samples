package com.azure.spring.sample.storage.resource.extend;

import com.azure.spring.cloud.autoconfigure.condition.ConditionalOnAnyProperty;
import com.azure.spring.cloud.autoconfigure.condition.ConditionalOnMissingProperty;
import com.azure.spring.cloud.autoconfigure.context.AzureGlobalProperties;
import com.azure.spring.cloud.autoconfigure.implementation.properties.utils.AzureGlobalPropertiesUtils;
import com.azure.spring.cloud.core.customizer.AzureServiceClientBuilderCustomizer;
import com.azure.spring.cloud.core.implementation.util.AzureSpringIdentifier;
import com.azure.spring.cloud.core.provider.connectionstring.ServiceConnectionStringProvider;
import com.azure.spring.cloud.core.provider.connectionstring.StaticConnectionStringProvider;
import com.azure.spring.cloud.core.service.AzureServiceType;
import com.azure.spring.cloud.service.implementation.storage.blob.BlobServiceClientBuilderFactory;
import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.azure.spring.sample.storage.resource.extend.ExtendAzureStorageBlobProperties.EXTEND_PREFIX;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(DefaultAzureStorageBlobResourceAutoConfiguration.class)
@EnableConfigurationProperties
@ConditionalOnMissingProperty(prefix = "extend", name = "second-container")
@ConditionalOnProperty(value = "extend.spring.cloud.azure.storage.blob.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnAnyProperty(prefix = "extend.spring.cloud.azure.storage.blob", name = { "account-name", "endpoint", "connection-string" })
public class ExtendStorageAccountAutoConfiguration {

    public static final String EXTEND_STORAGE_BLOB_PROPERTIES_BEAN_NAME = "extendAzureStorageBlobProperties";
    public static final String EXTEND_STATIC_STORAGE_BLOB_CONNECTION_STRING_PROVIDER_BEAN_NAME = "extendStaticStorageBlobConnectionStringProvider";
    public static final String EXTEND_AZURE_SERVICE_CLIENT_BUILDER_CUSTOMIZER_BEAN_NAME = "extendAzureServiceClientBuilderCustomizer";
    public static final String EXTEND_STORAGE_BLOB_CLIENT_BUILDER_FACTORY_BEAN_NAME = "extendSpringCloudAzureStorageBlobClientBuilderFactory";
    public static final String EXTEND_STORAGE_BLOB_CLIENT_BUILDER_BEAN_NAME = "extendSpringCloudAzureStorageBlobClientBuilder";

    static final String EXTEND_STORAGE_BLOB_CONTAINER_CLIENT_BEAN_NAME = "extendContainerClient";
    static final String EXTEND_STORAGE_BLOB_ASYNC_CONTAINER_CLIENT_BEAN_NAME = "extendAsyncContainerClient";
    static final String EXTEND_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME = "extendServiceClient";
    static final String EXTEND_STORAGE_BLOB_ASYNC_SERVICE_CLIENT_BEAN_NAME = "extendAsyncServiceClient";
    static final String EXTEND_STORAGE_BLOB_CLIENT_BEAN_NAME = "extendBlobClient";
    static final String EXTEND_STORAGE_BLOB_ASYNC_CLIENT_BEAN_NAME = "extendAsyncBlobClient";

    @Bean(EXTEND_STORAGE_BLOB_PROPERTIES_BEAN_NAME)
    @ConfigurationProperties(EXTEND_PREFIX)
    public ExtendAzureStorageBlobProperties extendAzureStorageBlobProperties(AzureGlobalProperties azureGlobalProperties) {
        return AzureGlobalPropertiesUtils.loadProperties(azureGlobalProperties, new ExtendAzureStorageBlobProperties());
    }

    @Bean(EXTEND_STORAGE_BLOB_ASYNC_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = EXTEND_STORAGE_BLOB_ASYNC_CLIENT_BEAN_NAME)
    @ConditionalOnProperty(prefix = EXTEND_PREFIX, name = "blob-name")
    public BlobAsyncClient extendBlobAsyncClient(@Qualifier(EXTEND_STORAGE_BLOB_PROPERTIES_BEAN_NAME) ExtendAzureStorageBlobProperties properties,
                                          @Qualifier(EXTEND_STORAGE_BLOB_ASYNC_CONTAINER_CLIENT_BEAN_NAME) BlobContainerAsyncClient blobContainerAsyncClient) {
        return blobContainerAsyncClient.getBlobAsyncClient(properties.getBlobName());
    }

    @Bean(EXTEND_STORAGE_BLOB_ASYNC_CONTAINER_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = EXTEND_STORAGE_BLOB_ASYNC_CONTAINER_CLIENT_BEAN_NAME)
    @ConditionalOnProperty(prefix = EXTEND_PREFIX, name = "container-name")
    public BlobContainerAsyncClient extendBlobContainerAsyncClient(@Qualifier(EXTEND_STORAGE_BLOB_PROPERTIES_BEAN_NAME) ExtendAzureStorageBlobProperties properties,
                                                            @Qualifier(EXTEND_STORAGE_BLOB_ASYNC_SERVICE_CLIENT_BEAN_NAME) BlobServiceAsyncClient blobServiceAsyncClient) {
        return blobServiceAsyncClient.getBlobContainerAsyncClient(properties.getContainerName());
    }

    @Bean(EXTEND_STORAGE_BLOB_ASYNC_SERVICE_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = EXTEND_STORAGE_BLOB_ASYNC_SERVICE_CLIENT_BEAN_NAME)
    public BlobServiceAsyncClient extendBlobServiceAsyncClient(
        @Qualifier(EXTEND_STORAGE_BLOB_CLIENT_BUILDER_BEAN_NAME) BlobServiceClientBuilder builder) {
        return builder.buildAsyncClient();
    }

    @Bean(EXTEND_STORAGE_BLOB_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = EXTEND_STORAGE_BLOB_CLIENT_BEAN_NAME)
    @ConditionalOnProperty(prefix = EXTEND_PREFIX, name = "blob-name")
    public BlobClient extendBlobClient(@Qualifier(EXTEND_STORAGE_BLOB_PROPERTIES_BEAN_NAME) ExtendAzureStorageBlobProperties properties,
                                @Qualifier(EXTEND_STORAGE_BLOB_CONTAINER_CLIENT_BEAN_NAME) BlobContainerClient blobContainerClient) {
        return blobContainerClient.getBlobClient(properties.getBlobName());
    }

    @Bean(EXTEND_STORAGE_BLOB_CONTAINER_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = EXTEND_STORAGE_BLOB_CONTAINER_CLIENT_BEAN_NAME)
    @ConditionalOnProperty(prefix = EXTEND_PREFIX, name = "container-name")
    public BlobContainerClient extendBlobContainerClient(@Qualifier(EXTEND_STORAGE_BLOB_PROPERTIES_BEAN_NAME) ExtendAzureStorageBlobProperties properties,
                                                  @Qualifier(EXTEND_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME) BlobServiceClient blobServiceClient) {
        return blobServiceClient.getBlobContainerClient(properties.getContainerName());
    }

    @Bean(EXTEND_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = EXTEND_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME)
    public BlobServiceClient extendBlobServiceClient(
        @Qualifier(EXTEND_STORAGE_BLOB_CLIENT_BUILDER_BEAN_NAME) BlobServiceClientBuilder builder) {
        return builder.buildClient();
    }

    @Bean(EXTEND_STORAGE_BLOB_CLIENT_BUILDER_FACTORY_BEAN_NAME)
    @ConditionalOnMissingBean(name = EXTEND_STORAGE_BLOB_CLIENT_BUILDER_FACTORY_BEAN_NAME)
    public BlobServiceClientBuilderFactory extendBlobServiceClientBuilderFactory(
        @Qualifier(EXTEND_STORAGE_BLOB_PROPERTIES_BEAN_NAME) ExtendAzureStorageBlobProperties properties,
        @Qualifier(EXTEND_STATIC_STORAGE_BLOB_CONNECTION_STRING_PROVIDER_BEAN_NAME) ObjectProvider<ServiceConnectionStringProvider<AzureServiceType.StorageBlob>> connectionStringProviders,
        @Qualifier(EXTEND_AZURE_SERVICE_CLIENT_BUILDER_CUSTOMIZER_BEAN_NAME) ObjectProvider<AzureServiceClientBuilderCustomizer<BlobServiceClientBuilder>> customizers) {
        BlobServiceClientBuilderFactory factory = new BlobServiceClientBuilderFactory(properties);

        factory.setSpringIdentifier(AzureSpringIdentifier.AZURE_SPRING_STORAGE_BLOB);
        connectionStringProviders.orderedStream().findFirst().ifPresent(factory::setConnectionStringProvider);
        customizers.orderedStream().forEach(factory::addBuilderCustomizer);
        return factory;
    }

    @Bean(EXTEND_STORAGE_BLOB_CLIENT_BUILDER_BEAN_NAME)
    @ConditionalOnMissingBean(name = EXTEND_STORAGE_BLOB_CLIENT_BUILDER_BEAN_NAME)
    public BlobServiceClientBuilder extendBlobServiceClientBuilder(
        @Qualifier(EXTEND_STORAGE_BLOB_CLIENT_BUILDER_FACTORY_BEAN_NAME) BlobServiceClientBuilderFactory factory) {
        return factory.build();
    }

    @Bean(EXTEND_STATIC_STORAGE_BLOB_CONNECTION_STRING_PROVIDER_BEAN_NAME)
    @ConditionalOnMissingBean(name = EXTEND_STATIC_STORAGE_BLOB_CONNECTION_STRING_PROVIDER_BEAN_NAME)
    @ConditionalOnAnyProperty(prefix = EXTEND_PREFIX, name = { "connection-string" })
    public StaticConnectionStringProvider<AzureServiceType.StorageBlob> extendStaticStorageBlobConnectionStringProvider(
        @Qualifier(EXTEND_STORAGE_BLOB_PROPERTIES_BEAN_NAME) ExtendAzureStorageBlobProperties properties) {
        return new StaticConnectionStringProvider<>(AzureServiceType.STORAGE_BLOB, properties.getConnectionString());
    }
}
