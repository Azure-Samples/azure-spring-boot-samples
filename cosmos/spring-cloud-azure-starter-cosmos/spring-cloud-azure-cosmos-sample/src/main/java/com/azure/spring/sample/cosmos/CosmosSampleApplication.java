// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.sample.cosmos;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;
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

    private final String databaseName = "products";
    private final String containerName = "users";
    private final String documentId = UUID.randomUUID().toString();
    private final String documentLastName = "Peterson";

    private CosmosDatabase database;
    private CosmosContainer container;


    public static void main(String[] args) {
        SpringApplication.run(CosmosSampleApplication.class, args);
    }

    public void run(String... var1) throws Exception {
        getDatabase();
        getContainer();
        createDocument();
        queryAllDocuments();
    }

    /**
     * Create Database
     */
    private void getDatabase() throws Exception {
        logger.info("Get database " + databaseName + " .........");

        //  Get database
        database = client.getDatabase(databaseName);

        logger.info("Exec getDatabase() is Done.");
    }

    /**
     * Create container
     */
    private void getContainer() throws Exception {
        logger.info("Get container " + containerName + " .........");

        //  Get container
        container = database.getContainer(containerName);

        logger.info("Exec getContainer() is Done.");
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
        container.createItem(family,new PartitionKey(family.getId()),new CosmosItemRequestOptions());

        logger.info("Exec createDocument() is Done.");
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

        logger.info("Exec executeQueryPrintSingleResult() is Done.");
    }
}
