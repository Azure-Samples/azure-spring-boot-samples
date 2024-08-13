// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.azure.spring.cloud.feature.management.FeatureManager;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HelloController {

    private FeatureManager featureManager;

    public HelloController(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    @GetMapping("/welcome")
    public String mainWithParam(Model model) {
        model.addAttribute("Beta", featureManager.isEnabledAsync("beta").block());
        return "welcome";
    }
}