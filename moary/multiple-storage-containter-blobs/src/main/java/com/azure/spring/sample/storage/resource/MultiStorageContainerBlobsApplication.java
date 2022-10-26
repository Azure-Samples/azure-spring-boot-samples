// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource;

import com.azure.spring.cloud.autoconfigure.storage.blob.AzureStorageBlobAutoConfiguration;
import com.azure.spring.cloud.autoconfigure.storage.blob.AzureStorageBlobResourceAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    AzureStorageBlobAutoConfiguration.class,
    AzureStorageBlobResourceAutoConfiguration.class
})
public class MultiStorageContainerBlobsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MultiStorageContainerBlobsApplication.class, args);
    }
}
