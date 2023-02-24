package com.azure.spring.data.cosmos.example.tenant;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.models.ThroughputProperties;
import com.azure.spring.data.cosmos.CosmosFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class MultiTenantContainerCosmosFactory extends CosmosFactory {

    private static final Logger logger = LoggerFactory.getLogger(MultiTenantContainerCosmosFactory.class);
    private CosmosAsyncClient client;
    private Environment env;
    @Autowired
    private TenantStorage tenantStorage;
    public String tenantId;

    public MultiTenantContainerCosmosFactory(CosmosAsyncClient cosmosAsyncClient, String databaseName, Environment env) {
        super(cosmosAsyncClient, databaseName);
        this.client = cosmosAsyncClient;
        this.env = env;
        this.tenantId = databaseName;
        logger.info("************tenant id is: "+ this.tenantId);
    }

    @Override
    public String overrideContainerName() {
        String tenantId = TenantStorage.getCurrentTenant();
        if (tenantId !=null){
            //the getTenant method will first check if the tenant exists in a thread-safe list of tenant ids
            //if it exists, it returns the id, and no further action taken.
            //If not, it will create the tenant container resource on the fly
            this.tenantId = tenantStorage.getTenant(tenantId);
            return tenantId;
        }
        else {
            return "default";
        }
    }



}
