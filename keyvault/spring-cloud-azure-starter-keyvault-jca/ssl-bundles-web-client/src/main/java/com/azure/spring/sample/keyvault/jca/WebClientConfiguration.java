// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.keyvault.jca;

import org.springframework.boot.webclient.autoconfigure.WebClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
class WebClientConfiguration {

    @Bean
    WebClient webClientWithTLS(WebClientSsl ssl) {
        return WebClient.builder().apply(ssl.fromBundle("tlsClientBundle")).build();
    }

    @Bean
    WebClient webClientWithMTLS(WebClientSsl ssl) {
        return WebClient.builder().apply(ssl.fromBundle("mtlsClientBundle")).build();
    }
}
