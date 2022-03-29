package com.azure.spring.sample.active.directory.oauth2.servlet.sample03.client.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceServerAllController {

    @GetMapping("/resource-server-all")
    public String resourceServerAll(
        @RegisteredOAuth2AuthorizedClient("client-1-resource-server-1") OAuth2AuthorizedClient client1ResourceServer1,
        @RegisteredOAuth2AuthorizedClient("client-1-resource-server-2") OAuth2AuthorizedClient client1ResourceServer2) {
        return "Hi, this is client-1. You can see this response means you already consented the permissions "
            + "configured for client registration. "
            + "Scopes in client1ResourceServer1: " + client1ResourceServer1.getAccessToken().getScopes()
            + "Scopes in client1ResourceServer2: " + client1ResourceServer2.getAccessToken().getScopes();
    }
}
