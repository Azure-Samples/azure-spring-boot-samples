// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;

import java.io.OutputStream;

/**
 * @author Warren Zhu
 */
@SpringBootApplication
public class StorageApplication implements CommandLineRunner {
    final static Logger logger = LoggerFactory.getLogger(StorageApplication.class);

    @Autowired
    @Qualifier("webApplicationContext")
    private ResourceLoader resourceLoader;

    @Value("${storage-container-name}")
    private String containerName;

    public static void main(String[] args) {
        SpringApplication.run(StorageApplication.class, args);
    }

    /**
     * This is used to initialize some data in Azure Storage Blob.
     * So users can use `curl -XGET http://localhost:8080/blob/getFileNamesWithProtocolResolver` to test
     * AzureStorageBlobProtocolResolver without initializing data.
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        logger.info("StorageApplication data initialization begin ...");
        for (int i = 0; i < 10; i++) {
            String fileName = "fileName" + i;
            String data = "data" + i;
            Resource storageBlobResource = resourceLoader.getResource("azure-blob://" +containerName+"/" + fileName + ".txt");
            try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
                os.write(data.getBytes());
                logger.info("write data to container={}, fileName={}", containerName, fileName);
            }
        }
        logger.info("StorageApplication data initialization end ...");
    }
}
