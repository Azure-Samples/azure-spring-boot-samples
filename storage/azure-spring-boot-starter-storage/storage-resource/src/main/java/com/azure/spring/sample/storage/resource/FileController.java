// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.OutputStream;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Warren Zhu
 */
@Controller
@ConditionalOnProperty("azure.storage.file-endpoint")
public class FileController {
    final static Logger logger = LoggerFactory.getLogger(FileController.class);

    public FileController() {
        logger.info("azure.storage.file-endpoint configured");
    }

    private String fileshareName = "input-filesharename";

    private String filePrefix = "azure-file://"+ fileshareName +"/";

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/file")
    public String index() {
        return "fileupload";
    }

    @PostMapping("file/upload")
    public String uploadToFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        logger.info("get input request：/upload");

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "upload file is empty");
            logger.info("uploaded file is empty");
            return "redirect:/uploadStauts";
        }

        String name = file.getOriginalFilename();
        Resource resource = resourceLoader.getResource(filePrefix + name);
        try (OutputStream os = ((WritableResource) resource).getOutputStream()) {
            os.write(file.getBytes());
        }
        redirectAttributes.addFlashAttribute("message",
            "upload success ： '" + file.getOriginalFilename() + "'");
        logger.info("upload success ：" + file.getOriginalFilename());
        return "redirect:/uploadStauts";
    }
}
