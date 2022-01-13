package com.azure.spring.sample.activedirectory.oauth2.gateway.resourceserver1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/resource-server-1")
    public String home() {
        return "Hello, this is resource-server-1.";
    }
}
