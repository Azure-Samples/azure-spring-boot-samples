package com.azure.spring.sample.storage.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/uploadStauts")
    public String uploadStatus() {
        return "uploadStauts";
    }
}
