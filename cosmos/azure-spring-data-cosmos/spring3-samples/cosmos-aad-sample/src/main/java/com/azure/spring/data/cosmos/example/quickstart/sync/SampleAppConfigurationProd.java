// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.spring.data.cosmos.example.quickstart.sync;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.credential.TokenRequestContext;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.DirectConnectionConfig;
import com.azure.identity.ClientSecretCredentialBuilder;
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
import org.springframework.context.annotation.Profile;
import org.springframework.lang.Nullable;

import java.util.Arrays;

@Configuration
@EnableConfigurationProperties(CosmosProperties.class)
@EnableCosmosRepositories
@EnableReactiveCosmosRepositories
@Profile("prod")
public class SampleAppConfigurationProd extends AbstractCosmosConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SampleAppConfigurationProd.class);
    private CosmosProperties properties;

    public SampleAppConfigurationProd(CosmosProperties properties){
        this.properties = properties;
    }

    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        DirectConnectionConfig directConnectionConfig = DirectConnectionConfig.getDefaultConfig();
        TokenCredential servicePrincipal = new ClientSecretCredentialBuilder()
                .authorityHost("https://login.microsoftonline.com") //this line is not required (is redundant) if connecting to AAD
                .tenantId(properties.getTenantId())
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .build();

        return new CosmosClientBuilder()
                .endpoint(properties.getUri())
                .credential(servicePrincipal)
                .directMode(directConnectionConfig)
                .preferredRegions(Arrays.asList("East US 2"));

    }

    @Bean
    public CosmosConfig cosmosConfig() {
        return CosmosConfig.builder()
                .responseDiagnosticsProcessor(new ResponseDiagnosticsProcessorImplementation())
                .enableQueryMetrics(properties.isQueryMetricsEnabled())
                .build();
    }

    @Profile("dev")
    private void checkAADSetup(TokenCredential servicePrincipal) {
        TokenRequestContext context = new TokenRequestContext();
        context.addScopes(properties.getDefaultScope());
        AccessToken token = servicePrincipal.getToken(context).block();
        logger.info("Cosmos DB token successfully retrieved using AAD: " + token.getToken());
    }

    @Override
    protected String getDatabaseName() {
        return properties.getDatabaseName();
    }

    private static class ResponseDiagnosticsProcessorImplementation implements ResponseDiagnosticsProcessor {

        @Override
        public void processResponseDiagnostics(@Nullable ResponseDiagnostics responseDiagnostics) {
            logger.info("Response Diagnostics {}", responseDiagnostics);
        }
    }
}
