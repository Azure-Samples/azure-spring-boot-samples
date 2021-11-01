package com.azure.sample.active.directory.resource.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnBehalfOfController {

    @Autowired
    private OAuth2AuthorizedClientManager authorizedClientManager;

    @GetMapping("/on-behalf-of/resource-server-3")
    public String onBehalfOfResourceServer3(JwtAuthenticationToken jwtAuthentication) {
        OAuth2AuthorizeRequest authorizeRequest =
            OAuth2AuthorizeRequest.withClientRegistrationId("resource-server-2-resource-server-3")
                                  .principal(jwtAuthentication)
                                  .build();
        OAuth2AuthorizedClient authorizedClient = this.authorizedClientManager.authorize(authorizeRequest);
        return "Hi, this is resource-server-2. You can see this response means you already consented the permissions "
            + "configured for client registration. "
            + "Scopes in authorizedClient: " + authorizedClient.getAccessToken().getScopes();
    }
}
