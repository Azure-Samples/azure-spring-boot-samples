// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.azure.spring.cloud.feature.management.FeatureManager;
import com.azure.spring.cloud.feature.management.web.FeatureGate;
import com.azure.spring.cloud.feature.management.web.FeatureManagerSnapshot;


@Controller
@ConfigurationProperties("controller")
public class HelloController {

    @Autowired
    private FeatureManager featureManager;

    @Autowired
    private FeatureManagerSnapshot featureManagerSnapshot;

    @GetMapping("/privacy")
    public String getRequestBased(Model model) {
        model.addAttribute("Beta", featureManager.isEnabled("beta"));
        model.addAttribute("isDarkThemeS1", featureManagerSnapshot.isEnabled("dark-theme"));
        model.addAttribute("isDarkThemeS2", featureManagerSnapshot.isEnabled("dark-theme"));
        model.addAttribute("isDarkThemeS3", featureManagerSnapshot.isEnabled("dark-theme"));
        return "privacy";
    }

    @GetMapping(value = {"/Beta", "/BetaA"})
    @FeatureGate(feature = "beta-ab", fallback = "/BetaB")
    public String getRedirect(Model model) {
        return "BetaA";
    }

    @GetMapping("/BetaB")
    public String getRedirected(Model model) {
        return "BetaB";
    }

    @GetMapping(value = {"", "/", "/welcome"})
    public String mainWithParam(Model model) {
        model.addAttribute("Beta", featureManager.isEnabled("beta"));
        return "welcome";
    }
}
