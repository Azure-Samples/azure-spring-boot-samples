// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.keyvault.jca;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManager;

@Configuration
public class WebClientConfiguration {

    private final SslBundles sslBundles;

    public WebClientConfiguration(SslBundles sslBundles) {
        this.sslBundles = sslBundles;
    }

    @Bean
    public WebClient webClientWithTLS() throws Exception {
        return buildWebClientEnableTls(false);
    }

    @Bean
    public WebClient webClientWithMTLS() throws Exception {
        return buildWebClientEnableTls(true);
    }

    private WebClient buildWebClientEnableTls(boolean enableMtls) throws Exception {
        SslBundle sslBundle = sslBundles.getBundle("keyVaultBundle");
        KeyManager keyManager = enableMtls ? sslBundle.getManagers().getKeyManagers()[0] : null;
        SslContext sslContext = SslContextBuilder
            .forClient()
            .keyManager(keyManager)
            .trustManager(sslBundle.getManagers().getTrustManagers()[0])
            .build();
        HttpClient httpClient = HttpClient.create()
                                          .secure(sslSpec -> sslSpec.sslContext(sslContext));
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder()
                        .clientConnector(connector)
                        .build();
    }

}
