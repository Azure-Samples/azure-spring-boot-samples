// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.samples.highavailability;

import com.azure.cosmos.CosmosDiagnostics;
import com.azure.cosmos.CosmosDiagnosticsContext;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.test.faultinjection.CosmosFaultInjectionHelper;
import com.azure.cosmos.test.faultinjection.FaultInjectionConditionBuilder;
import com.azure.cosmos.test.faultinjection.FaultInjectionConnectionType;
import com.azure.cosmos.test.faultinjection.FaultInjectionResultBuilders;
import com.azure.cosmos.test.faultinjection.FaultInjectionRule;
import com.azure.cosmos.test.faultinjection.FaultInjectionRuleBuilder;
import com.azure.cosmos.test.faultinjection.FaultInjectionServerErrorType;
import com.azure.spring.data.cosmos.CosmosFactory;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.exception.CosmosNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies synchronous and reactive queries recover from an outage of the first preferred region's Gateway path.
 * Cases run serially with fresh clients, using fault hits, complete results and contacted-region diagnostics
 * as recovery evidence.
 */
@SpringBootTest(classes = {SampleAppConfiguration.class, GatewayRecoveryIT.LiveConfiguration.class},
    webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Execution(ExecutionMode.SAME_THREAD)
class GatewayRecoveryIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayRecoveryIT.class);

    @Autowired
    private UserRepository repository;

    @Autowired
    private ReactiveUserRepository reactiveRepository;

    @Autowired
    private CosmosFactory factory;

    @Autowired
    private CosmosProperties properties;

    @Autowired
    private AtomicReference<CosmosDiagnostics> diagnostics;

    @Value("${cosmos.test.sdk-version}")
    private String expectedSdkVersion;

    private final List<User> users = new ArrayList<>();
    private FaultInjectionRule delayedRegion;
    private FaultInjectionRule healthyRegionProbe;
    private String firstName;

    @BeforeEach
    void seedAndWarmRoutingMetadata() {
        List<String> regions = properties.getPreferredRegions();
        assertThat(regions).as("Configure exactly two account regions using PREFERRED_LOCATIONS").hasSize(2);
        assertThat(regions.get(1)).isNotEqualToIgnoringCase(regions.get(0));

        firstName = "gateway-it-" + UUID.randomUUID();
        // lastName is the partition key; querying the shared firstName spans two logical partitions.
        users.add(new User(firstName + "-one", firstName, "partition-one"));
        users.add(new User(firstName + "-two", firstName, "partition-two"));
        users.forEach(repository::save);

        // Establish a healthy baseline before simulating a Gateway outage during steady-state queries.
        assertResults(repository.findByFirstName(firstName));
        // Capture diagnostics from the recovery query only.
        diagnostics.set(null);
    }

    @ParameterizedTest(name = "{1} query recovers from first-region Gateway {0}")
    @CsvSource({
        "CONNECTION_DELAY,SYNCHRONOUS",
        "RESPONSE_DELAY,SYNCHRONOUS",
        "CONNECTION_DELAY,REACTIVE",
        "RESPONSE_DELAY,REACTIVE"
    })
    @Timeout(120)
    void recoversFromFirstRegionGatewayDelay(FaultInjectionServerErrorType delayType, RepositoryApi api) {
        // Allow the recovery query the configured timeout plus five seconds to complete.
        Duration recoveryBudget = properties.getEndToEndTimeout().plusSeconds(5);
        // Request a first-region delay five minutes longer than the recovery allowance.
        Duration outageDelay = recoveryBudget.plus(Duration.ofMinutes(5));
        delayedRegion = rule(properties.getPreferredRegions().get(0), delayType, outageDelay, true);
        // Record Gateway traffic to the second region while allowing its normal service response.
        healthyRegionProbe = rule(properties.getPreferredRegions().get(1),
            FaultInjectionServerErrorType.RESPONSE_DELAY, Duration.ZERO, false);

        // Apply the regional fault and observation rules to this test's Cosmos client.
        CosmosFaultInjectionHelper.configureFaultInjectionRules(
            factory.getCosmosAsyncClient().getDatabase(factory.getDatabaseName()).getContainer("highAvailabilityUsers"),
            List.of(delayedRegion, healthyRegionProbe)).block();

        long start = System.nanoTime();
        List<User> results = api == RepositoryApi.SYNCHRONOUS
            ? repository.findByFirstName(firstName)
            : reactiveRepository.findByFirstName(firstName).collectList().block(recoveryBudget);
        // Verify the completed query returns the expected users from both logical partitions.
        assertResults(results);
        assertThat(Duration.ofNanos(System.nanoTime() - start)).isLessThan(recoveryBudget);

        // Confirm the first region was delayed and the healthy region received Gateway requests.
        assertGatewayHit(delayedRegion);
        assertGatewayHit(healthyRegionProbe);
        CosmosDiagnostics queryDiagnostics = diagnostics.get();
        assertThat(queryDiagnostics).isNotNull();
        // Validate the runtime SDK version reported by query diagnostics.
        assertThat(queryDiagnostics.getUserAgent()).startsWith("azsdk-java-cosmos/" + expectedSdkVersion + " ");
        // Cross-region evidence: the query context must record both preferred regions as contacted.
        CosmosDiagnosticsContext queryContext = queryDiagnostics.getDiagnosticsContext();
        assertThat(queryContext).isNotNull();
        assertThat(queryContext.getContactedRegionNames().stream()
            .map(region -> region.toLowerCase(Locale.ROOT)).toList())
            .as("The recovered query contacts both the delayed and healthy preferred regions")
            .contains(properties.getPreferredRegions().get(0).toLowerCase(Locale.ROOT),
                properties.getPreferredRegions().get(1).toLowerCase(Locale.ROOT));
        LOGGER.info("Gateway recovery verified: SDK {}, {} / {}, first region {}, healthy region {}",
            expectedSdkVersion, delayType, api, properties.getPreferredRegions().get(0),
            properties.getPreferredRegions().get(1));
        LOGGER.info("Gateway operations observed: delayed {}, healthy {}",
            delayedRegion.getHitCountDetails(), healthyRegionProbe.getHitCountDetails());
        LOGGER.info("Query context contacted regions: {}", queryContext.getContactedRegionNames());
    }

    @AfterEach
    void disableFaultsAndDeleteOwnUsers() {
        // Disable the faults before deleting this run's users.
        if (delayedRegion != null) {
            delayedRegion.disable();
        }
        if (healthyRegionProbe != null) {
            healthyRegionProbe.disable();
        }
        RuntimeException failure = null;
        for (User user : users) {
            try {
                repository.deleteById(user.getId(), new PartitionKey(user.getLastName()));
            } catch (CosmosNotFoundException absent) {
                LOGGER.debug("Test user {} is already absent.", user.getId());
            } catch (RuntimeException cleanupFailure) {
                if (failure == null) {
                    failure = cleanupFailure;
                } else if (failure != cleanupFailure) {
                    failure.addSuppressed(cleanupFailure);
                }
            }
        }
        if (failure != null) {
            throw failure;
        }
    }

    private FaultInjectionRule rule(String region, FaultInjectionServerErrorType type,
                                   Duration delay, boolean suppressServiceRequests) {
        return new FaultInjectionRuleBuilder("gateway-" + UUID.randomUUID())
            .condition(new FaultInjectionConditionBuilder()
                // Target Gateway requests to this region, including QueryPlan and other metadata operations.
                .connectionType(FaultInjectionConnectionType.GATEWAY)
                .region(region)
                .build())
            .result(FaultInjectionResultBuilders.getResultBuilder(type)
                .delay(delay)
                .suppressServiceRequests(suppressServiceRequests)
                .build())
            .duration(properties.getEndToEndTimeout().plusSeconds(5).multipliedBy(4).plusSeconds(30))
            .build();
    }

    private static void assertGatewayHit(FaultInjectionRule rule) {
        assertThat(rule.getRegionEndpoints()).hasSize(1);
        assertThat(rule.getHitCount()).isPositive();
        assertThat(rule.getHitCountDetails()).isNotEmpty();
    }

    private void assertResults(List<User> results) {
        assertThat(results).isNotNull().hasSize(2);
        assertThat(results).extracting(User::getId).containsExactlyInAnyOrderElementsOf(
            users.stream().map(User::getId).toList());
    }

    enum RepositoryApi {
        SYNCHRONOUS,
        REACTIVE
    }

    @TestConfiguration
    static class LiveConfiguration {
        @Bean
        AtomicReference<CosmosDiagnostics> capturedDiagnostics() {
            return new AtomicReference<>();
        }

        @Bean
        @Primary
        CosmosConfig liveCosmosConfig() {
            // Capture query diagnostics for the SDK-version and contacted-region assertions.
            return CosmosConfig.builder()
                .responseDiagnosticsProcessor(response -> capturedDiagnostics().set(response.getCosmosDiagnostics()))
                .build();
        }
    }
}
