package com.azure.spring.sample.storage.resource.extend;

import com.azure.spring.cloud.autoconfigure.condition.ConditionalOnAnyProperty;
import com.azure.spring.cloud.autoconfigure.context.AzureGlobalProperties;
import com.azure.spring.cloud.autoconfigure.implementation.properties.utils.AzureGlobalPropertiesUtils;
import com.azure.spring.cloud.autoconfigure.implementation.storage.blob.properties.AzureStorageBlobProperties;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.azure.spring.cloud.autoconfigure.context.AzureContextUtils.STORAGE_BLOB_CLIENT_BUILDER_BEAN_NAME;
import static com.azure.spring.cloud.autoconfigure.context.AzureContextUtils.STORAGE_BLOB_CLIENT_BUILDER_FACTORY_BEAN_NAME;
import static com.azure.spring.sample.storage.resource.extend.DefaultAzureStorageBlobResourceAutoConfiguration.CURRENT_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@ConditionalOnClass(BlobServiceClientBuilder.class)
@ConditionalOnProperty(value = "spring.cloud.azure.storage.blob.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnAnyProperty(prefix = "spring.cloud.azure.storage.blob", name = { "account-name", "endpoint", "connection-string" })
public class DefaultAzureStorageBlobAutoConfiguration {

    public static final String DEFAULT_STORAGE_BLOB_PROPERTIES_BEAN_NAME = "azureStorageBlobProperties";
    public static final String DEFAULT_STATIC_STORAGE_BLOB_CONNECTION_STRING_PROVIDER_BEAN_NAME = "staticStorageBlobConnectionStringProvider";
    public static final String DEFAULT_AZURE_SERVICE_CLIENT_BUILDER_CUSTOMIZER_BEAN_NAME = "azureServiceClientBuilderCustomizer";

    static final String DEFAULT_STORAGE_BLOB_CONTAINER_CLIENT_BEAN_NAME = "blobContainerClient";
    static final String DEFAULT_STORAGE_BLOB_ASYNC_CONTAINER_CLIENT_BEAN_NAME = "blobContainerAsyncClient";
    static final String DEFAULT_STORAGE_BLOB_ASYNC_SERVICE_CLIENT_BEAN_NAME = "blobServiceAsyncClient";
    static final String DEFAULT_STORAGE_BLOB_CLIENT_BEAN_NAME = "blobClient";
    static final String DEFAULT_STORAGE_BLOB_ASYNC_CLIENT_BEAN_NAME = "blobAsyncClient";

    @Bean(DEFAULT_STORAGE_BLOB_PROPERTIES_BEAN_NAME)
    @ConditionalOnMissingBean(name = DEFAULT_STORAGE_BLOB_PROPERTIES_BEAN_NAME)
    @ConfigurationProperties(AzureStorageBlobProperties.PREFIX)
    public AzureStorageBlobProperties azureStorageBlobProperties(AzureGlobalProperties azureGlobalProperties) {
        return AzureGlobalPropertiesUtils.loadProperties(azureGlobalProperties, new AzureStorageBlobProperties());
    }

    @Bean(DEFAULT_STORAGE_BLOB_ASYNC_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = DEFAULT_STORAGE_BLOB_ASYNC_CLIENT_BEAN_NAME)
    @ConditionalOnProperty(prefix = AzureStorageBlobProperties.PREFIX, name = "blob-name")
    BlobAsyncClient blobAsyncClient(@Qualifier(DEFAULT_STORAGE_BLOB_PROPERTIES_BEAN_NAME) AzureStorageBlobProperties properties,
                                    @Qualifier(DEFAULT_STORAGE_BLOB_ASYNC_CONTAINER_CLIENT_BEAN_NAME) BlobContainerAsyncClient blobContainerAsyncClient) {
        return blobContainerAsyncClient.getBlobAsyncClient(properties.getBlobName());
    }

    @Bean(DEFAULT_STORAGE_BLOB_ASYNC_CONTAINER_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = DEFAULT_STORAGE_BLOB_ASYNC_CONTAINER_CLIENT_BEAN_NAME)
    @ConditionalOnProperty(prefix = AzureStorageBlobProperties.PREFIX, name = "container-name")
    BlobContainerAsyncClient blobContainerAsyncClient(@Qualifier(DEFAULT_STORAGE_BLOB_PROPERTIES_BEAN_NAME) AzureStorageBlobProperties properties,
                                                      @Qualifier(DEFAULT_STORAGE_BLOB_ASYNC_SERVICE_CLIENT_BEAN_NAME) BlobServiceAsyncClient blobServiceAsyncClient) {
        return blobServiceAsyncClient.getBlobContainerAsyncClient(properties.getContainerName());
    }

    /**
     * Autoconfigure the {@link BlobServiceAsyncClient} instance.
     * @param builder the {@link BlobServiceClientBuilder} to build the instance.
     * @return the blob service async client.
     */
    @Bean(DEFAULT_STORAGE_BLOB_ASYNC_SERVICE_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = DEFAULT_STORAGE_BLOB_ASYNC_SERVICE_CLIENT_BEAN_NAME)
    public BlobServiceAsyncClient blobServiceAsyncClient(
        @Qualifier(STORAGE_BLOB_CLIENT_BUILDER_BEAN_NAME) BlobServiceClientBuilder builder) {
        return builder.buildAsyncClient();
    }

    @Bean(DEFAULT_STORAGE_BLOB_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = DEFAULT_STORAGE_BLOB_CLIENT_BEAN_NAME)
    @ConditionalOnProperty(prefix = AzureStorageBlobProperties.PREFIX, name = "blob-name")
    BlobClient blobClient(@Qualifier(DEFAULT_STORAGE_BLOB_PROPERTIES_BEAN_NAME) AzureStorageBlobProperties properties,
                          @Qualifier(DEFAULT_STORAGE_BLOB_CONTAINER_CLIENT_BEAN_NAME) BlobContainerClient blobContainerClient) {
        return blobContainerClient.getBlobClient(properties.getBlobName());
    }

    @Bean(DEFAULT_STORAGE_BLOB_CONTAINER_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = DEFAULT_STORAGE_BLOB_CONTAINER_CLIENT_BEAN_NAME)
    @ConditionalOnProperty(prefix = AzureStorageBlobProperties.PREFIX, name = "container-name")
    BlobContainerClient blobContainerClient(@Qualifier(DEFAULT_STORAGE_BLOB_PROPERTIES_BEAN_NAME) AzureStorageBlobProperties properties,
                                            @Qualifier(CURRENT_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME) BlobServiceClient blobServiceClient) {
        return blobServiceClient.getBlobContainerClient(properties.getContainerName());
    }

    /**
     * Autoconfigure the {@link BlobServiceClient} instance.
     * @param builder the {@link BlobServiceClientBuilder} to build the instance.
     * @return the blob service client.
     */
    @Bean(CURRENT_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean(name = CURRENT_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME)
    public BlobServiceClient blobServiceClient(
        @Qualifier(STORAGE_BLOB_CLIENT_BUILDER_BEAN_NAME) BlobServiceClientBuilder builder) {
        return builder.buildClient();
    }

    @Bean(STORAGE_BLOB_CLIENT_BUILDER_FACTORY_BEAN_NAME)
    @ConditionalOnMissingBean(name = STORAGE_BLOB_CLIENT_BUILDER_FACTORY_BEAN_NAME)
    BlobServiceClientBuilderFactory blobServiceClientBuilderFactory(
        @Qualifier(DEFAULT_STORAGE_BLOB_PROPERTIES_BEAN_NAME) AzureStorageBlobProperties properties,
        @Qualifier(DEFAULT_STATIC_STORAGE_BLOB_CONNECTION_STRING_PROVIDER_BEAN_NAME) ObjectProvider<ServiceConnectionStringProvider<AzureServiceType.StorageBlob>> connectionStringProviders,
        @Qualifier(DEFAULT_AZURE_SERVICE_CLIENT_BUILDER_CUSTOMIZER_BEAN_NAME) ObjectProvider<AzureServiceClientBuilderCustomizer<BlobServiceClientBuilder>> customizers) {
        BlobServiceClientBuilderFactory factory = new BlobServiceClientBuilderFactory(properties);

        factory.setSpringIdentifier(AzureSpringIdentifier.AZURE_SPRING_STORAGE_BLOB);
        connectionStringProviders.orderedStream().findFirst().ifPresent(factory::setConnectionStringProvider);
        customizers.orderedStream().forEach(factory::addBuilderCustomizer);
        return factory;
    }

    @Bean(STORAGE_BLOB_CLIENT_BUILDER_BEAN_NAME)
    @ConditionalOnMissingBean(name = STORAGE_BLOB_CLIENT_BUILDER_BEAN_NAME)
    BlobServiceClientBuilder blobServiceClientBuilder(@Qualifier(STORAGE_BLOB_CLIENT_BUILDER_FACTORY_BEAN_NAME)
                                                          BlobServiceClientBuilderFactory factory) {
        return factory.build();
    }

    @Bean(DEFAULT_STATIC_STORAGE_BLOB_CONNECTION_STRING_PROVIDER_BEAN_NAME)
    @ConditionalOnProperty("spring.cloud.azure.storage.blob.connection-string")
    StaticConnectionStringProvider<AzureServiceType.StorageBlob> staticStorageBlobConnectionStringProvider(
        @Qualifier(DEFAULT_STORAGE_BLOB_PROPERTIES_BEAN_NAME) AzureStorageBlobProperties properties) {
        return new StaticConnectionStringProvider<>(AzureServiceType.STORAGE_BLOB, properties.getConnectionString());
    }

}
