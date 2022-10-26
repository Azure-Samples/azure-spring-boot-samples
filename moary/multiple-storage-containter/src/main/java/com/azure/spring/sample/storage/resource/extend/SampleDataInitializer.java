package com.azure.spring.sample.storage.resource.extend;

import com.azure.spring.cloud.core.resource.AbstractAzureStorageProtocolResolver;
import com.azure.spring.sample.storage.resource.extend.storage.blob.ExtendAzureStorageBlobProtocolResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;

import static com.azure.spring.sample.storage.resource.extend.autoconfigure.storage.blob.DefaultAzureStorageBlobResourceAutoConfiguration.CURRENT_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME;
import static com.azure.spring.sample.storage.resource.extend.autoconfigure.storage.blob.ExtendAzureStorageBlobResourceAutoConfiguration.EXTEND_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME_PREFIX;

@Profile("extend")
@Component
public class SampleDataInitializer implements CommandLineRunner {
    final static Logger logger = LoggerFactory.getLogger(SampleDataInitializer.class);

    private final ConfigurableEnvironment env;

    private ExtendAzureStorageBlobProtocolResolver defaultAzureStorageBlobProtocolResolver;
    private ExtendAzureStorageBlobProtocolResolver extendAzureStorageBlobProtocolResolver;

    public SampleDataInitializer(ConfigurableEnvironment env,
                                 @Qualifier(CURRENT_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME) ExtendAzureStorageBlobProtocolResolver defaultPatternResolver,
                                 @Qualifier(EXTEND_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME_PREFIX) ExtendAzureStorageBlobProtocolResolver extendPatternResolver) {
        this.env = env;
        this.defaultAzureStorageBlobProtocolResolver = defaultPatternResolver;
        this.extendAzureStorageBlobProtocolResolver = extendPatternResolver;
    }

    /**
     * This is used to initialize some data in Azure Storage Blob.
     * So users can use `curl -XGET http://localhost:8080/blob/getFileNamesWithProtocolResolver` to test
     * AzureStorageBlobProtocolResolver without initializing data.
     */
    @Override
    public void run(String... args) throws Exception {
        String defaultContainerName = env.getProperty("spring.cloud.azure.storage.blob.container-name");
        String secondContainerName = env.getProperty("extend.spring.cloud.azure.storage.blob.container-name");
        logger.info("Default container name: {}", defaultContainerName);
        logger.info("Extend container name: {}", secondContainerName);
        initBlobsData(defaultContainerName, secondContainerName);
    }

    private void initBlobsData(String defaultContainerName, String secondContainerName) throws IOException {
        writeDataByResolver(defaultContainerName, this.defaultAzureStorageBlobProtocolResolver);
        writeDataByResolver(secondContainerName, this.extendAzureStorageBlobProtocolResolver);
    }

    private void writeDataByResolver(String containerName, AbstractAzureStorageProtocolResolver resolver) throws IOException {
        if (!StringUtils.hasText(containerName) || resolver == null) {
            return;
        }

        logger.info("StorageApplication data initialization of '{}' begin ...", containerName);
        long currentTimeMillis = System.currentTimeMillis();
        String fileName = "fileName-" + currentTimeMillis;
        String data = "data" + currentTimeMillis;
        Resource storageBlobResource = resolver.getResource("azure-blob://" + containerName +"/" + fileName + ".txt");
        try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
            os.write(data.getBytes());
            logger.info("Write data to container={}, fileName={}.txt", containerName, fileName);
        }
        logger.info("StorageApplication data initialization of '{}' end ...", containerName);
    }
}
