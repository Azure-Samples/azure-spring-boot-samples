package com.azure.sample.active.directory.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceServerAllController {

    @GetMapping("/client/resource-server-all/hello")
    public String resourceServer1(
        @RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1,
        @RegisteredOAuth2AuthorizedClient("client-1-resource-server-2") OAuth2AuthorizedClient client1ResourceServer2,
        @RegisteredOAuth2AuthorizedClient("client-1-resource-server-3") OAuth2AuthorizedClient client1ResourceServer3,
        @RegisteredOAuth2AuthorizedClient("client-1-resource-server-4") OAuth2AuthorizedClient client1ResourceServer4,
        @RegisteredOAuth2AuthorizedClient("client-1-resource-server-5") OAuth2AuthorizedClient client1ResourceServer5) {
        return "Hi, this is client 1. You can see this response means you already consented the permissions "
            + "configured for client registration. "
            + "Scopes in client1ResourceServer1: " + client1ResourceServer1.getAccessToken().getScopes()
            + "Scopes in client1ResourceServer2: " + client1ResourceServer2.getAccessToken().getScopes()
            + "Scopes in client1ResourceServer3: " + client1ResourceServer3.getAccessToken().getScopes()
            + "Scopes in client1ResourceServer4: " + client1ResourceServer4.getAccessToken().getScopes()
            + "Scopes in client1ResourceServer5: " + client1ResourceServer5.getAccessToken().getScopes();
    }
}
