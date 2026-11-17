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

This Spring Boot 4 sample enables both **threshold-based availability (hedging)** and the
**per-partition circuit breaker (PPCB)** on the same Cosmos client, with an end-to-end operation latency
policy. Both synchronous and reactive repositories execute a cross-partition `findByFirstName` query.

## Features

- Configure the client in [SampleAppConfiguration.java](src/main/java/com/azure/spring/data/cosmos/samples/highavailability/SampleAppConfiguration.java)
  using the existing `CosmosProperties` and `application.yaml` pattern.
- Use Microsoft Entra ID (AAD) with `DefaultAzureCredential`, or optional account-key authentication.
- Enable PPCB before client construction and use it alongside the client-level hedging policy.
- Save two uniquely named users with different `lastName` partition-key values, query both by their
  common `firstName`, and verify complete results.
- Clean up only this run's users and close the client when the sample finishes.

There is no fault-injection dependency or test framework in the application. The general
[Cosmos quickstart](../cosmos-quickstart-samples) is unchanged.

## Getting started

### Prerequisites

- JDK 17 or later and Maven.
- An Azure Cosmos DB for NoSQL account and a global account endpoint.
- An account default consistency of **Session or stronger** (Session, Bounded Staleness, or Strong).
  The sample immediately queries its writes using the same client. Eventual and Consistent Prefix accounts
  can temporarily return incomplete results even when healthy; setting Session only on the client does
  not upgrade a weaker account. Verify this prerequisite before running the sample.
- At least two readable account regions to benefit from regional hedging. A single-region account or
  trusted emulator can run the queries but cannot demonstrate regional redundancy.
- To benefit from write-side PPCB, use a multi-region write account with at least two writable regions.
  The client opts into multiple write regions; it does not change the account configuration.
- Network access for Direct mode and HTTPS gateway metadata requests, including query-plan retrieval.
- Azure CLI for the resource and local Entra ID setup below.

### Create the database and container

Create these resources once using the Azure portal or Azure CLI before running with either authentication
method. The application does not create resources, so its identity only needs data-plane permissions:

```powershell
az login
az account set --subscription "<subscription-id>"
$resourceGroup = "<resource-group>"
$accountName = "<account-name>"
$databaseName = "cosmos-high-availability-sample"
$accountId = az cosmosdb show --resource-group $resourceGroup --name $accountName --query id -o tsv

az cosmosdb sql database create --resource-group $resourceGroup --account-name $accountName --name $databaseName
az cosmosdb sql container create --resource-group $resourceGroup --account-name $accountName `
    --database-name $databaseName --name highAvailabilityUsers --partition-key-path /lastName --throughput 400
```

For a serverless account, omit `--throughput`. Creating resources requires management-plane permissions;
an administrator can perform this step separately from running the application.

### Microsoft Entra ID (AAD), recommended

With no account key configured, the application uses `DefaultAzureCredential`. For local development,
the Azure CLI login above supplies the identity. Assign that identity the **Cosmos DB Built-in Data
Contributor** data-plane role on the sample container:

```powershell
$principalId = az ad signed-in-user show --query id -o tsv
az cosmosdb sql role assignment create --resource-group $resourceGroup --account-name $accountName `
    --principal-id $principalId `
    --role-definition-id "$accountId/sqlRoleDefinitions/00000000-0000-0000-0000-000000000002" `
    --scope "$accountId/dbs/$databaseName/colls/highAvailabilityUsers"

$env:ACCOUNT_HOST = "https://<account-name>.documents.azure.com:443/"
$env:COSMOS_DATABASE = $databaseName
$env:PREFERRED_LOCATIONS = "East US,West US"
$env:ACCOUNT_KEY = ""
```

Replace placeholders and region names with your own. Creating a role assignment requires appropriate
management-plane permissions; ask an administrator if needed, and allow time for propagation.
Azure management-plane Contributor alone does not grant data-plane item/query access.
Use the object ID of the actual application identity when running as a service principal or managed
identity in Azure, rather than the signed-in developer. `DefaultAzureCredential` supports those identities too.

Remove `ACCOUNT_KEY` from the external properties file below when using Entra ID. A nonempty key takes
precedence over `DefaultAzureCredential`. The pre-created container and `autoCreateContainer=false`
avoid database/container creation calls that a data-plane role does not authorize.

### Optional account-key authentication

For an existing key-authenticated development setup, enable key authentication on the account and set
`ACCOUNT_HOST` and `ACCOUNT_KEY`, or create `cosmos-v4.properties` in your user home directory outside
the checkout:

```properties
ACCOUNT_HOST=https://<account-name>.documents.azure.com:443/
ACCOUNT_KEY=<account-key>
COSMOS_DATABASE=cosmos-high-availability-sample
PREFERRED_LOCATIONS=East US,West US
```

Environment variables override the imported file. Never commit credentials or pass keys in command-line
arguments. This file is optional for passwordless authentication.

### Build and run

From `cosmos/azure-spring-data-cosmos/cosmos-high-availability-sample`:

```powershell
mvn clean verify
mvn spring-boot:run
```

Expected output:

