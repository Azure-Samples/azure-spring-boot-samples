// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.keyvault.secrets.sample.multiple.property.source;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.ProxyOptions;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.spring.cloud.autoconfigure.keyvault.environment.KeyVaultOperation;
import com.azure.spring.cloud.autoconfigure.keyvault.environment.KeyVaultPropertySource;
import org.apache.commons.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import java.net.InetSocketAddress;
import java.time.Duration;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME;

public class MyKeyVaultEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    public static final int ORDER = ConfigDataEnvironmentPostProcessor.ORDER + 1;

    private final Log logger;

    public MyKeyVaultEnvironmentPostProcessor(Log logger) {
        this.logger = logger;
    }


    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // configure the proxy for idenity clients here.
        TokenCredential tokenCredential = new ClientSecretCredentialBuilder()
            .clientId("")
            .clientSecret("")
            .proxyOptions(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("", 1234)))
            .build();
        SecretClient secretClient = new SecretClientBuilder()
            .vaultUrl("")
            .credential(tokenCredential)
            .buildClient();

        try {
            final MutablePropertySources sources = environment.getPropertySources();
            final KeyVaultOperation keyVaultOperation = new KeyVaultOperation(secretClient, Duration.ofMinutes(30), null, false);

            KeyVaultPropertySource keyVaultPropertySource = new KeyVaultPropertySource("my-kv-propertysource",
                keyVaultOperation);
            if (sources.contains(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)) {
                sources.addAfter(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, keyVaultPropertySource);
            } else {
                sources.addFirst(keyVaultPropertySource);
            }

        } catch (final Exception ex) {
            throw new IllegalStateException("Failed to configure KeyVault property source", ex);
        }

    }

    /**
     *
     * @return The order value.
     */
    @Override
    public int getOrder() {
        return ORDER;
    }

}
