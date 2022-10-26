// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource.current;

import com.azure.spring.cloud.core.resource.AzureStorageBlobProtocolResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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

@Profile("current")
@RestController
@RequestMapping("/blob")
public class StorageAccountBlobController {

    final static Logger logger = LoggerFactory.getLogger(StorageAccountBlobController.class);
    private final String firstContainerName;
    private final String secondContainerName;
    private final ResourceLoader resourceLoader;
    private final AzureStorageBlobProtocolResolver azureStorageBlobProtocolResolver;

    public StorageAccountBlobController(@Value("${spring.cloud.azure.storage.blob.container-name}") String firstContainerName,
                                        @Value("${extend.second-container}") String secondContainerName,
                                        ResourceLoader resourceLoader,
                                        AzureStorageBlobProtocolResolver patternResolver) {
        this.firstContainerName = firstContainerName;
        this.secondContainerName = secondContainerName;
        this.resourceLoader = resourceLoader;
        this.azureStorageBlobProtocolResolver = patternResolver;
    }

    /**
     * Using AzureStorageBlobProtocolResolver to get Azure Storage Blob resources with file pattern.
     *
     * @return fileNames in the container match pattern: *.txt
     */
    @GetMapping("/first")
    public List<String> listTxtFiles() throws IOException {
        Resource[] resources = azureStorageBlobProtocolResolver.getResources("azure-blob://" + firstContainerName + "/*.txt");
        logger.info("{} resources founded with pattern:*.txt",resources.length);
        return Stream.of(resources).map(Resource::getFilename).collect(Collectors.toList());
    }

    @GetMapping("/first/{fileName}")
    public String readBlobResource(@PathVariable("fileName") String fileName) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + firstContainerName + "/" + fileName);
        return StreamUtils.copyToString(
            storageBlobResource.getInputStream(),
            Charset.defaultCharset());
    }

    @PostMapping("/first/{fileName}")
    public String writeBlobResource(@PathVariable("fileName") String fileName, @RequestBody String data) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + firstContainerName + "/" + fileName);
        try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "blob was updated";
    }

    @GetMapping("/second")
    public List<String> secondContainerListTxtFiles() throws IOException {
        Resource[] resources = azureStorageBlobProtocolResolver.getResources("azure-blob://" + secondContainerName + "/*.txt");
        logger.info("{} resources founded with pattern:*.txt",resources.length);
        return Stream.of(resources).map(Resource::getFilename).collect(Collectors.toList());
    }

    @GetMapping("/second/{fileName}")
    public String secondContainerReadBlobResource(@PathVariable("fileName") String fileName) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + secondContainerName + "/" + fileName);
        return StreamUtils.copyToString(
            storageBlobResource.getInputStream(),
            Charset.defaultCharset());
    }

    @PostMapping("/second/{fileName}")
    public String secondContainerWriteBlobResource(@PathVariable("fileName") String fileName, @RequestBody String data) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + secondContainerName + "/" + fileName);
        try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "blob was updated";
    }
}
