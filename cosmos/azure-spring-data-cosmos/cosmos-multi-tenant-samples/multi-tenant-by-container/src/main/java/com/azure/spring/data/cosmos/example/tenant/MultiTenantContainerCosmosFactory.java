// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example.tenant;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.spring.data.cosmos.CosmosFactory;
import com.azure.spring.data.cosmos.example.CosmosProperties;
import org.springframework.beans.factory.annotation.Autowired;

public class MultiTenantContainerCosmosFactory extends CosmosFactory {
    @Autowired
    private TenantStorage tenantStorage;

    private CosmosProperties properties;
    public String tenantId;

    public MultiTenantContainerCosmosFactory(CosmosAsyncClient cosmosAsyncClient, String databaseName, CosmosProperties properties) {
        super(cosmosAsyncClient, databaseName);
        this.properties = properties;
    }

    @Override
    public String overrideContainerName() {
        String tenantId = TenantStorage.getCurrentTenant();
        if (tenantId !=null){
            this.tenantId = tenantId;
            tenantStorage.createTenantSpecificContainerIfNotExists(tenantId, properties.getPartitionKeyPath());
            return tenantId;
        }
        else {
            return "default";
        }
    }
}
