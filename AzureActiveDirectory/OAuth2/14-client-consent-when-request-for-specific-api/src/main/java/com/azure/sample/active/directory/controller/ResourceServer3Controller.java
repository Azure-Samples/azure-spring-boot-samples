package com.azure.sample.active.directory.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceServer3Controller {

    @GetMapping("/client/resource-server-3/hello")
    public String resourceServer1(@RegisteredOAuth2AuthorizedClient("client-1-resource-server-3")
                                      OAuth2AuthorizedClient client1ResourceServer3) {
        return "Hi, this is client-1. You can see this response means you already consented the permissions "
            + "configured for client registration: client-1-resource-server-3. Here are the scopes in "
            + "OAuth2AuthorizedClient: " + client1ResourceServer3.getAccessToken().getScopes();
    }
}
