// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Controller
public class ApplicationController {

    @Autowired
    private WebClient webClient;

    @GetMapping("/application")
    @ResponseBody
    public String index() {
        return "Hello, this is application.";
    }

    @GetMapping("/application/resource-server")
    @ResponseBody
    public String graph(
        @RegisteredOAuth2AuthorizedClient("myself") OAuth2AuthorizedClient myselfClient
    ) {
        String body = webClient
            .get()
            .uri("http://localhost:8084/resource-server/hello")
            .attributes(oauth2AuthorizedClient(myselfClient))
            .retrieve()
            .bodyToMono(String.class)
            .block();
        return "Hello, this is application. I got response from 'http://localhost:8084/resource-server/hello': " +  body;
    }
}
