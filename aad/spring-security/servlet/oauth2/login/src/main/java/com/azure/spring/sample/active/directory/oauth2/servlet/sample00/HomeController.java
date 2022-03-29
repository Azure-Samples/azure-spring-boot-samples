package com.azure.spring.sample.active.directory.oauth2.servlet.sample00;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, this is client-1.";
    }
}
