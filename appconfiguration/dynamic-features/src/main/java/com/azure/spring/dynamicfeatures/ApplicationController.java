/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.azure.spring.dynamicfeatures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.spring.cloud.feature.manager.DynamicFeatureManagerSnapshot;
import com.azure.spring.dynamicfeatures.models.ShoppingCart;

@RestController
public class ApplicationController {

    @Autowired
    private DynamicFeatureManagerSnapshot dynamicFeatureManager;

    @GetMapping("")
    public String getMessage() {
        return dynamicFeatureManager
            .getVariantAsync("ShoppingCart", ShoppingCart.class).block().toString();
    }
}
