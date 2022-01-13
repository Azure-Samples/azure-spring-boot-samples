package com.azure.spring.sample.activedirectory.reactive.webflux.oauth2.gateway.client.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class ResourceServer1Controller {

    private final WebClient webClient;

    public ResourceServer1Controller(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/resource-server-1")
    public Mono<String> hello(
        @RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1) {
        return webClient
            .get()
            .uri("http://localhost:8083/resource-server-1")
            .attributes(oauth2AuthorizedClient(client1ResourceServer1))
            .retrieve()
            .bodyToMono(String.class);
    }
}
