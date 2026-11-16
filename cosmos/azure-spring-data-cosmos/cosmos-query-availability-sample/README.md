---
page_type: sample
languages:
- java
products:
- azure-cosmos-db
name: Spring Data Cosmos cross-partition query timeout and availability
description: Configure timeout and availability policies for ordinary synchronous and reactive Spring Data Cosmos queries.
---

# Spring Data Cosmos query timeout and availability

This Spring Boot 4 command-line sample configures a Cosmos DB end-to-end operation latency policy and
threshold-based availability strategy, then executes ordinary Spring Data repository queries across
two logical partition keys. There is no fault-injection dependency or test framework in the application.

## What to read

- [SampleConfiguration.java](src/main/java/com/azure/spring/data/cosmos/samples/availability/SampleConfiguration.java):
  set the timeout, availability thresholds, preferred regions, and credentials on the client builder.
- [SampleRepository.java](src/main/java/com/azure/spring/data/cosmos/samples/availability/SampleRepository.java)
  and [ReactiveSampleRepository.java](src/main/java/com/azure/spring/data/cosmos/samples/availability/ReactiveSampleRepository.java):
  declare the same cross-partition query for both repository APIs.
- [SampleApplication.java](src/main/java/com/azure/spring/data/cosmos/samples/availability/SampleApplication.java):
  save two uniquely named items in different logical partitions, consume both queries completely, verify
  their results, and delete only those two items.

The query deliberately does not specify a partition key:

```java
@Query("SELECT * FROM c WHERE STARTSWITH(c.id, @prefix)")
List<SampleItem> findByIdPrefix(@Param("prefix") String prefix);
```

The prefix isolates this run's items, but does not restrict routing to one partition. Multiple physical
partitions are not required to run the example.

## Prerequisites

- JDK 17 or later and Maven.
- An Azure Cosmos DB for NoSQL account and network access to its global account endpoint in Direct mode.
  Query-plan and other metadata requests still need HTTPS gateway connectivity.
- At least two readable account regions to benefit from cross-region hedging. A single-region account
  or a trusted local emulator can run the query, but cannot demonstrate regional redundancy.

### Create the sample database and container

The sample does not create or delete databases or containers. Create a database named
`query-availability-sample` and a container named `query-availability-items` with partition key path
`/partitionKey`, using the Azure portal or Azure CLI:

```powershell
az cosmosdb sql database create --resource-group "<resource-group>" `
    --account-name "<account-name>" --name query-availability-sample
az cosmosdb sql container create --resource-group "<resource-group>" `
    --account-name "<account-name>" --database-name query-availability-sample `
    --name query-availability-items --partition-key-path /partitionKey --throughput 400
```

Choose throughput appropriate for your account; for a serverless account, omit `--throughput`.
Provisioned resources can incur charges after this application exits. Do not delete shared resources
when cleaning up the sample.

### Configure authentication and regions

By default, the application uses `DefaultAzureCredential`. Sign in with `az login` for local development
and grant the identity the Cosmos DB Built-in Data Contributor data-plane role on the existing resources.
An Azure management-plane Contributor assignment alone does not grant item/query access. See
[Cosmos DB data-plane RBAC](https://learn.microsoft.com/azure/cosmos-db/how-to-connect-role-based-access-control).

Set the endpoint and the names of your account's read regions:

```powershell
$env:ACCOUNT_HOST = "https://<account-name>.documents.azure.com:443/"
$env:PREFERRED_LOCATIONS = "East US,West US"
```

An existing key-authenticated development setup is also supported. The application optionally imports
`cosmos-v4.properties` from your user home directory, outside the checkout:

```properties
ACCOUNT_HOST=https://<account-name>.documents.azure.com:443/
ACCOUNT_KEY=<account-key>
PREFERRED_LOCATIONS=East US,West US
```

If `ACCOUNT_KEY` is present, the sample uses it instead of `DefaultAzureCredential`; leave it unset for
passwordless authentication. Do not commit credentials or paste them into command-line arguments.
Spring Boot command-line properties and environment variables take precedence over the imported file.
To import an existing file elsewhere, pass
`--spring.config.import=optional:file:C:/path/to/external-repository/cosmos-v4.properties`
as an application argument.

## Build and run

From `cosmos/azure-spring-data-cosmos/cosmos-query-availability-sample`:

```powershell
mvn clean verify
mvn spring-boot:run
```

The unit tests use mocked repositories and require no Azure resources. The run command uses the account
configured above and should print:

```text
Synchronous cross-partition query returned 2 items.
Reactive cross-partition query completed with 2 items.
```

The application verifies the returned item IDs, waits for reactive completion, deletes its own items in
`finally`, and closes the Spring context and Cosmos client. Failures are not converted into successful
empty results. The database and container remain for you to reuse or remove.

### Change the policy

| Spring property | Environment variable | Default |
| --- | --- | --- |
| `cosmos.database` | `COSMOS_DATABASE` | `query-availability-sample` |
| `cosmos.preferred-regions` | `PREFERRED_LOCATIONS` | No explicit order |
| `cosmos.end-to-end-timeout` | `COSMOS_END_TO_END_TIMEOUT` | `PT5S` |
| `cosmos.availability.threshold` | `COSMOS_AVAILABILITY_THRESHOLD` | `PT0.5S` |
| `cosmos.availability.threshold-step` | `COSMOS_AVAILABILITY_THRESHOLD_STEP` | `PT0.1S` |

Durations use ISO-8601 syntax. The timeout must exceed the initial availability threshold.
For example:

```powershell
mvn spring-boot:run "-Dspring-boot.run.arguments=--cosmos.end-to-end-timeout=PT3S --cosmos.availability.threshold=PT0.5S"
```

These are sample-defined properties wired into the Java SDK client builder, not automatically recognized
Spring Cloud Azure availability settings. The same client settings apply to both repositories.
Explicit preferred regions make routing order predictable; they are not a general requirement for hedging
if endpoint discovery supplies multiple eligible regions.

## Understand the guarantees

- The availability strategy can start requests in other eligible regions after the configured threshold.
  It is distinct from ordinary retries and can add RU/network cost. A healthy run does not prove that
  hedging occurred.
- A regional outage can affect gateway query-plan retrieval even when data requests use Direct mode.
  Cross-partition queries make this dependency relevant; enabling Direct mode does not remove it.
- The SDK query-page timeout is not a deadline for query-plan acquisition plus complete multi-page
  consumption. Applications needing a broader deadline must also implement application-level
  timeout/cancellation and handle it explicitly.
- Successful SDK diagnostics alone do not prove that a reactive query delivered its expected results;
  observe application completion, errors/cancellation, and returned items too.

For fault-injection recovery tests, keep fault setup and assertions in a separate integration-test
fixture rather than adding them to customer application code.

## Resources

- [Threshold-based availability strategy](https://learn.microsoft.com/azure/cosmos-db/performance-tips-java-sdk-v4#threshold-based-availability-strategy)
- [Azure Spring Data Cosmos](https://github.com/Azure/azure-sdk-for-java/tree/main/sdk/spring/azure-spring-data-cosmos)
- [Azure Identity authentication](https://learn.microsoft.com/java/api/overview/azure/identity-readme)
