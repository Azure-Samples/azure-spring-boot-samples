package com.azure.spring.data.cosmos.example.tenant;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncDatabase;
import com.azure.cosmos.models.CosmosContainerProperties;
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
    private static ThreadLocal<String> currentTenantTier = new ThreadLocal<>();
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
    public static void clear() {
        currentTenant.remove();
    }
    private static final Logger logger = LoggerFactory.getLogger(TenantStorage.class);
    private Environment env;
    private ApplicationContext applicationContext;
    public CosmosAsyncClient client;
    private CosmosAsyncDatabase database;
    ConcurrentLinkedQueue<String> tenantList = new ConcurrentLinkedQueue<String>();
    CosmosPagedFlux<CosmosContainerProperties> containers;
    public TenantStorage(Environment env,ApplicationContext applicationContext){
        this.env = env;
        this.applicationContext = applicationContext;

        //access the existing CosmosAsyncClient from the bean already created by Cosmos Spring Data Client Library
        client = applicationContext.getBean(CosmosAsyncClient.class);

        //get tenants database, and create it if it does not already exist
        CosmosDatabaseResponse databaseResponse = client.createDatabaseIfNotExists(env.getProperty("spring.data.cosmos.databaseName"), ThroughputProperties.createAutoscaledThroughput(4000)).block();
        database = client.getDatabase(databaseResponse.getProperties().getId());
    }
    public String getTenant(String tenantId){
        //create container using CosmosAsyncClient, if it does not already exist in tenant list, and set dedicated throughput if premium tier
        Boolean tenant = tenantList.contains(tenantId);
        if(!tenant){
            createTieredTenantContainer(tenantId, getCurrentTenantTier());
        }
        return tenantId;
    }

    public void createTieredTenantContainer(String tenantId, String tier){
        CosmosContainerProperties containerProperties = new CosmosContainerProperties(tenantId, "/lastName");
        //create container using CosmosAsyncClient, if it does not already exist in tenant list, and set dedicated throughput if premium tier
        if (tier.equals("premium")) {
            client.getDatabase(database.getId()).createContainerIfNotExists(containerProperties, ThroughputProperties.createManualThroughput(4000)).block();
        } else {
            client.getDatabase(database.getId()).createContainerIfNotExists(containerProperties).block();
        }
        client.getDatabase(database.getId()).createContainerIfNotExists(containerProperties).block();
        tenantList.add(tenantId);

    }

    @Override
    public void run(String...args) throws Exception {
        containers = database.readAllContainers();
        String msg="Listing containers in tenants database:\n";
        containers.byPage(100).flatMap(readAllContainersResponse -> {
            logger.info("read {} containers(s) with request charge of {}", readAllContainersResponse.getResults().size(),readAllContainersResponse.getRequestCharge());
            for (CosmosContainerProperties response : readAllContainersResponse.getResults()) {
                String tenantId = response.getId();
                logger.info("container tenant id: {}", tenantId);
                logger.info("adding {} to tenant list", tenantId);
                tenantList.add(tenantId);
            }
            return Flux.empty();
        }).blockLast();
    }
}