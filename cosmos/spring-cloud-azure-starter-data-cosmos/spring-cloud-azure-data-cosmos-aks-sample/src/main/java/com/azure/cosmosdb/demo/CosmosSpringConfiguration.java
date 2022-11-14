// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.cosmosdb.demo;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.DirectConnectionConfig;
import com.azure.cosmos.GatewayConnectionConfig;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.config.CosmosConfig;
import com.azure.spring.data.cosmos.core.ResponseDiagnostics;
import com.azure.spring.data.cosmos.core.ResponseDiagnosticsProcessor;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import com.azure.spring.data.cosmos.repository.config.EnableReactiveCosmosRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.lang.Nullable;

@Configuration
@EnableConfigurationProperties(CosmosProperties.class)
@EnableCosmosRepositories
@EnableReactiveCosmosRepositories
@PropertySource("classpath:application.properties")
public class CosmosSpringConfiguration extends AbstractCosmosConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(CosmosSpringConfiguration.class);

    private CosmosProperties properties;

    CosmosSpringConfiguration(CosmosProperties properties){
        this.properties = properties;
    }

    @Bean
    public CosmosClientBuilder cosmosBuildClient() {
        DirectConnectionConfig directConnectionConfig = DirectConnectionConfig.getDefaultConfig();
        
        //use this for gateway connection
        GatewayConnectionConfig gatewayConnectionConfig = GatewayConnectionConfig.getDefaultConfig(); 

        return new CosmosClientBuilder()
            .endpoint(properties.getUri())
            .key(properties.getKey())
            .directMode(directConnectionConfig);
    }

    @Bean
    public CosmosConfig cosmosConfig() {
        return CosmosConfig.builder()
                           .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation(this.properties))
                           .enableQueryMetrics(properties.isQueryMetricsEnabled())
                           .build();
    }

    private static class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        private CosmosProperties properties;
        ResponseDiagnosticsProcessorImplementation(CosmosProperties properties){
            this.properties = properties;
        }

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            if(this.properties.isResponseDiagnosticsEnabled()){
                logger.info("Response Diagnostics {}", responseDiagnostics);
            }
        }
    }

    @Override
    protected String getDatabaseName() {
        return properties.getDatabase();
    }
}