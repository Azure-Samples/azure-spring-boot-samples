/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.azure.spring.dynamicfeatures;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.azure.spring.cloud.feature.manager.IDynamicFeatureProperties;
import com.azure.spring.dynamicfeatures.models.ShoppingCart;

@Configuration
@ConfigurationProperties(prefix = "config")
public class ApplicationProperties implements IDynamicFeatureProperties {

    private Map<String, ShoppingCart> shoppingCart;

    /**
     * @return the shoppingCart
     */
    public Map<String, ShoppingCart> getShoppingCart() {
        return shoppingCart;
    }

    /**
     * @param shoppingCart the shoppingCart to set
     */
    public void setShoppingCart(Map<String, ShoppingCart> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

}
