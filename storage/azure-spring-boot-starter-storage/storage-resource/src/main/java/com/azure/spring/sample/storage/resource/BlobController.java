// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource;

import com.azure.spring.autoconfigure.storage.resource.AzureStorageProtocolResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Warren Zhu
 */
@Controller
@ConditionalOnProperty("azure.storage.blob-endpoint")
public class BlobController {

    final static Logger logger = LoggerFactory.getLogger(BlobController.class);

    public BlobController() {
        logger.info("azure.storage.blob-endpoint configured");
    }

    private String containerName = "gzhcontainer";
    private String blobPath = "/";
    private String blobPrefix = "azure-blob://" + containerName + blobPath;

    @Autowired
    private AzureStorageProtocolResolver resolver;

    @GetMapping("/blob")
    public String index() {
        return "blobupload";
    }

    @PostMapping("blob/upload")
    public String uploadToBlob(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        logger.info("get input request：/upload");

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "upload file is empty");
            logger.info("uploaded file is empty");
            return "redirect:/uploadStauts";
        }

        String name = file.getOriginalFilename();
        Resource resource = resolver.resolve(blobPrefix + name,null);
        try (OutputStream os = ((WritableResource) resource).getOutputStream()) {
            os.write(file.getBytes());
        }
        redirectAttributes.addFlashAttribute("message",
            "upload success ： '" + file.getOriginalFilename() + "'");
        logger.info("upload success ：" + file.getOriginalFilename());
        return "redirect:/uploadStauts";
    }

    @GetMapping("/uploadStauts")
    public String uploadStatus() {
        return "uploadStauts";
    }
}
