// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.servicebus;

import com.azure.spring.messaging.PropertiesSupplier;
import com.azure.spring.servicebus.core.properties.ProcessorProperties;
import com.azure.spring.servicebus.core.properties.ProducerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.azure.spring.servicebus.core.processor.DefaultServiceBusNamespaceProcessorFactory.INVALID_SUBSCRIPTION;

@ConfigurationProperties("servicebus")
public class CustomizedServiceBusProperties {

    @NestedConfigurationProperty
    private final List<ProducerProperties> producers = new ArrayList<>();

    @NestedConfigurationProperty
    private final List<ProcessorProperties> processors = new ArrayList<>();

    public PropertiesSupplier<String, ProducerProperties> producerPropertiesSupplier() {
        Map<String, ProducerProperties> map = producers.stream().collect(Collectors.toMap(p -> p.getEntityName(),
            p -> p));
        return new PropertiesSupplier<String, ProducerProperties>() {
            @Override
            public ProducerProperties getProperties(String key) {
                return map.get(key);
            }
        };
    }

    public PropertiesSupplier<Tuple2<String, String>, ProcessorProperties> processorPropertiesSupplier() {
        Map<Tuple2<String, String>, ProcessorProperties> map = processors.stream().collect(Collectors.toMap(
            p -> Tuples.of(p.getEntityName(), p.getSubscriptionName() != null ? p.getSubscriptionName() : INVALID_SUBSCRIPTION),
            p -> p));
        return  new PropertiesSupplier<Tuple2<String, String>, ProcessorProperties>() {
            @Override
            public ProcessorProperties getProperties(Tuple2<String, String> key) {
                return map.get(key);
            }
        };
    }

    public List<ProducerProperties> getProducers() {
        return producers;
    }

    public List<ProcessorProperties> getProcessors() {
        return processors;
    }

}
