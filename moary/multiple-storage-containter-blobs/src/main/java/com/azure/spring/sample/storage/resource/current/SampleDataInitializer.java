package com.azure.spring.sample.storage.resource.current;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;

@Profile("current")
@Component
public class SampleDataInitializer implements CommandLineRunner {
    final static Logger logger = LoggerFactory.getLogger(SampleDataInitializer.class);

    private final ResourceLoader resourceLoader;

    private final ConfigurableEnvironment env;

    public SampleDataInitializer(ResourceLoader resourceLoader, ConfigurableEnvironment env) {
        this.resourceLoader = resourceLoader;
        this.env = env;
    }

    /**
     * This is used to initialize some data in Azure Storage Blob.
     * So users can use `curl -XGET http://localhost:8080/blob/getFileNamesWithProtocolResolver` to test
     * AzureStorageBlobProtocolResolver without initializing data.
     */
    @Override
    public void run(String... args) throws Exception {
        String defaultContainerName = env.getProperty("spring.cloud.azure.storage.blob.container-name");
        initBlobsData(defaultContainerName);
        String secondContainerName = env.getProperty("extend.second-container");
        if (StringUtils.hasText(secondContainerName)) {
            initBlobsData(defaultContainerName);
        }
    }

    private void initBlobsData(String containerName) throws IOException {
        logger.info("StorageApplication data initialization of '{}' begin ...", containerName);
        long currentTimeMillis = System.currentTimeMillis();
        String fileName = "fileName" + currentTimeMillis;
        String data = "data" + currentTimeMillis;
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + containerName +"/" + fileName + ".txt");
        try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
            os.write(data.getBytes());
            logger.info("Write data to container={}, fileName={}.txt", containerName, fileName);
        }
        logger.info("StorageApplication data initialization of '{}' end ...", containerName);
    }
}
