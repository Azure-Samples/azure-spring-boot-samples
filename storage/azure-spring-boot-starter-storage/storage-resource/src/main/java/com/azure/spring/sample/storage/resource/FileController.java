// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author Warren Zhu
 */
@RestController
@RequestMapping("file")
@ConditionalOnProperty("azure.storage.file-endpoint")
public class FileController {
    final static Logger logger = LoggerFactory.getLogger(FileController.class);

    public FileController() {
        logger.info("azure.storage.file-endpoint configured");
    }

    @Value("${resource.file}")
    private Resource azureFileResource;

    @GetMapping
    public String readFileResource() throws IOException {
        return StreamUtils.copyToString(
            this.azureFileResource.getInputStream(),
            Charset.defaultCharset());
    }

    @PostMapping
    public String writeFileResource(@RequestBody String data) throws IOException {
        try (OutputStream os = ((WritableResource) this.azureFileResource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "file was updated";
    }
}