```text
Synchronous cross-partition query returned 2 users.
Reactive cross-partition query completed with 2 users.
```

The sample uses the existing database and `highAvailabilityUsers` container with partition key `/lastName`.
It deletes only its own users; the database and container remain.
Provisioned resources can incur charges after the application exits. Do not delete shared resources
when cleaning up.
Cleanup attempts both deletes even if a save fails, because a timed-out write may still have committed.
Missing users are tolerated. Other cleanup errors are attached as suppressed exceptions to the original
operation failure; if the operation succeeded, the first cleanup error is thrown with later errors suppressed.

## Combined availability settings

Set these environment variables, or add them to the optional external properties file to override the defaults:

```properties
COSMOS_END_TO_END_TIMEOUT=PT10S
COSMOS_AVAILABILITY_THRESHOLD=PT5S
COSMOS_AVAILABILITY_THRESHOLD_STEP=PT0.1S
COSMOS_PPCB_READ_FAILURE_THRESHOLD=10
COSMOS_PPCB_WRITE_FAILURE_THRESHOLD=5
```

Durations use ISO-8601 syntax, and the timeout must exceed the initial availability threshold.
The same configured client serves both repositories. The initial hedging threshold is 5 seconds;
further regions can be considered in 100 ms steps.

The timeout and hedging portion of the `CosmosClientBuilder` configuration looks like this with the
sample defaults:

```java
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosEndToEndOperationLatencyPolicyConfigBuilder;
import com.azure.cosmos.ThresholdBasedAvailabilityStrategy;
import com.azure.identity.DefaultAzureCredentialBuilder;
import java.time.Duration;

CosmosClientBuilder builder = new CosmosClientBuilder()
    .endpoint(System.getenv("ACCOUNT_HOST"))
    .credential(new DefaultAzureCredentialBuilder().build())
    .directMode()
    .endToEndOperationLatencyPolicyConfig(
        new CosmosEndToEndOperationLatencyPolicyConfigBuilder(Duration.ofSeconds(10))
            .availabilityStrategy(new ThresholdBasedAvailabilityStrategy(
                Duration.ofSeconds(5), Duration.ofMillis(100)))
            .build());
```

Return the configured builder from the Spring `@Bean` method rather than building another client.
This excerpt shows timeout and hedging only; the full
[SampleAppConfiguration.java](src/main/java/com/azure/spring/data/cosmos/samples/highavailability/SampleAppConfiguration.java)
also sets preferred regions, selects authentication, and enables PPCB before constructing the client.

PPCB tracks terminal failures for a **physical partition in a region** and routes subsequent eligible
data requests away from unhealthy locations. Failure thresholds default to 10 for reads and 5 for writes;
the sample rejects smaller values rather than triggering the SDK's default fallback.
Initial requests can still fail before the circuit opens, and the SDK manages recovery.
PPCB's `COSMOS.PARTITION_LEVEL_CIRCUIT_BREAKER_CONFIG` JVM setting is **process-wide** and must be set
before constructing any Cosmos clients. In a multi-client application, configure it centrally rather
than toggling it per request.

Both mechanisms are enabled by default because they complement each other. To experiment with hedging
without explicitly enabling PPCB, omit the `enablePartitionLevelCircuitBreaker()` call before client
construction. To omit the explicit hedging/timeout policy, remove the
`endToEndOperationLatencyPolicyConfig(...)` builder call instead. Other SDK features/defaults still apply.

Explicit preferred regions make routing order predictable, but are not generally required when
endpoint discovery supplies multiple eligible regions. A healthy run demonstrates configuration and
query results; it does not prove that hedging occurred, force a circuit to open, or simulate an outage.
Read-side PPCB needs multiple eligible read regions; write-side PPCB needs multiple writable regions.
**PPCB excludes QueryPlan requests in the SDK used here.** Hedging is therefore still relevant for
regional query-plan failures even when PPCB is enabled. Two logical partitions can share a physical
partition; this dataset does not demonstrate physical-partition isolation.

The SDK query-page timeout is not a deadline for query-plan acquisition plus complete multi-page
consumption. Applications requiring that broader deadline must also implement application-level
timeout/cancellation. Observe actual results and terminal signals rather than relying only on a
diagnostics success status. Cross-partition refers to logical partitions; multiple physical partitions
are not required for this example.

## Opt-in QueryPlan fault-injection test

[QueryPlanAvailabilityIT.java](src/test/java/com/azure/spring/data/cosmos/samples/highavailability/QueryPlanAvailabilityIT.java)
starts the real sample configuration without running the command-line application. It runs four cases:
connection and response delays against QueryPlan calls in the **first preferred region only**, through
both synchronous and reactive repositories. The other region receives only a zero-delay observation rule
that permits the actual service request.

The test seeds users in two logical partitions, warms routing metadata, and verifies fresh
`QueryPlan-Document` fault hits in the delayed and healthy regions. Each query must return exactly the
expected users within the recovery budget (the SDK timeout plus a five-second test allowance).
Query diagnostics must identify the SDK version selected by the profile. A fresh client is used per
case; faults are disabled before deleting only test-owned users.

