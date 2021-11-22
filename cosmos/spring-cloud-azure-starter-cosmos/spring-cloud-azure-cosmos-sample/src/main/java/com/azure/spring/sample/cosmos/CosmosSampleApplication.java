// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.sample.cosmos;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import com.azure.cosmos.models.ThroughputProperties;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.azure.spring.sample.cosmos.common.Family;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class CosmosSampleApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CosmosSampleApplication.class);

    @Autowired
    private CosmosClient client;

    private final String databaseName = "AzureCosmosSampleDB";
    private final String containerName = "AzureCosmosSampleContainer";
    private final String documentId = UUID.randomUUID().toString();
    private final String documentLastName = "Peterson";

    private CosmosDatabase database;
    private CosmosContainer container;


    public static void main(String[] args) {
        SpringApplication.run(CosmosSampleApplication.class, args);
    }

    public void run(String... var1) throws Exception {
        createDatabaseIfNotExists();
        createContainerIfNotExists();
        createDocument();
        queryAllDocuments();
    }

    /**
     * Create Database
     */
    private void createDatabaseIfNotExists() throws Exception {
        logger.info("Create database " + databaseName + " if not exists...");

        //  Create database if not exists
        CosmosDatabaseResponse databaseResponse = client.createDatabaseIfNotExists(databaseName);
        database = client.getDatabase(databaseResponse.getProperties().getId());

        logger.info("Done.");
    }

    /**
     * Create container
     */
    private void createContainerIfNotExists() throws Exception {
        logger.info("Create container " + containerName + " if not exists.");

        //  Create container if not exists
        CosmosContainerProperties containerProperties =
                new CosmosContainerProperties(containerName, "/lastName");

        // Provision throughput
        ThroughputProperties throughputProperties = ThroughputProperties.createManualThroughput(400);

        //  Create container with 200 RU/s
        CosmosContainerResponse containerResponse = database.createContainerIfNotExists(containerProperties, throughputProperties);
        container = database.getContainer(containerResponse.getProperties().getId());

        logger.info("Done.");
    }

    /**
     * Create document
     */
    private void createDocument() throws Exception {
        logger.info("Create document " + documentId);

        // Define a document as a POJO (internally this
        // is converted to JSON via custom serialization)
        Family family = new Family();
        family.setLastName(documentLastName);
        family.setId(documentId);

        // Insert this item as a document
        // Explicitly specifying the /pk value improves performance.
        container.createItem(family,new PartitionKey(family.getLastName()),new CosmosItemRequestOptions());

        logger.info("Done.");
    }

    private void queryAllDocuments() throws Exception {
        logger.info("Query all documents.");

        executeQueryPrintSingleResult("SELECT * FROM c");
    }

    private void executeQueryPrintSingleResult(String sql) {
        logger.info("Execute query {}",sql);

        CosmosPagedIterable<Family> filteredFamilies = container.queryItems(sql, new CosmosQueryRequestOptions(), Family.class);

        // Print
        if (filteredFamilies.iterator().hasNext()) {
            Family family = filteredFamilies.iterator().next();
            logger.info(String.format("First query result: Family with (/id, partition key) = (%s,%s)",family.getId(),family.getLastName()));
        }

        logger.info("Done.");
    }
}
