// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.example.demo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.azure.spring.cloud.feature.management.targeting.TargetingContext;
import com.azure.spring.cloud.feature.management.targeting.TargetingContextAccessor;

@Component
@RequestScope
public class TargetingContextImpl implements TargetingContextAccessor {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void configureTargetingContext(TargetingContext context) {
        context.setUserId(request.getParameter("userId"));
        List<String> groups = new ArrayList<>();
        groups.add(request.getParameter("groups"));
        context.setGroups(groups);
    }

}
