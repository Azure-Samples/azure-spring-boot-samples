// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.servicebus;

import com.azure.spring.messaging.servicebus.core.properties.NamespaceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Property class for Service Bus multiple namespaces sample.
 */
@ConfigurationProperties("my.servicebus")
public class CustomizedServiceBusProperties {

    private final List<NamespaceProperties> namespaces = new ArrayList<>();

    public List<NamespaceProperties> getNamespaces() {
        return namespaces;
    }
}
