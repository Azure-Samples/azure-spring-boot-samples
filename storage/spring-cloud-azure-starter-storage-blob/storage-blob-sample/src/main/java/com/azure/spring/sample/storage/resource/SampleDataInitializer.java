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
    final static Logger logger = LoggerFactory.getLogger(SampleDataInitializer.class);

    private final ResourceLoader resourceLoader;

    private final String containerName;
    public SampleDataInitializer(@Value("${spring.cloud.azure.storage.blob.container-name}") String containerName,
                          ResourceLoader resourceLoader) {
        this.containerName = containerName;
        this.resourceLoader = resourceLoader;
    }

    /**
     * This is used to initialize some data in Azure Storage Blob.
     * So users can use `curl -XGET http://localhost:8080/blob/getFileNamesWithProtocolResolver` to test
     * AzureStorageBlobProtocolResolver without initializing data.
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
                logger.info("write data to container={}, fileName={}.txt", containerName, fileName);
            }
        }
        logger.info("StorageApplication data initialization end ...");
    }
}
