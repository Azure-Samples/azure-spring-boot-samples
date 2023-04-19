// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example.tenant;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import com.azure.cosmos.models.ThroughputProperties;
import com.azure.cosmos.util.CosmosPagedFlux;
import com.azure.spring.data.cosmos.example.CosmosProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantStorage implements CommandLineRunner {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    private static final ThreadLocal<String> currentTenantTier = new ThreadLocal<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantStorage.class);
    private CosmosProperties properties;
    private ApplicationContext applicationContext;
    public CosmosAsyncClient client;
    private CosmosAsyncDatabase database;
    Set<String> containerIds = ConcurrentHashMap.newKeySet();
    CosmosPagedFlux<CosmosContainerProperties> containers;

    public TenantStorage(CosmosProperties properties,ApplicationContext applicationContext){
        this.properties = properties;
        this.applicationContext = applicationContext;

        //access the existing CosmosAsyncClient from the bean already created by Cosmos Spring Data Client Library
        client = applicationContext.getBean(CosmosAsyncClient.class);

        //get tenants database, and create it if it does not already exist
        CosmosDatabaseResponse databaseResponse = client.createDatabaseIfNotExists(properties.getDatabaseName(), ThroughputProperties.createAutoscaledThroughput(4000)).block();
        database = client.getDatabase(databaseResponse.getProperties().getId());
    }

    public static void clear() {
        currentTenant.remove();
        currentTenantTier.remove();
    }

    @Override
    public void run(String...args) throws Exception {
        containers = database.readAllContainers();
        containers.byPage(100).flatMap(response -> {
            LOGGER.info("Read {} containers(s) with request charge of {}", response.getResults().size(),response.getRequestCharge());
            for (CosmosContainerProperties properties : response.getResults()) {
                String tenantId = properties.getId();
                LOGGER.info("adding {} to tenant list", tenantId);
                containerIds.add(tenantId);
            }
            return Flux.empty();
        }).blockLast();
    }

    /**
     * Create tenant specific container if not exists.
     * @param tenantId The tenant id to check whether tenant specific container exists.
     */
    public void createTenantSpecificContainerIfNotExists(String tenantId, String partitionKeyPath) {
        if(!containerIds.contains(tenantId)){
            createTieredTenantContainerIfNotExist(tenantId, getCurrentTenantTier(), partitionKeyPath);
        }
    }

    /**
     * Create container using CosmosAsyncClient, if it does not already exist in tenant list,
     * and set dedicated throughput if premium tier
     */
    private void createTieredTenantContainerIfNotExist(String tenantId, String tier, String partitionKeyPath){
        CosmosContainerProperties containerProperties = new CosmosContainerProperties(tenantId, partitionKeyPath);
        if (tier.equals("premium")) {
            database.createContainerIfNotExists(containerProperties, ThroughputProperties.createManualThroughput(4000)).block();
        } else {
            database.createContainerIfNotExists(containerProperties).block();
        }
        containerIds.add(tenantId);
    }

    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }

    public static void setCurrentTenantTier(String tenantTier) {
        currentTenantTier.set(tenantTier);
    }
    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static String getCurrentTenantTier() {
        return currentTenantTier.get();
    }
}