package com.azure.spring.data.cosmos.example.tenant;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosDatabaseProperties;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import com.azure.cosmos.models.ThroughputProperties;
import com.azure.cosmos.util.CosmosPagedFlux;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@PropertySource("classpath:application.yaml")
public class TenantStorage implements CommandLineRunner {
    private static ThreadLocal<String> currentTenant = new ThreadLocal<>();
    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }
    public static String getCurrentTenant() {
        return currentTenant.get();
    }
    public static void clear() {
        currentTenant.remove();
    }
    private static final Logger logger = LoggerFactory.getLogger(TenantStorage.class);
    private Environment env;
    private ApplicationContext applicationContext;
    private CosmosAsyncClient client;
    ConcurrentLinkedQueue<String> tenantList = new ConcurrentLinkedQueue<String>();
    CosmosPagedFlux<CosmosDatabaseProperties> tenantDatabases;
    public TenantStorage(Environment env,ApplicationContext applicationContext){
        this.env = env;
        this.applicationContext = applicationContext;

        //access the existing CosmosAsyncClient from the bean already created by Cosmos Spring Data Client Library
        client = applicationContext.getBean(CosmosAsyncClient.class);
    }
    public String getTenant(String tenantId){
        //create database and containers for the tenant based on the default database
        Boolean tenant = tenantList.contains(tenantId);
        if(!tenant){
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
            tenantList.add(tenantId);
        }
        return tenantId;
    }

    @Override
    public void run(String...args) throws Exception {
        tenantDatabases = client.readAllDatabases();
        String msg="Listing databases in the cosmos db account:\n";
        tenantDatabases.byPage(100).flatMap(readAllDatabasesResponse -> {
            logger.info("read {} databases(s) with request charge of {}", readAllDatabasesResponse.getResults().size(),readAllDatabasesResponse.getRequestCharge());
            for (CosmosDatabaseProperties response : readAllDatabasesResponse.getResults()) {
                String tenantId = response.getId();
                //adding tenants to the list at startup (no need to add the default database)
                if(!tenantId.equals(env.getProperty("spring.data.cosmos.databaseName"))){
                    logger.info("database tenant id: {}", tenantId);
                    logger.info("adding {} to tenant database list", tenantId);
                    tenantList.add(tenantId);
                }
            }
            return Flux.empty();
        }).blockLast();
    }
}