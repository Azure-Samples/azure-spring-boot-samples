// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction function =
            new ServletOAuth2AuthorizedClientExchangeFilterFunction(oAuth2AuthorizedClientManager);
        return WebClient.builder()
                        .apply(function.oauth2Configuration())
                        .build();
    }
}
