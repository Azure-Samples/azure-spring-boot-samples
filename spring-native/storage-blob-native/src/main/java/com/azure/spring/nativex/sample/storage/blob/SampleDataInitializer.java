package com.azure.spring.nativex.sample.storage.blob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.OutputStream;
import java.nio.charset.Charset;

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
     */
    @Override
    public void run(String... args) throws Exception {
        logger.info("StorageApplication data initialization begin ...");
        long millis = System.currentTimeMillis();
        String fileName = "fileName-" + millis + ".txt";
        String filePath = "azure-blob://" + containerName + "/" + fileName;
        Resource storageBlobResource = resourceLoader.getResource(filePath);
        try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
            String data = "data-" + millis;
            os.write(data.getBytes());
            logger.info("Write data to container={}, filePath={}", containerName, filePath);
        }
        String downloadedData = StreamUtils.copyToString(resourceLoader.getResource(filePath).getInputStream(),
            Charset.defaultCharset());
        logger.info("Downloaded data from the azure storage blob resource: {}", downloadedData);
        logger.info("Get the data content through this address 'curl -XGET http://localhost:8080/blob/{}'.",
            fileName);
        logger.info("StorageApplication data initialization end ...");
    }
}
