// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.security.keyvault.certificates.sample.client.side;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@Profile("webclient")
public class WebClientController {

    private static final String SERVER_SIDE_ENDPOINT = "https://localhost:8443/";

    final WebClient webClientWithTLS;
    final WebClient webClientWithMTLS;

    public WebClientController(WebClient webClientWithTLS, WebClient webClientWithMTLS) {
        this.webClientWithTLS = webClientWithTLS;
        this.webClientWithMTLS = webClientWithMTLS;
    }

    @GetMapping("webclient/tls")
    public String webclientTls() {
        return String.format("Response from webclientTls \"%s\": %s",
                SERVER_SIDE_ENDPOINT,
                webClientWithTLS.get().uri(SERVER_SIDE_ENDPOINT).retrieve().bodyToMono(String.class).block());
    }

    @GetMapping("webclient/mtls")
    public String webclientMtls() {
        return String.format("Response from webclientMtls \"%s\": %s",
                SERVER_SIDE_ENDPOINT,
                webClientWithMTLS.get().uri(SERVER_SIDE_ENDPOINT).retrieve().bodyToMono(String.class).block()
        );
    }
}
