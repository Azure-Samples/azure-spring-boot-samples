// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.example;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azure.spring.cloud.feature.management.FeatureManager;

@Component
public class TestComponent {

    @Autowired
    private FeatureManager featureManager;

    public String test() throws InterruptedException, ExecutionException {
        if (featureManager.isEnabled("beta")) {
            return "Beta";
        }
        return "Original";
    }

}
