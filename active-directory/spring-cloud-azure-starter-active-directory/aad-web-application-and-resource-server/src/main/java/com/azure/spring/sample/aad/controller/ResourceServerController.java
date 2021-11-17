// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Resource server exposes the APIs.
 */
@RestController
@RequestMapping("/resource-server")
public class ResourceServerController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, this is resource-server.";
    }
}
