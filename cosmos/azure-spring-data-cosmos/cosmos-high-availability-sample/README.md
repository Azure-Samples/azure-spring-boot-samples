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

This Spring Boot 4 sample enables **threshold-based availability (hedging)** and the **per-partition
circuit breaker (PPCB)** on one Cosmos client. It saves users with different `lastName` partition keys,
runs synchronous and reactive cross-partition queries by `firstName`, verifies the results, and deletes
only its own users.

## Set up and run

### Prerequisites

- JDK 17 or later and Maven.
- An Azure Cosmos DB for NoSQL account, its global endpoint, and Direct-mode/HTTPS connectivity.
- **Session-or-stronger account consistency**: Session, Bounded Staleness, or Strong. Immediate queries
  can miss recent writes on Eventual or Consistent Prefix accounts; a client setting cannot upgrade
  weaker account consistency.
- Multiple readable regions for regional read recovery; multiple writable regions for write-side PPCB.
  The client does not change the account's regional configuration.
- Azure CLI for resource provisioning and local Microsoft Entra ID (AAD) authentication.

### 1. Create the database and container

Provision the resources once with an administrative identity. The application does not create them:

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

For a serverless account, omit `--throughput`. An administrator can perform this management-plane step
separately from running the application.

### 2. Configure the account and authentication

Set these environment variables, or put the same names in `cosmos-v4.properties` in your user home
directory, outside the checkout. Environment variables override the imported file.

```properties
ACCOUNT_HOST=https://<account-name>.documents.azure.com:443/
COSMOS_DATABASE=cosmos-high-availability-sample
PREFERRED_LOCATIONS=East US,West US
```

Replace the account and region names with your own. Preferred regions control routing order; they are
optional for the application, but the live test below requires exactly two distinct regions.

#### Microsoft Entra ID (recommended)

With no account key configured, the application uses `DefaultAzureCredential`, which can use the
Azure CLI login from step 1. Assign the application's identity the **Cosmos DB Built-in Data Contributor**
data-plane role on the sample container:

```powershell
$principalId = az ad signed-in-user show --query id -o tsv
az cosmosdb sql role assignment create --resource-group $resourceGroup --account-name $accountName `
    --principal-id $principalId `
    --role-definition-id "$accountId/sqlRoleDefinitions/00000000-0000-0000-0000-000000000002" `
    --scope "$accountId/dbs/$databaseName/colls/highAvailabilityUsers"
