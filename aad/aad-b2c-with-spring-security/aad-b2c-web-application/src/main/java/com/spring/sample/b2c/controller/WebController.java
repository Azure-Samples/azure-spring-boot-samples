// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.spring.sample.b2c.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping(value = { "/", "/home" })
    public String index() {
        return "home";
    }

}
