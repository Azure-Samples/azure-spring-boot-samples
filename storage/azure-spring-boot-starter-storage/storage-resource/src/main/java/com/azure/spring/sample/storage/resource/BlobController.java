// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.storage.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
public class BlobController {

    final static Logger logger = LoggerFactory.getLogger(BlobController.class);

    private String containerName = "gzhcontainer";
    private String blobPath = "/";
    private String blobPrefix = "azure-blob://" + containerName + blobPath;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("azure-blob://gzhcontainer/hahaha.txt")
    private Resource blobresource;


    @Value("azure-file://gzhfile/hahahafile.txt")
    private Resource resourcefile;

    @GetMapping("/blob")
    public String index() {
        return "blobupload";
    }


    @GetMapping("/resource")
    @ResponseBody
    public String resource() throws IOException{
        try (OutputStream os = ((WritableResource) this.blobresource).getOutputStream()) {
            os.write("resource test".getBytes());
        }
        return "success";
    }


    @GetMapping("/resourcefile")
    @ResponseBody
    public String resourcefile() throws IOException{
        try (OutputStream os = ((WritableResource) this.resourcefile).getOutputStream()) {
            os.write("resourcefile test".getBytes());
        }
        return "success";
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
        Resource resource = resourceLoader.getResource(blobPrefix + name);
        try (OutputStream os = ((WritableResource) resource).getOutputStream()) {
            os.write(file.getBytes());
        }
        redirectAttributes.addFlashAttribute("message",
            "upload success ： '" + file.getOriginalFilename() + "'");
        logger.info("upload success ：" + file.getOriginalFilename());
        return "redirect:/uploadStauts";
    }

}
