package com.azure.spring.sample.storage.resource.extend;

import com.azure.spring.cloud.autoconfigure.condition.ConditionalOnAnyProperty;
import com.azure.spring.cloud.autoconfigure.condition.ConditionalOnMissingProperty;
import com.azure.spring.cloud.core.resource.AzureStorageBlobProtocolResolver;
import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(DefaultAzureStorageBlobAutoConfiguration.class)
@ConditionalOnMissingProperty(prefix = "extend", name = "second-container")
@ConditionalOnClass({ AzureStorageBlobProtocolResolver.class })
@ConditionalOnProperty(value = "spring.cloud.azure.storage.blob.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnAnyProperty(prefix = "spring.cloud.azure.storage.blob", name = { "account-name", "endpoint", "connection-string" })
public class DefaultAzureStorageBlobResourceAutoConfiguration {

    public static final String CURRENT_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME = "azureStorageBlobProtocolResolver";
    public static final String CURRENT_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME = "blobServiceClient";

    @Bean(CURRENT_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME)
    @ConditionalOnMissingBean(name = CURRENT_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME)
    public ExtendAzureStorageBlobProtocolResolver azureStorageBlobProtocolResolver(
        @Qualifier(CURRENT_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME) BlobServiceClient blobServiceClient) {
        ExtendAzureStorageBlobProtocolResolver azureStorageBlobProtocolResolver = new ExtendAzureStorageBlobProtocolResolver();
        azureStorageBlobProtocolResolver.setBlobServiceClient(blobServiceClient);
        return azureStorageBlobProtocolResolver;
    }
}
