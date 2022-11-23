// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.sample.ebookstore.cosmos;

import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.CosmosFactory;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.CosmosTemplate;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.azure.spring.data.cosmos.core.convert.MappingCosmosConverter;
import com.azure.spring.data.cosmos.core.mapping.EnableCosmosAuditing;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.IsNewAwareAuditingHandler;
import org.springframework.lang.Nullable;

@Configuration
@EnableCosmosAuditing
public class StoreCosmosConfiguration extends AbstractCosmosConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(StoreCosmosConfiguration.class);

    @Autowired(required = false)
    private IsNewAwareAuditingHandler cosmosAuditingHandler;
    @Autowired
    @Qualifier("bookStoreConnectionProperties")
    CosmosProperties bookStoreConnectionProperties;

    @Override
    protected String getDatabaseName() {
        return bookStoreConnectionProperties.getStoreDatabase();
    }


    //1. Read Configuration
    @ConfigurationProperties(prefix = "azure.cosmos.bookstore")
    @Bean("bookStoreConnectionProperties")
    public CosmosProperties bookStoreDataSourceConfiguration() {
        return new CosmosProperties();
    }

    //2. Create Cosmos client builder
    @Bean
    public CosmosClientBuilder bookStoreCosmosClientBuilder() {
        return new CosmosClientBuilder()
                .key(bookStoreConnectionProperties.getKey())
                .endpoint(bookStoreConnectionProperties.getUri());
    }

    //3. Create Cosmos Client
    @Bean("bookStoreCosmosAsyncClient")
    public CosmosAsyncClient getCosmosAsyncClient(CosmosClientBuilder bookStoreCosmosClientBuilder) {
        return CosmosFactory.createCosmosAsyncClient(bookStoreCosmosClientBuilder);
    }

    //4. Build cosmos config
    @Bean("bookStoreCosmosConfig")
    public CosmosConfig getCosmosConfig() {
        return CosmosConfig.builder()
                           .enableQueryMetrics(true)
                           .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation())
                           .build();
    }

    //5. Create cosmos template for Store Database
    @EnableCosmosRepositories(basePackages = {"com.azure.spring.data.cosmos.sample.ebookstore.model.book","com.azure.spring.data.cosmos.sample.ebookstore.model.cart", "com.azure.spring.data.cosmos.sample.ebookstore.model.order"})
    public class StoreDataBaseConfiguration {
    }

    //6. Create cosmos template for Security Database
    @EnableCosmosRepositories(basePackages = {"com.azure.spring.data.cosmos.sample.ebookstore.model.customer"}, cosmosTemplateRef = "securityDatabaseCosmosTemplate")
    public class SecurityDataBaseConfiguration {
        @Bean
        public CosmosTemplate securityDatabaseCosmosTemplate(@Qualifier("bookStoreConnectionProperties") CosmosProperties bookStoreConnectionProperties, @Qualifier("bookStoreCosmosAsyncClient") CosmosAsyncClient client, @Qualifier("bookStoreCosmosConfig") CosmosConfig cosmosConfig, MappingCosmosConverter mappingCosmosConverter) {
            return new CosmosTemplate(client, bookStoreConnectionProperties.getSecurityDatabase(), cosmosConfig, mappingCosmosConverter, cosmosAuditingHandler);
        }
    }


    private class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            logger.info("Response Diagnostics {}", responseDiagnostics);
        }
    }
}
