// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author Warren Zhu
 */
@RestController
@RequestMapping("blob")
public class BlobController {

    final static Logger logger = LoggerFactory.getLogger(BlobController.class);

    public BlobController() {
        logger.info("spring.cloud.azure.storage.blob.endpoint configured");
    }

    @Value("${resource.blob}")
    private Resource azureBlobResource;

    @GetMapping
    public String readBlobResource() throws IOException {
        return StreamUtils.copyToString(
            this.azureBlobResource.getInputStream(),
            Charset.defaultCharset());
    }

    @PostMapping
    public String writeBlobResource(@RequestBody String data) throws IOException {
        try (OutputStream os = ((WritableResource) this.azureBlobResource).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "blob was updated";
    }
}
