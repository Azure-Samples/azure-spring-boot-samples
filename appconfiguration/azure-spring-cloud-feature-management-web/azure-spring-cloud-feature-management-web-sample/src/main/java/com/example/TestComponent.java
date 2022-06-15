// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.example;

import java.util.concurrent.ExecutionException;

import com.azure.spring.cloud.feature.manager.FeatureManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestComponent {

    @Autowired
    private FeatureManager featureManager;

    public String test() throws InterruptedException, ExecutionException {
        if (featureManager.isEnabledAsync("Beta").block()) {
            return "Beta";
        }
        return "Original";
    }

}
