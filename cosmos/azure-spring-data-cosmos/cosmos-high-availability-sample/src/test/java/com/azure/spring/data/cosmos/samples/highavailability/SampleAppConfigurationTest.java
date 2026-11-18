// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.highavailability;

import com.azure.core.credential.TokenCredential;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosEndToEndOperationLatencyPolicyConfig;
import com.azure.cosmos.ThresholdBasedAvailabilityStrategy;
import com.azure.identity.DefaultAzureCredential;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_SELF;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

class SampleAppConfigurationTest {
    private static final String PPCB_PROPERTY = "COSMOS.PARTITION_LEVEL_CIRCUIT_BREAKER_CONFIG";
    private final CosmosProperties properties = new CosmosProperties();
    private final SampleAppConfiguration configuration = new SampleAppConfiguration();
    private String previousConfig;

    @BeforeEach
    void setUp() {
        previousConfig = System.getProperty(PPCB_PROPERTY);
        properties.setUri("https://sample.documents.azure.com:443/");
        properties.setPreferredRegions(List.of("East US", "West US"));
        properties.setEndToEndTimeout(Duration.ofSeconds(10));
        properties.setAvailabilityThreshold(Duration.ofSeconds(5));
        properties.setAvailabilityThresholdStep(Duration.ofMillis(100));
        properties.setReadFailureThreshold(20);
        properties.setWriteFailureThreshold(10);
        ReflectionTestUtils.setField(configuration, "properties", properties);
    }

    @AfterEach
    void restoreProcessConfiguration() {
        if (previousConfig == null) {
            System.clearProperty(PPCB_PROPERTY);
        } else {
            System.setProperty(PPCB_PROPERTY, previousConfig);
        }
    }

    @Test
    void enablesBothStrategiesAndUsesEntraIdWithoutAKey() {
        try (MockedConstruction<CosmosClientBuilder> builders = mockConstruction(CosmosClientBuilder.class,
            withSettings().defaultAnswer(RETURNS_SELF), (builder, context) -> {
                JsonNode ppcb = new ObjectMapper().readTree(System.getProperty(PPCB_PROPERTY));
                assertThat(ppcb.path("isPartitionLevelCircuitBreakerEnabled").asBoolean()).isTrue();
                assertThat(ppcb.path("consecutiveExceptionCountToleratedForReads").asInt()).isEqualTo(20);
                assertThat(ppcb.path("consecutiveExceptionCountToleratedForWrites").asInt()).isEqualTo(10);
            })) {
            configuration.cosmosClientBuilder();

            CosmosClientBuilder builder = builders.constructed().get(0);
            ArgumentCaptor<CosmosEndToEndOperationLatencyPolicyConfig> policy =
                ArgumentCaptor.forClass(CosmosEndToEndOperationLatencyPolicyConfig.class);
            verify(builder).endToEndOperationLatencyPolicyConfig(policy.capture());
            assertThat(policy.getValue().getEndToEndOperationTimeout()).isEqualTo(Duration.ofSeconds(10));
            ThresholdBasedAvailabilityStrategy availability =
                (ThresholdBasedAvailabilityStrategy) policy.getValue().getAvailabilityStrategy();
            assertThat(availability.getThreshold()).isEqualTo(Duration.ofSeconds(5));
            assertThat(availability.getThresholdStep()).isEqualTo(Duration.ofMillis(100));
            verify(builder).multipleWriteRegionsEnabled(true);
            verify(builder).preferredRegions(properties.getPreferredRegions());
            ArgumentCaptor<TokenCredential> credential = ArgumentCaptor.forClass(TokenCredential.class);
            verify(builder).credential(credential.capture());
            assertThat(credential.getValue()).isInstanceOf(DefaultAzureCredential.class);
            verify(builder, never()).key(any());
        }
    }

    @Test
    void retainsExplicitKeyAuthentication() {
        properties.setKey("sample-key");
        try (MockedConstruction<CosmosClientBuilder> builders =
            mockConstruction(CosmosClientBuilder.class, withSettings().defaultAnswer(RETURNS_SELF))) {
            configuration.cosmosClientBuilder();

            CosmosClientBuilder builder = builders.constructed().get(0);
            verify(builder).key("sample-key");
            verify(builder, never()).credential(any(TokenCredential.class));
        }
    }

    @ParameterizedTest
    @CsvSource({"9,5", "10,4"})
    void rejectsUnsupportedCircuitBreakerThresholds(int reads, int writes) {
        properties.setReadFailureThreshold(reads);
        properties.setWriteFailureThreshold(writes);

        assertThrows(IllegalArgumentException.class, configuration::cosmosClientBuilder);
    }
}
