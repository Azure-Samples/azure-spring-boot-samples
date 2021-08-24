// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.spring.sample.b2c.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    @ResponseBody
    @GetMapping(value = { "/log" })
    public String log() {
        return "Test log read success.";
    }
}
