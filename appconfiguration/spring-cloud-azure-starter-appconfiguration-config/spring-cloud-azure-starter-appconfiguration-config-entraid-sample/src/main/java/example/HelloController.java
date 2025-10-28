/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.azure.spring.cloud.feature.management.FeatureManager;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class HelloController {

    @Autowired
    private MessageProperties properties;

    @Autowired
    private FeatureManager featureManager;

    @GetMapping("")
    public String getMessage() throws JsonProcessingException {
        return properties.getMessage();
    }

    @GetMapping("/feature-flag/{name}")
    public String getFeatureFlag(@PathVariable String name) {
        if (featureManager.isEnabled(name)) {
            return "Feature flag '" + name + "' is enabled.";
        } else {
            return "Feature flag '" + name + "' is disabled.";
        }
    }

}
