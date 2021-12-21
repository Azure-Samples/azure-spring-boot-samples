package com.azure.spring.sample.active.directory.oauth2.servlet.sample03.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, this is client-1.";
    }
}