Connection delays are transport-scoped in the fault-injection library. The test therefore fails if the
rule also hits unrelated gateway operations, rather than silently accepting a different failure scenario.
These cases prove recovery for the injected QueryPlan failures; they do not prove that PPCB opened
(QueryPlan is outside PPCB's scope), or distinguish hedging from ordinary retry-based recovery.

The test is **not run by default** and needs the pre-created resources and authentication described above,
plus **exactly two distinct account regions** in `PREFERRED_LOCATIONS`. It fails rather than skips when
these live-test prerequisites are missing. Fault injection is test-scoped and affects this test client
only; no account failover or service outage is performed.

From the sample directory:

```powershell
# Select SDK 4.83.0 for the sample and run unit tests plus all four live fault cases.
mvn verify -P live-tests

# Compare with the BOM-selected SDK used by this sample at the time of writing.
mvn verify -P live-tests "-Dcosmos.test.sdk-version=4.81.0"
```

The `live-tests` profile defaults to `cosmos.test.sdk-version=4.83.0` and uses Failsafe for `*IT` tests.
The profile explicitly overrides the sample's SDK dependency; without it, the BOM continues to select
the normal version, and `mvn clean verify` runs only the credential-free unit tests. Verify the resolved
version after a BOM upgrade before choosing the baseline comparison value.
The `azure-cosmos-test` dependency is test-scoped and excluded from the runnable application.
All four live cases were validated with SDK 4.83.0 and the 4.81.0 baseline against a two-region
multi-writer account using key authentication. The eight credential-free regression tests also passed
with each version. Live Entra ID execution was not validated in that run.

## SDK versions and explicit overrides

`azure-spring-data-cosmos` and the underlying `azure-cosmos` Java SDK have separate versions.
This sample uses the Spring Cloud Azure BOM to select them together. Prefer upgrading the supported
Spring Cloud Azure/Spring Boot combination rather than independently changing the underlying SDK:

- [Spring Cloud Azure dependency management](https://learn.microsoft.com/azure/developer/java/spring-framework/developer-guide-overview#setting-up-dependencies)
- [Spring version mapping](https://github.com/Azure/azure-sdk-for-java/wiki/Spring-Versions-Mapping)
- [Azure Java dependency-conflict guidance](https://learn.microsoft.com/azure/developer/java/sdk/troubleshooting-dependency-version-conflict#mitigate-version-mismatch-issues)

From this sample's directory, inspect the resolved versions before making changes:

```powershell
mvn dependency:tree "-Dincludes=com.azure:azure-spring-data-cosmos,com.azure:azure-cosmos"
```

For a targeted SDK upgrade or reproduction outside the `live-tests` profile, an explicit dependency
can override the BOM-managed transitive SDK version. To use Java SDK 4.83.0 in a normal application build,
add this entry to the existing `<dependencies>` section of the sample POM:

```xml
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-cosmos</artifactId>
    <version>4.83.0</version>
</dependency>
```

**This is a tested override example, not a compatibility guarantee for arbitrary version combinations.**
The sample's default BOM remains unchanged. The 4.83.0 override was tested with Spring Boot 4.1.1,
Spring Cloud Azure BOM 7.4.0, and Spring Data Cosmos 7.4.0, on JDK 21 with a Java 17 compilation target.
All eight regression tests passed, and the executable JAR was checked to contain only SDK 4.83.0.
Synchronous and reactive queries each returned the two expected users against a live two-region account
using key authentication, with hedging and PPCB configured together. The opt-in tests above additionally
exercise first-region QueryPlan connection and response delays. This is coverage of those specific
failure scenarios, not every outage mode or live Entra ID validation.
Public guidance recommends tested BOM-managed dependencies and cautions against SDK downgrades.
Sharing the same SDK major version alone does not establish that the full dependency graph is compatible.

If an override is required for an application:

1. Record its Spring Boot, Spring Cloud Azure/BOM, Spring Data Cosmos, and Java SDK versions.
2. Re-run `mvn dependency:tree` and check for conflicting Azure Core, Reactor, Netty, and Jackson versions.
3. Run `mvn clean verify` and exercise the sample against the intended account. Also run the application's
   integration/resilience tests, including the query-plan behavior relevant to the incident.
4. Confirm support for the exact combination before recommending it for production; a successful build
   or healthy query alone does not prove outage behavior or establish support.

Remove the explicit dependency after a reproduction to return to the BOM-managed version.

## Resources

- [Microsoft Entra ID data-plane RBAC](https://learn.microsoft.com/azure/cosmos-db/how-to-connect-role-based-access-control)
- [Azure Identity and DefaultAzureCredential](https://learn.microsoft.com/java/api/overview/azure/identity-readme)
- [Partition-level circuit breaker](https://learn.microsoft.com/azure/cosmos-db/performance-tips-java-sdk-v4#partition-level-circuit-breaker)
- [Threshold-based availability strategy](https://learn.microsoft.com/azure/cosmos-db/performance-tips-java-sdk-v4#threshold-based-availability-strategy)
- [Azure Spring Data Cosmos](https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring/azure-spring-data-cosmos)
