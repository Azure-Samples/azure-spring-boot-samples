// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.servicebus;

import com.azure.spring.messaging.ConsumerIdentifier;
import com.azure.spring.messaging.PropertiesSupplier;
import com.azure.spring.servicebus.core.properties.CommonProperties;
import com.azure.spring.servicebus.core.properties.ProcessorProperties;
import com.azure.spring.servicebus.core.properties.ProducerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Property class for Service Bus multiple namespaces sample.
 */
@ConfigurationProperties("servicebus")
public class CustomizedServiceBusProperties {

    @NestedConfigurationProperty
    private final List<ProducerProperties> producers = new ArrayList<>();

    @NestedConfigurationProperty
    private final List<ProcessorProperties> processors = new ArrayList<>();

    public PropertiesSupplier<String, ProducerProperties> producerPropertiesSupplier() {
        Map<String, ProducerProperties> map = producers
                .stream()
                .collect(Collectors.toMap(CommonProperties::getEntityName, p -> p));
        return map::get;
    }

    public PropertiesSupplier<ConsumerIdentifier, ProcessorProperties> processorPropertiesSupplier() {
        Map<ConsumerIdentifier, ProcessorProperties> map = processors
                .stream()
                .collect(Collectors.toMap(
                        p -> p.getSubscriptionName() == null ? new ConsumerIdentifier(p.getEntityName()) : new ConsumerIdentifier(p.getEntityName(), p.getSubscriptionName()), p -> p));
        return map::get;
    }

    public List<ProducerProperties> getProducers() {
        return producers;
    }

    public List<ProcessorProperties> getProcessors() {
        return processors;
    }

}
