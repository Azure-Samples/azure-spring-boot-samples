// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource.extend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.azure.spring.sample.storage.resource.extend.DefaultAzureStorageBlobResourceAutoConfiguration.CURRENT_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME;
import static com.azure.spring.sample.storage.resource.extend.ExtendAzureStorageBlobResourceAutoConfiguration.EXTEND_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME_PREFIX;

@Profile("extend")
@RestController
@RequestMapping("/blob")
public class StorageAccountBlobController {

    final static Logger logger = LoggerFactory.getLogger(StorageAccountBlobController.class);
    private final String firstContainerName;
    private final String secondContainerName;
    private final ExtendAzureStorageBlobProtocolResolver defaultAzureStorageBlobProtocolResolver;
    private final ExtendAzureStorageBlobProtocolResolver extendAzureStorageBlobProtocolResolver;

    public StorageAccountBlobController(@Value("${spring.cloud.azure.storage.blob.container-name}") String firstContainerName,
                                        @Value("${extend.spring.cloud.azure.storage.blob.container-name}") String secondContainerName,
                                        @Qualifier(CURRENT_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME) ExtendAzureStorageBlobProtocolResolver defaultPatternResolver,
                                        @Qualifier(EXTEND_STORAGE_BLOB_PROTOCOL_RESOLVER_BEAN_NAME_PREFIX) ExtendAzureStorageBlobProtocolResolver extendPatternResolver) {
        this.firstContainerName = firstContainerName;
        this.secondContainerName = secondContainerName;
        this.defaultAzureStorageBlobProtocolResolver = defaultPatternResolver;
        this.extendAzureStorageBlobProtocolResolver = extendPatternResolver;
    }

    /**
     * Using AzureStorageBlobProtocolResolver to get Azure Storage Blob resources with file pattern.
     *
     * @return fileNames in the container match pattern: *.txt
     */
    @GetMapping("/first")
    public List<String> listTxtFiles() throws IOException {
        Resource[] resources = defaultAzureStorageBlobProtocolResolver.getResources("azure-blob://" + firstContainerName + "/*.txt");
        logger.info("{} resources founded with pattern:*.txt", resources.length);
        return Stream.of(resources).map(Resource::getFilename).collect(Collectors.toList());
    }

    @GetMapping("/first/{fileName}")
    public String readBlobResource(@PathVariable("fileName") String fileName) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = defaultAzureStorageBlobProtocolResolver.getResource("azure-blob://" + firstContainerName + "/" + fileName);
        return StreamUtils.copyToString(
            storageBlobResource.getInputStream(),
            Charset.defaultCharset());
    }

    @PostMapping("/first/{container}/{fileName}")
    public String writeBlobResource(@PathVariable("container") String container,
                                    @PathVariable("fileName") String fileName, @RequestBody String data) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = defaultAzureStorageBlobProtocolResolver.getResource("azure-blob://" + firstContainerName + "/" + fileName);
        try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "blob was updated";
    }

    @GetMapping("/second")
    public List<String> secondListTxtFiles() throws IOException {
        Resource[] resources = extendAzureStorageBlobProtocolResolver.getResources("azure-blob://" + secondContainerName + "/*.txt");
        logger.info("{} resources founded with pattern:*.txt", resources.length);
        return Stream.of(resources).map(Resource::getFilename).collect(Collectors.toList());
    }

    @GetMapping("/second/{fileName}")
    public String secondReadBlobResource(@PathVariable("fileName") String fileName) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = extendAzureStorageBlobProtocolResolver.getResource("azure-blob://" + secondContainerName + "/" + fileName);
        return StreamUtils.copyToString(
            storageBlobResource.getInputStream(),
            Charset.defaultCharset());
    }

    @PostMapping("/second/{container}/{fileName}")
    public String secondWriteBlobResource(@PathVariable("container") String container,
                                    @PathVariable("fileName") String fileName, @RequestBody String data) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = extendAzureStorageBlobProtocolResolver.getResource("azure-blob://" + secondContainerName + "/" + fileName);
        try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "blob was updated";
    }
}
