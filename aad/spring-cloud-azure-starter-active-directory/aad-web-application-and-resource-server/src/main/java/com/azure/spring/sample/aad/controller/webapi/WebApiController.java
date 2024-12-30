// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.sample.aad.controller.webapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

/**
 * Resource server exposes the APIs.
 */
@RestController
@RequestMapping("/api")
public class WebApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebApiController.class);

    private static final String WEB_API_B_URI = "http://localhost:8082/webapiB";
    private static final String WEB_API_B_CLIENT_CREDENTIAL_URI = "http://localhost:8082/webapiB/clientCredential";

    @Autowired
    private WebClient webClient;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/webapiC")
    @PreAuthorize("hasAuthority('SCOPE_WebApiC.SampleScope')")
    public String webapiC() {
        return "webapiC";
    }

    /**
     * Call custom resources, combine all the response and return.
     *
     * @param webapiBClient authorized client for Custom
     * @return Response webapiBWithObo data.
     */
    @PreAuthorize("hasAuthority('SCOPE_Obo.WebApiC.SampleScope')")
    @GetMapping("/webapiC/webapiB")
    public String callWebapiB(
        @RegisteredOAuth2AuthorizedClient("webapiBWithObo") OAuth2AuthorizedClient webapiBClient) {
        return canVisitUri(webapiBClient, WEB_API_B_URI);
    }

    /**
     * Call custom resources, combine all the response and return.
     *
     * @param webapiBClient authorized client for Custom
     * @return Response webapiBWithClientCredentials data.
     */
    @PreAuthorize("hasAuthority('APPROLE_ClientCredentials.WebApiC.SampleScope')")
    @GetMapping("/webapiC/webapiB/clientCredential")
    public String callWebapiBWithClientCredentials(
        @RegisteredOAuth2AuthorizedClient("webapiBWithClientCredentials") OAuth2AuthorizedClient webapiBClient) {
        return canVisitUri(webapiBClient, WEB_API_B_CLIENT_CREDENTIAL_URI);
    }

    /**
     * Check whether uri is accessible by provided client.
     *
     * @param client Authorized client.
     * @param uri The request uri.
     * @return "Get http response successfully." or "Get http response failed."
     */
    private String canVisitUri(OAuth2AuthorizedClient client, String uri) {
        if (null == client) {
            return "Get response failed.";
        }
        String body = webClient
            .get()
            .uri(uri)
            .attributes(oauth2AuthorizedClient(client))
            .retrieve()
            .bodyToMono(String.class)
            .block();
        LOGGER.info("Response from {} : {}", uri, body);
        return "Get response " + (null != body ? "successfully" : "failed");
    }
}
