// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.availability;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosDiagnosticsHandler;
import com.azure.cosmos.CosmosEndToEndOperationLatencyPolicyConfigBuilder;
import com.azure.cosmos.ThresholdBasedAvailabilityStrategy;
import com.azure.cosmos.models.CosmosClientTelemetryConfig;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@EnableCosmosRepositories
@EnableReactiveCosmosRepositories
public class SampleConfiguration extends AbstractCosmosConfiguration {

    @Value("${cosmos.endpoint}")
    private String endpoint;

    @Value("${cosmos.key}")
    private String key;

    @Value("${cosmos.database}")
    private String database;

    @Value("${cosmos.preferred-regions}")
    private String preferredRegions;

    @Value("${cosmos.end-to-end-timeout}")
    private Duration timeout;

    @Value("${cosmos.availability.threshold}")
    private Duration threshold;

    @Value("${cosmos.availability.threshold-step}")
    private Duration thresholdStep;

    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        Assert.isTrue(timeout.compareTo(threshold) > 0,
            "cosmos.end-to-end-timeout must exceed cosmos.availability.threshold");

        CosmosClientBuilder builder = new CosmosClientBuilder()
            .endpoint(endpoint)
            .directMode()
            .preferredRegions(Arrays.asList(StringUtils.tokenizeToStringArray(preferredRegions, ",")))
            .endToEndOperationLatencyPolicyConfig(
                new CosmosEndToEndOperationLatencyPolicyConfigBuilder(timeout)
                    .availabilityStrategy(new ThresholdBasedAvailabilityStrategy(threshold, thresholdStep))
                    .build())
            .clientTelemetryConfig(new CosmosClientTelemetryConfig()
                .diagnosticsHandler(CosmosDiagnosticsHandler.DEFAULT_LOGGING_HANDLER));

        if (StringUtils.hasText(key)) {
            return builder.key(key);
        }
        return builder.credential(new DefaultAzureCredentialBuilder().build());
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }
}
