// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource;

import com.azure.spring.core.resource.AzureStorageBlobProtocolResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Warren Zhu
 */
@RestController
@RequestMapping("blob")
public class BlobController implements ResourceLoaderAware {

    final static Logger logger = LoggerFactory.getLogger(BlobController.class);

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    private ResourceLoader resourceLoader;

    @Autowired
    private AzureStorageBlobProtocolResolver azureStorageBlobProtocolResolver;

    /**
     * Using resourceLoader to get Azure Storage Blob resource with filename.
     *
     * @param fileName the fileName(contains extension name) stored in Storage Blob Container.
     *                 eg: fileName = fileName1.txt
     * @return the content stored in the file.
     */
    @GetMapping("/getResourceWithResourceLoader/{fileName}")
    public String getResourceWithResourceLoader(@PathVariable String fileName) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + containerName + "/" + fileName);
        String fileContent = StreamUtils.copyToString(
                storageBlobResource.getInputStream(),
                Charset.defaultCharset());
        logger.info("Blob content retrieved: fileName={}, fileContent={}", fileName, fileContent);
        return "fileContent=" + fileContent;
    }

    /**
     * Using AzureStorageBlobProtocolResolver to get Azure Storage Blob resources with file pattern.
     *
     * @return fileNames in the container match pattern: *.txt
     */
    @GetMapping("/getFileNamesWithProtocolResolver")
    public String getFileNamesWithProtocolResolver() throws IOException {
        Resource[] resources = azureStorageBlobProtocolResolver.getResources("azure-blob://" + containerName + "/*.txt");
        logger.info("{} resources founded with pattern:*.txt",resources.length);
        return Stream.of(resources).map(Resource::getFilename).collect(Collectors.joining("\n"));
    }

    @GetMapping("/{fileName}")
    public String readBlobResource(@PathVariable("fileName") String fileName) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + containerName + "/" + fileName);
        return StreamUtils.copyToString(
            storageBlobResource.getInputStream(),
            Charset.defaultCharset());
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostMapping("/{fileName}")
    public String writeBlobResource(@PathVariable("fileName") String fileName, @RequestBody String data) throws IOException {
        // get a BlobResource
        Resource storageBlobResource = resourceLoader.getResource("azure-blob://" + containerName + "/" + fileName);
        try (OutputStream os = ((WritableResource) storageBlobResource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "blob was updated";
    }
}
