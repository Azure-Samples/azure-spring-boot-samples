// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.spring.cosmos.ebookstore.cosmos;

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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired(required = false)
    private IsNewAwareAuditingHandler cosmosAuditingHandler;
    @Autowired
    @Qualifier("bookStoreConnectionConfiguration")
    CosmosProperties bookStoreConnectionProperties;

    @Override
    protected String getDatabaseName() {
        return bookStoreConnectionProperties.getStoreDatabase();
    }


    //1. Read Configuration
    @ConfigurationProperties(prefix = "azure.cosmos.bookstore")
    @Bean("bookStoreConnectionConfiguration")
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
    @EnableCosmosRepositories(basePackages = {"com.spring.cosmos.ebookstore.model.book","com.spring.cosmos.ebookstore.model.cart", "com.spring.cosmos.ebookstore.model.order"})
    public class StoreDataBaseConfiguration {
    }

    //5. Create cosmos template for Security Database
    @EnableCosmosRepositories(basePackages = {"com.spring.cosmos.ebookstore.model.user"}, cosmosTemplateRef = "securityDatabaseCosmosTemplate")
    public class SecurityDataBaseConfiguration {
        @Bean
        public CosmosTemplate securityDatabaseCosmosTemplate(@Qualifier("bookStoreConnectionConfiguration") CosmosProperties bookStoreConnectionConfiguration, @Qualifier("bookStoreCosmosAsyncClient") CosmosAsyncClient client, @Qualifier("bookStoreCosmosConfig") CosmosConfig cosmosConfig, MappingCosmosConverter mappingCosmosConverter) {
            return new CosmosTemplate(client, bookStoreConnectionConfiguration.getSecurityDatabase(), cosmosConfig, mappingCosmosConverter, cosmosAuditingHandler);
        }
    }


    private class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            logger.info("Response Diagnostics {}", responseDiagnostics);
        }
    }
}
