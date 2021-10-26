package com.azure.sample.active.directory.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class ResourceServer1CheckPermissionByScopeController {

    private final WebClient webClient;

    public ResourceServer1CheckPermissionByScopeController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/check-permission/client/resource-server-1/scope/resource-server-1-scope-1")
    @PreAuthorize("hasAuthority('SCOPE_api://<resource-server-1-client-id>/resource-server-1.scope-1')")
    public String resourceServer1Scope1(@RegisteredOAuth2AuthorizedClient("client-1") OAuth2AuthorizedClient client1) {
        return webClient
            .get()
            .uri("http://localhost:8081/scope/resource-server-1-scope-1")
            .attributes(oauth2AuthorizedClient(client1))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    @GetMapping("/check-permission/client/resource-server-1/scope/resource-server-1-scope-2")
    @PreAuthorize("hasAuthority('SCOPE_api://<resource-server-1-client-id>/resource-server-1.scope-2')")
    public String resourceServer1Scope2(@RegisteredOAuth2AuthorizedClient("client-1") OAuth2AuthorizedClient client1) {
        return webClient
            .get()
            .uri("http://localhost:8081/scope/resource-server-1-scope-2")
            .attributes(oauth2AuthorizedClient(client1))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
}
