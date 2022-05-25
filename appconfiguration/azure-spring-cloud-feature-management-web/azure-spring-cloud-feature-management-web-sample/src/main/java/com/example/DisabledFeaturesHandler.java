// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.azure.spring.cloud.feature.manager.IDisabledFeaturesHandler;
import org.springframework.stereotype.Component;


@Component
public class DisabledFeaturesHandler implements IDisabledFeaturesHandler{

    @Override
    public HttpServletResponse handleDisabledFeatures(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("FeatureFlag", "false");
        return response;
    }

}
