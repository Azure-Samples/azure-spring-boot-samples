// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

@Component
public class SampleDataInitializer implements CommandLineRunner {
    static final String FILE_RESOURCE_PATTERN = "azure-file://%s/%s";
    private static final Logger logger = LoggerFactory.getLogger(SampleDataInitializer.class);
    private final ResourceLoader resourceLoader;
    private final String shareName;

    public SampleDataInitializer(@Value("${spring.cloud.azure.storage.fileshare.share-name}") String shareName, ResourceLoader resourceLoader) {
        this.shareName = shareName;
        this.resourceLoader = resourceLoader;
    }

    /**
     * This is used to initialize some data in Azure Storage File Share.
     * So users can use `curl -XGET http://localhost:8080/file` to test
     * {@link com.azure.spring.cloud.core.resource.AzureStorageFileProtocolResolver} without initializing data.
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("StorageApplication data initialization begin ...");
        for (int i = 0; i < 10; i++) {
            String fileName = "file" + i + ".txt";
            String data = "data" + i;
            Resource storageFileResource = resourceLoader.getResource(String.format(FILE_RESOURCE_PATTERN, shareName, fileName));
            try (OutputStream os = ((WritableResource) storageFileResource).getOutputStream()) {
                os.write(data.getBytes());
                logger.info("write data to share={}, fileName={}", shareName, fileName);
            }
        }
        logger.info("StorageApplication data initialization end ...");
    }
}
