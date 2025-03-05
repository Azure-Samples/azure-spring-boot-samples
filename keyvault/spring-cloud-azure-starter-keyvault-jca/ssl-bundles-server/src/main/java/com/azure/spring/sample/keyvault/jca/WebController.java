// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.keyvault.jca;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class WebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);

    private final RestTemplate restTemplate;

    @Value("${server.port:8444}")
    private int port;

    public WebController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/ssl-test")
    public String inbound(){
        String message = "Inbound TLS is working!";
        LOGGER.info(message);
        return message;
    }

    @GetMapping(value = "/ssl-test-outbound")
    public String outbound() {
        String sslTest = "https://localhost:" + port + "/ssl-test";
        ResponseEntity<String> response = restTemplate.getForEntity(sslTest, String.class);
        String message = "Outbound TLS " + (response.getStatusCode() == HttpStatus.OK ? "is" : "is not")  + " working!";
        LOGGER.info(message);
        return message;
    }
}
