// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.keyvault.jca;

import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient webClientWithTLS(WebClientSsl ssl) throws Exception {
        return buildWebClientEnableTls(false, ssl);
    }

    @Bean
    public WebClient webClientWithMTLS(WebClientSsl ssl) throws Exception {
        return buildWebClientEnableTls(true, ssl);
    }

    private WebClient buildWebClientEnableTls(boolean enableMtls, WebClientSsl ssl) throws Exception {
        String sslBundleName = enableMtls ? "mtlsClientBundle" : "tlsClientBundle";
        return WebClient.builder().baseUrl("https://localhost:8444")
                .apply(ssl.fromBundle(sslBundleName))
                .build();
    }

}
