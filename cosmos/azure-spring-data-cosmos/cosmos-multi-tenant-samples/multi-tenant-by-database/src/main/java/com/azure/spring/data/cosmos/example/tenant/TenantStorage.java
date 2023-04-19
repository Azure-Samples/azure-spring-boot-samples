// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example.tenant;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosDatabaseProperties;
import com.azure.cosmos.models.ThroughputProperties;
import com.azure.cosmos.util.CosmosPagedFlux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantStorage implements CommandLineRunner {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(TenantStorage.class);
    private Environment env;
    private ApplicationContext applicationContext;
    private CosmosAsyncClient client;
    Set<String> databaseIds = ConcurrentHashMap.newKeySet();
    CosmosPagedFlux<CosmosDatabaseProperties> tenantDatabases;
    public TenantStorage(Environment env,ApplicationContext applicationContext){
        this.env = env;
        this.applicationContext = applicationContext;

        //access the existing CosmosAsyncClient from the bean already created by Cosmos Spring Data Client Library
        client = applicationContext.getBean(CosmosAsyncClient.class);
    }
    public static void clear() {
        currentTenant.remove();
    }

    public void createTenantSpecificDatabaseIfNotExists(String tenantId){
        //create database and containers for the tenant based on the default database
        Boolean tenantExists = databaseIds.contains(tenantId);
        if(!tenantExists){
            this.client.createDatabaseIfNotExists(tenantId, ThroughputProperties.createAutoscaledThroughput(1000)).block();
            CosmosPagedFlux<CosmosContainerProperties> containers = this.client.getDatabase(env.getProperty("spring.data.cosmos.databaseName")).readAllContainers();
            containers.byPage(100).flatMap(readAllContainersResponse -> {
                for (CosmosContainerProperties response : readAllContainersResponse.getResults()) {
                    CosmosContainerProperties containerProperties =
                            new CosmosContainerProperties(response.getId(), response.getPartitionKeyDefinition());
                    this.client.getDatabase(tenantId).createContainerIfNotExists(containerProperties).block();
                }
                return Flux.empty();
            }).blockLast();
            databaseIds.add(tenantId);
        }
    }

    @Override
    public void run(String...args) throws Exception {
        tenantDatabases = client.readAllDatabases();
        String msg="Listing databases in the cosmos db account:\n";
        tenantDatabases.byPage(100).flatMap(readAllDatabasesResponse -> {
            LOGGER.info("read {} databases(s) with request charge of {}", readAllDatabasesResponse.getResults().size(),readAllDatabasesResponse.getRequestCharge());
            for (CosmosDatabaseProperties response : readAllDatabasesResponse.getResults()) {
                String tenantId = response.getId();
                //adding tenants to the list at startup (no need to add the default database)
                if(!tenantId.equals(env.getProperty("spring.data.cosmos.databaseName"))){
                    LOGGER.info("database tenant id: {}", tenantId);
                    LOGGER.info("adding {} to tenant database list", tenantId);
                    databaseIds.add(tenantId);
                }
            }
            return Flux.empty();
        }).blockLast();
    }
    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }
    public static String getCurrentTenant() {
        return currentTenant.get();
    }
}