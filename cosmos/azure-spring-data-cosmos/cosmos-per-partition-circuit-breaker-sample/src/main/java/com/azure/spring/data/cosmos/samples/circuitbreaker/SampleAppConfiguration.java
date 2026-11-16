// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.circuitbreaker;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

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
        Assert.isTrue(properties.getReadFailureThreshold() >= 10, "readFailureThreshold must be at least 10");
        Assert.isTrue(properties.getWriteFailureThreshold() >= 5, "writeFailureThreshold must be at least 5");

        String circuitBreakerConfig = String.format(Locale.ROOT,
            "{\"isPartitionLevelCircuitBreakerEnabled\":true,"
                + "\"circuitBreakerType\":\"CONSECUTIVE_EXCEPTION_COUNT_BASED\","
                + "\"consecutiveExceptionCountToleratedForReads\":%d,"
                + "\"consecutiveExceptionCountToleratedForWrites\":%d}",
            properties.getReadFailureThreshold(), properties.getWriteFailureThreshold());
        // This SDK setting is process-wide and must be applied before constructing any Cosmos client.
        System.setProperty("COSMOS.PARTITION_LEVEL_CIRCUIT_BREAKER_CONFIG", circuitBreakerConfig);

        return new CosmosClientBuilder()
            .endpoint(properties.getUri())
            .key(properties.getKey())
            .directMode()
            .multipleWriteRegionsEnabled(true)
            .preferredRegions(properties.getPreferredRegions());
    }

    @Override
    protected String getDatabaseName() {
        return properties.getDatabaseName();
    }
}
