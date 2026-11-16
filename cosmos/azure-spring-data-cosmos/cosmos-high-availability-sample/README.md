---
page_type: sample
languages:
- java
products:
- azure-cosmos-db
name: High availability with Spring Data Cosmos
description: Configure timeout and availability policies for cross-partition Spring Data Cosmos queries.
---

# High availability with Spring Data Cosmos

This Spring Boot 4 sample follows the existing Cosmos sample layout and demonstrates client-wide
timeout and threshold-based availability configuration. Both synchronous and reactive repositories
execute a cross-partition `findByFirstName` query.

## Features

- Configure the client in [SampleAppConfiguration.java](src/main/java/com/azure/spring/data/cosmos/samples/highavailability/SampleAppConfiguration.java)
  using the existing `CosmosProperties` and `application.yaml` pattern.
- Save two uniquely named users with different `lastName` partition-key values, query both by their
  common `firstName`, and verify complete results.
- Clean up only this run's users and close the client when the sample finishes.

There is no fault-injection dependency or test framework in the application. The general
[Cosmos quickstart](../cosmos-quickstart-samples) is unchanged.

## Getting started

### Prerequisites

- JDK 17 or later and Maven.
- An Azure Cosmos DB for NoSQL account using provisioned throughput, key-based authentication,
  and a global account endpoint.
- An account default consistency of **Session or stronger** (Session, Bounded Staleness, or Strong).
  The sample immediately queries its writes using the same client. Eventual and Consistent Prefix accounts
  can temporarily return incomplete results even when healthy; setting Session only on the client does
  not upgrade a weaker account. Verify this prerequisite before running the sample.
- At least two readable account regions to benefit from regional hedging. A single-region account or
  trusted emulator can run the queries but cannot demonstrate regional redundancy.
- Network access for Direct mode and HTTPS gateway metadata requests, including query-plan retrieval.

### Quickstart

1. Clone this repository and change to `cosmos/azure-spring-data-cosmos/cosmos-high-availability-sample`.
2. Set `ACCOUNT_HOST` and `ACCOUNT_KEY`, or create `cosmos-v4.properties` in your user home directory,
   outside the checkout:

   ```properties
   ACCOUNT_HOST=https://<account-name>.documents.azure.com:443/
   ACCOUNT_KEY=<account-key>
   COSMOS_DATABASE=cosmos-high-availability-sample
   PREFERRED_LOCATIONS=East US,West US
   COSMOS_END_TO_END_TIMEOUT=PT10S
   COSMOS_AVAILABILITY_THRESHOLD=PT5S
   COSMOS_AVAILABILITY_THRESHOLD_STEP=PT0.1S
   ```

   Replace the placeholders and use the exact names of your account's read regions. Environment variables
   override the imported file. Never commit credentials.
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

The sample creates the configured database and `highAvailabilityUsers` container if needed, with
400 RU/s for the container. It deletes only its own users; the database and container remain.
Provisioned resources can incur charges after the application exits. Do not delete shared resources
when cleaning up.
Cleanup attempts both deletes even if a save fails, because a timed-out write may still have committed.
Missing users are tolerated. Other cleanup errors are attached as suppressed exceptions to the original
operation failure; if the operation succeeded, the first cleanup error is thrown with later errors suppressed.

## Timeout and availability settings

The values above are the sample defaults except that preferred regions are optional. Durations use
ISO-8601 syntax, and the timeout must exceed the initial availability threshold.
The same configured client serves both repositories. The initial hedging threshold is 5 seconds;
further regions can be considered in 100 ms steps.

Explicit preferred regions make routing order predictable, but are not generally required when
endpoint discovery supplies multiple eligible regions. A healthy run demonstrates configuration and
query results; it does not prove that hedging occurred or simulate an outage.

The SDK query-page timeout is not a deadline for query-plan acquisition plus complete multi-page
consumption. Applications requiring that broader deadline must also implement application-level
timeout/cancellation. Observe actual results and terminal signals rather than relying only on a
diagnostics success status. Cross-partition refers to logical partitions; multiple physical partitions
are not required for this example.

## Resources

- [Per-partition circuit-breaker sample](../cosmos-per-partition-circuit-breaker-sample), which avoids unhealthy physical partitions rather than explicitly configuring threshold-based hedging.
- [Threshold-based availability strategy](https://learn.microsoft.com/azure/cosmos-db/performance-tips-java-sdk-v4#threshold-based-availability-strategy)
- [Azure Spring Data Cosmos](https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring/azure-spring-data-cosmos)
