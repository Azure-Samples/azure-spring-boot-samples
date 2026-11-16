---
page_type: sample
languages:
- java
products:
- azure-cosmos-db
name: Per-partition circuit breaker with Spring Data Cosmos
description: Enable the Cosmos DB SDK per-partition circuit breaker for ordinary Spring Data repository operations.
---

# Per-partition circuit breaker with Spring Data Cosmos

This Spring Boot 4 sample enables the Java SDK's per-partition circuit breaker (PPCB), then executes
synchronous and reactive cross-partition queries. It follows the existing Cosmos sample layout and
does not add fault-injection or third-party circuit-breaker libraries.

## Features

- Configure PPCB in [SampleAppConfiguration.java](src/main/java/com/azure/spring/data/cosmos/samples/circuitbreaker/SampleAppConfiguration.java)
  before the Cosmos client is constructed.
- Bind preferred regions and read/write failure thresholds through `CosmosProperties` and `application.yaml`.
- Save users in two logical partitions, consume and verify both query results, and delete only this run's users.

PPCB tracks failures for a **physical partition in a region**, not for each logical partition-key value
or the whole application. After sufficient terminal failures, subsequent eligible data requests can avoid
the unhealthy partition/region. Initial requests can still incur retries and timeouts. The SDK manages recovery.

## Getting started

### Prerequisites

- JDK 17 or later and Maven.
- A provisioned-throughput Azure Cosmos DB for NoSQL account with key-based authentication and a global endpoint.
- An account default consistency of **Session or stronger** (Session, Bounded Staleness, or Strong).
  The sample immediately queries its writes using the same client. Eventual and Consistent Prefix accounts
  can temporarily return incomplete results even when healthy; setting Session only on the client does
  not upgrade a weaker account. Verify this prerequisite before running the sample.
- For read and write regional recovery, use a multi-region write account with at least two writable regions.
  The client opts into multiple write regions; it does not change the account configuration.
- Read-side recovery needs multiple eligible read regions in the SDK used by this sample. With no eligible
  alternate region, normal queries can still run, but enabling PPCB cannot demonstrate regional rerouting.
- Direct-mode and HTTPS gateway connectivity.

### Quickstart

1. Change to `cosmos/azure-spring-data-cosmos/cosmos-per-partition-circuit-breaker-sample`.
2. Set environment variables, or create `cosmos-v4.properties` in your user home directory outside the checkout:

   ```properties
   ACCOUNT_HOST=https://<account-name>.documents.azure.com:443/
   ACCOUNT_KEY=<account-key>
   COSMOS_DATABASE=cosmos-per-partition-circuit-breaker-sample
   PREFERRED_LOCATIONS=East US,West US
   COSMOS_PPCB_READ_FAILURE_THRESHOLD=10
   COSMOS_PPCB_WRITE_FAILURE_THRESHOLD=5
   ```

   Replace the placeholders and use your account's actual read/write regions in preference order.
   Environment variables override the imported file. Never commit credentials.
3. Build and run:

   ```powershell
   mvn clean verify
   mvn spring-boot:run
   ```

Expected output:

```text
Synchronous cross-partition query returned 2 users.
Reactive cross-partition query completed with 2 users.
```

The sample creates the database and `circuitBreakerUsers` container if needed, provisioning 400 RU/s for
the container. It attempts to delete only its own uniquely named users in `finally` and closes the client.
The database/container remain and can incur charges; do not delete shared resources when cleaning up.
Cleanup attempts both deletes even if a save fails, because a timed-out write may still have committed.
Missing users are tolerated. Other cleanup errors are attached as suppressed exceptions to the original
operation failure; if the operation succeeded, the first cleanup error is thrown with later errors suppressed.

## Circuit-breaker settings

The sample sets the documented JVM property before constructing its client:

```text
COSMOS.PARTITION_LEVEL_CIRCUIT_BREAKER_CONFIG
```

The JSON contains `isPartitionLevelCircuitBreakerEnabled=true`,
`circuitBreakerType=CONSECUTIVE_EXCEPTION_COUNT_BASED`, and the configured
`consecutiveExceptionCountToleratedForReads` / `consecutiveExceptionCountToleratedForWrites` values.
The read/write defaults are 10/5. These are failure counts, not durations. The sample rejects values below
the SDK's accepted minimums instead of silently falling back to defaults.

This setting is **process-wide**, not a Spring request-level option. In an application with multiple
Cosmos clients, configure it centrally before creating any of them; do not toggle it per request.
The sample does not explicitly configure threshold-based hedging. Other SDK features/defaults still apply.

## Scope and limitations

- A healthy run validates configuration and query results; it does not force the circuit to open or prove failover.
- Two logical partitions can share a physical partition, so this small dataset does not prove isolation
  between physical partitions.
- **PPCB does not apply to QueryPlan requests in the SDK used by this sample.** Query planning still uses
  the gateway; PPCB alone is not a mitigation for an outage affecting regional QueryPlan calls.
- See the separate [high-availability sample](../cosmos-high-availability-sample) for explicit threshold-based
  availability/hedging configuration. Both mechanisms can be used together, but have different scopes.
- Failure-count thresholds do not provide an end-to-end deadline. Applications must handle their own
  timeout/cancellation requirements and observe actual query completion/errors.

## Resources

- [Java SDK partition-level circuit breaker](https://learn.microsoft.com/azure/cosmos-db/performance-tips-java-sdk-v4#partition-level-circuit-breaker)
- [High-availability sample](../cosmos-high-availability-sample)
