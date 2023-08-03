// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.data.cosmos.example.tenant;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.models.ThroughputProperties;
import com.azure.spring.data.cosmos.CosmosFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * Extend CosmosFactory to allow mutli-tenancy at the database level
 */
public class MultiTenantDBCosmosFactory extends CosmosFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiTenantDBCosmosFactory.class);
    private CosmosAsyncClient client;
    private Environment env;
    @Autowired
    private TenantStorage tenantStorage;
    public String tenantId;

    /**
     * Validate config and initialization
     *
     * @param cosmosAsyncClient cosmosAsyncClient
     * @param databaseName databaseName
     * @param env env
     */
    public MultiTenantDBCosmosFactory(CosmosAsyncClient cosmosAsyncClient, String databaseName, Environment env) {
        super(cosmosAsyncClient, databaseName);
        this.client = cosmosAsyncClient;
        this.env = env;
        this.tenantId = databaseName;
        LOGGER.info("************tenant id is: "+ this.tenantId);
    }

    @Override
    public String getDatabaseName() {
        String tenantId = TenantStorage.getCurrentTenant();
        if (tenantId !=null && !tenantId.equals(env.getProperty("spring.data.cosmos.databaseName"))){
            this.tenantId = tenantId;
            //first check if the tenant exists in a thread-safe list of tenant ids
            //if it exists, no further action taken.
            //If not, create the tenant database resources on the fly, using the default database as a model
            tenantStorage.createTenantSpecificDatabaseIfNotExists(tenantId);
            return tenantId;
        }
        else {
            return this.tenantId;
        }
    }
}
