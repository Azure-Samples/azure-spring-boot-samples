// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.example;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.azure.spring.cloud.feature.management.FeatureManager;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
public class FeatureFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureFilter.class);

    @Autowired
    private FeatureManager featureManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!featureManager.isEnabled("beta")) {
            chain.doFilter(request, response);
            return;
        }
        LOGGER.info("Run the Beta filter");
        chain.doFilter(request, response);
    }
}
