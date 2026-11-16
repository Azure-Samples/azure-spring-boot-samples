// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.highavailability;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosEndToEndOperationLatencyPolicyConfigBuilder;
import com.azure.cosmos.ThresholdBasedAvailabilityStrategy;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Configuration
@EnableConfigurationProperties(CosmosProperties.class)
@EnableCosmosRepositories
@EnableReactiveCosmosRepositories
public class SampleAppConfiguration extends AbstractCosmosConfiguration {
    @Autowired
    private CosmosProperties properties;

    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        Assert.isTrue(properties.getEndToEndTimeout().compareTo(properties.getAvailabilityThreshold()) > 0,
            "endToEndTimeout must exceed availabilityThreshold");

        return new CosmosClientBuilder()
            .endpoint(properties.getUri())
            .key(properties.getKey())
            .directMode()
            .preferredRegions(properties.getPreferredRegions())
            .endToEndOperationLatencyPolicyConfig(
                new CosmosEndToEndOperationLatencyPolicyConfigBuilder(properties.getEndToEndTimeout())
                    .availabilityStrategy(new ThresholdBasedAvailabilityStrategy(
                        properties.getAvailabilityThreshold(), properties.getAvailabilityThresholdStep()))
                    .build());
    }

    @Override
    protected String getDatabaseName() {
        return properties.getDatabaseName();
    }
}
