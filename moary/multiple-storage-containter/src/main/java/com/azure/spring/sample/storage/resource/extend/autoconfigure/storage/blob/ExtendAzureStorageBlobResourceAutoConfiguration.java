// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource.extend.autoconfigure.storage.blob;

import com.azure.spring.cloud.autoconfigure.condition.ConditionalOnAnyProperty;
import com.azure.spring.cloud.autoconfigure.condition.ConditionalOnMissingProperty;
import com.azure.spring.sample.storage.resource.extend.storage.blob.ExtendAzureStorageBlobProtocolResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.azure.spring.sample.storage.resource.extend.autoconfigure.storage.blob.ExtendStorageAccountAutoConfiguration.EXTEND_STORAGE_BLOB_SERVICE_CLIENT_BEAN_NAME;

@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingProperty(prefix = "extend", name = "second-container")
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
