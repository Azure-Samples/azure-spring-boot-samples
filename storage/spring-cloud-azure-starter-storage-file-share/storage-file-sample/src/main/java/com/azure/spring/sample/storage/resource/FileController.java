// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource;

import com.azure.spring.cloud.core.resource.AzureStorageFileProtocolResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

import static com.azure.spring.sample.storage.resource.SampleDataInitializer.FILE_RESOURCE_PATTERN;

@RestController
@RequestMapping("file")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final String shareName;
    private final ResourceLoader resourceLoader;
    private final AzureStorageFileProtocolResolver azureStorageFileProtocolResolver;

    public FileController(@Value("${spring.cloud.azure.storage.fileshare.share-name}") String shareName,
                          ResourceLoader resourceLoader,
                          AzureStorageFileProtocolResolver patternResolver) {
        this.shareName = shareName;
        this.resourceLoader = resourceLoader;
        this.azureStorageFileProtocolResolver = patternResolver;
    }

    /**
     * Using AzureStorageFileProtocolResolver to get Azure Storage File Share resources with file pattern.
     *
     * @return fileNames in the container match pattern: *.txt
     */
    @GetMapping
    public List<String> listTxtFiles() throws IOException {
        Resource[] resources = azureStorageFileProtocolResolver.getResources(String.format(FILE_RESOURCE_PATTERN, this.shareName, "*.txt"));
        logger.info("{} resources founded with pattern:*.txt",resources.length);
        return Stream.of(resources).map(Resource::getFilename).collect(Collectors.toList());
    }

    @GetMapping("/{fileName}")
    public String readResource(@PathVariable("fileName") String fileName) throws IOException {
        Resource resource = resourceLoader.getResource(String.format(FILE_RESOURCE_PATTERN, this.shareName, fileName));
        return StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
    }

    @PostMapping("/{fileName}")
    public String writeResource(@PathVariable("fileName") String fileName, @RequestBody String data) throws IOException {
        Resource resource = resourceLoader.getResource(String.format(FILE_RESOURCE_PATTERN, this.shareName, fileName));
        try (OutputStream os = ((WritableResource) resource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "file was updated";
    }
}
