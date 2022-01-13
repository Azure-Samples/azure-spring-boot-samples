package com.azure.spring.sample.activedirectory.reactive.webflux.oauth2.gateway.resourceserver2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/resource-server-2")
    public String home() {
        return "Hello, this is resource-server-2.";
    }
}
