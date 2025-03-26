// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.keyvault.jca;

import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration(proxyBeanMethods = false)
class RestTemplateConfiguration {

    @Bean
    RestTemplate restTemplateWithTLS(RestTemplateBuilder restTemplateBuilder, SslBundles sslBundles) {
        return restTemplateBuilder.sslBundle(sslBundles.getBundle("tlsClientBundle")).build();
    }

    @Bean
    RestTemplate restTemplateWithMTLS(RestTemplateBuilder restTemplateBuilder, SslBundles sslBundles) {
        return restTemplateBuilder.sslBundle(sslBundles.getBundle("mtlsClientBundle")).build();
    }
}
