package com.azure.spring.sample.storage.resource.current;

import com.azure.spring.cloud.autoconfigure.condition.ConditionalOnAnyProperty;
import com.azure.spring.cloud.autoconfigure.condition.ConditionalOnMissingProperty;
import com.azure.spring.cloud.autoconfigure.storage.blob.AzureStorageBlobAutoConfiguration;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(AzureStorageBlobAutoConfiguration.class)
@ConditionalOnMissingProperty(prefix = "extend.spring.cloud.azure.storage.blob", name = { "account-name", "endpoint", "connection-string" })
public class StorageContainerConfiguration {

    static final String STORAGE_BLOB_CONTAINER_CLIENT_BEAN_NAME = "secondContainerClient";

    @Bean(STORAGE_BLOB_CONTAINER_CLIENT_BEAN_NAME)
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "extend", name = "second-container")
    BlobContainerClient secondContainerClient(@Value("${extend.second-container}") String secondContainerName,
                                              BlobServiceClient blobServiceClient) {
        return blobServiceClient.getBlobContainerClient(secondContainerName);
    }
}
