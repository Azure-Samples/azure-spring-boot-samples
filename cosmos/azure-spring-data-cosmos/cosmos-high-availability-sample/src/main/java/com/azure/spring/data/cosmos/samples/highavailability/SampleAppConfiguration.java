// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.highavailability;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosEndToEndOperationLatencyPolicyConfigBuilder;
import com.azure.cosmos.ThresholdBasedAvailabilityStrategy;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Locale;

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
        Assert.isTrue(properties.getReadFailureThreshold() >= 10, "readFailureThreshold must be at least 10");
        Assert.isTrue(properties.getWriteFailureThreshold() >= 5, "writeFailureThreshold must be at least 5");

        enablePartitionLevelCircuitBreaker();

        CosmosClientBuilder builder = new CosmosClientBuilder()
            .endpoint(properties.getUri())
            .directMode()
            .multipleWriteRegionsEnabled(true)
            .preferredRegions(properties.getPreferredRegions())
            .endToEndOperationLatencyPolicyConfig(
                new CosmosEndToEndOperationLatencyPolicyConfigBuilder(properties.getEndToEndTimeout())
                    .availabilityStrategy(new ThresholdBasedAvailabilityStrategy(
                        properties.getAvailabilityThreshold(), properties.getAvailabilityThresholdStep()))
                    .build());

        if (StringUtils.hasText(properties.getKey())) {
            return builder.key(properties.getKey());
        }
        return builder.credential(new DefaultAzureCredentialBuilder().build());
    }

    private void enablePartitionLevelCircuitBreaker() {
        String circuitBreakerConfig = String.format(Locale.ROOT,
            "{\"isPartitionLevelCircuitBreakerEnabled\":true,"
                + "\"circuitBreakerType\":\"CONSECUTIVE_EXCEPTION_COUNT_BASED\","
                + "\"consecutiveExceptionCountToleratedForReads\":%d,"
                + "\"consecutiveExceptionCountToleratedForWrites\":%d}",
            properties.getReadFailureThreshold(), properties.getWriteFailureThreshold());
        // This SDK setting is process-wide and must be applied before constructing any Cosmos client.
        System.setProperty("COSMOS.PARTITION_LEVEL_CIRCUIT_BREAKER_CONFIG", circuitBreakerConfig);
    }

    @Override
    protected String getDatabaseName() {
        return properties.getDatabaseName();
    }
}
