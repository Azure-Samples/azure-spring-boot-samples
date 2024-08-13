// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.example;

import com.azure.spring.cloud.feature.management.web.DisabledFeaturesHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;


@Component
public class MyDisabledFeaturesHandler implements DisabledFeaturesHandler {

    @Override
    public HttpServletResponse handleDisabledFeatures(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("FeatureFlag", "false");
        return response;
    }

}