```

Role assignment requires management-plane permissions and can take time to propagate. Azure
management-plane Contributor alone does not grant item/query access. For a service principal or managed
identity, use that identity's object ID instead of the developer's. `DefaultAzureCredential` supports both.

Remove `ACCOUNT_KEY` from both the environment and imported file: a nonempty key takes precedence.
The pre-created container and `autoCreateContainer=false` avoid resource-creation calls that this
data-plane role does not authorize.

#### Account key (optional)

If key authentication is enabled on the account, add this value to the external file or environment:

```properties
ACCOUNT_KEY=<account-key>
```

Never commit credentials or pass keys in command-line arguments.

### 3. Build and run

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

`mvn clean verify` runs only credential-free unit tests. The application waits for both query results,
then closes its Spring context and Cosmos client. See [cleanup](#cleanup) for resource ownership.

## Configure availability

Both repositories share the client configured in
[SampleAppConfiguration.java](src/main/java/com/azure/spring/data/cosmos/samples/highavailability/SampleAppConfiguration.java).
Override its defaults with environment variables or the external properties file:

| Setting | Variable | Default |
| --- | --- | --- |
| SDK query-page timeout | `COSMOS_END_TO_END_TIMEOUT` | `PT10S` |
| Initial hedging threshold | `COSMOS_AVAILABILITY_THRESHOLD` | `PT5S` |
| Subsequent hedging step | `COSMOS_AVAILABILITY_THRESHOLD_STEP` | `PT0.1S` |
| PPCB read failure count | `COSMOS_PPCB_READ_FAILURE_THRESHOLD` | `10` |
| PPCB write failure count | `COSMOS_PPCB_WRITE_FAILURE_THRESHOLD` | `5` |

Durations use ISO-8601 syntax. The timeout must exceed the initial threshold; PPCB counts must be at
least 10 for reads and 5 for writes.

The timeout and hedging portion of the builder, using these defaults:

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

Return this builder from the Spring `@Bean` rather than building another client. The full configuration
also sets preferred regions, selects authentication, and enables PPCB before client construction.

| Mechanism | Behavior |
| --- | --- |
| Hedging | Starts parallel requests in alternate eligible regions after a threshold; can add RU/network cost. |
| PPCB | Tracks terminal failures for a physical partition in a region and avoids unhealthy locations on subsequent requests; initial requests can still fail. |

PPCB's `COSMOS.PARTITION_LEVEL_CIRCUIT_BREAKER_CONFIG` setting is **process-wide**: configure it before
creating any clients, not per request. To experiment without explicit PPCB, omit
`enablePartitionLevelCircuitBreaker()`; to omit the explicit hedging/timeout policy, remove
`endToEndOperationLatencyPolicyConfig(...)`. Other SDK defaults can still apply.

## Test regional Gateway recovery

The opt-in [GatewayRecoveryIT.java](src/test/java/com/azure/spring/data/cosmos/samples/highavailability/GatewayRecoveryIT.java)
establishes healthy queries, then simulates a Gateway outage during steady-state operation.
It uses the real sample configuration with a fresh client per case:

| Injected fault | Target | Repository APIs |
| --- | --- | --- |
| Connection delay | All Gateway requests to the first preferred region | Synchronous and reactive |
| Response delay | All Gateway requests to the first preferred region | Synchronous and reactive |

There is no operation-type filter: QueryPlan, metadata, or other Gateway traffic to that region can be
affected. Direct TCP data requests are not faulted. This models an outage of the regional Gateway path,
not loss of the entire region or recovery during cold client startup.

After completing setup, set `PREFERRED_LOCATIONS` to **exactly two distinct account regions** and run:

```powershell
mvn verify -P live-tests
```

Failsafe runs these tests only with `live-tests`. Missing live prerequisites cause failures, not skips.

Assertions require Gateway fault hits and traffic to the healthy second region, the expected user IDs
and completion within the SDK timeout plus a five-second test allowance, and diagnostics identifying
the runtime SDK. The query's `CosmosDiagnosticsContext` must also record both preferred regions as
contacted. The test logs these regions and the observed Gateway operation types to show the recovery
path. Faults affect only the test client and are disabled before cleanup.
The test library is test-scoped and excluded from the runnable application.

These cases demonstrate the specified recovery scenarios, not every outage mode or PPCB opening.
They do not distinguish hedging from ordinary retry-based recovery. Live Entra ID execution has not
been validated; configuration/authentication selection is covered by unit tests.

## Cleanup

The application and live tests delete only their uniquely named users, tolerate missing items, and
preserve original failures if cleanup also fails. Both deletes are attempted even after an ambiguous
write. The application closes the client, but the pre-created database/container remain and can incur
charges. Remove only dedicated sample resources, never shared data.

## Resources

- [Microsoft Entra ID data-plane RBAC](https://learn.microsoft.com/azure/cosmos-db/how-to-connect-role-based-access-control)
- [Azure Identity and DefaultAzureCredential](https://learn.microsoft.com/java/api/overview/azure/identity-readme)
- [Partition-level circuit breaker](https://learn.microsoft.com/azure/cosmos-db/performance-tips-java-sdk-v4#partition-level-circuit-breaker)
- [Threshold-based availability strategy](https://learn.microsoft.com/azure/cosmos-db/performance-tips-java-sdk-v4#threshold-based-availability-strategy)
