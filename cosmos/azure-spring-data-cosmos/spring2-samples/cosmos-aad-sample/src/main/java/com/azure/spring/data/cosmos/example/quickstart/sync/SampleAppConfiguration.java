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

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.lang.Nullable;

@Configuration
@EnableConfigurationProperties(CosmosProperties.class)
@EnableCosmosRepositories
@EnableReactiveCosmosRepositories
@Profile("dev")
public class SampleAppConfiguration extends AbstractCosmosConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SampleAppConfiguration.class);
    private CosmosProperties properties;

    public SampleAppConfiguration(CosmosProperties properties){
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

        //if this check fails, review error in logs and AAD setup as well as connectivity to AAD.
        //If setup is correct and there are no errors, you can change spring.profiles.active=prod 
        //in application.yaml so that prod version is used which does not contain this check.
        checkAADSetup(servicePrincipal);

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
