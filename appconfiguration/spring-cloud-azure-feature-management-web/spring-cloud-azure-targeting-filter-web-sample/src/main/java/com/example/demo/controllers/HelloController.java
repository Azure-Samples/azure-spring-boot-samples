// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.azure.spring.cloud.feature.management.FeatureManager;


@Controller
public class HelloController {

    private final FeatureManager featureManager;

    public HelloController(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    @GetMapping({"/", "/welcome"})
    public String mainWithParam(Model model) {
        model.addAttribute("Beta", featureManager.isEnabled("Beta"));
        return "welcome";
    }
}