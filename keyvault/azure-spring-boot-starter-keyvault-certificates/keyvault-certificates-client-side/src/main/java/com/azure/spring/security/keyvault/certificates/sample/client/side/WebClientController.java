// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.spring.security.keyvault.certificates.sample.client.side;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static com.azure.spring.security.keyvault.certificates.sample.client.side.common.AzureKeyVaultKeyStoreUtil.SERVER_SIDE_ENDPOINT;

@RestController
public class WebClientController {

    final WebClient webClientWithTLS;
    final WebClient webClientWithMTLS;

    public WebClientController(WebClient webClientWithTLS, WebClient webClientWithMTLS) {
        this.webClientWithTLS = webClientWithTLS;
        this.webClientWithMTLS = webClientWithMTLS;
    }

    @GetMapping("webclient/tls")
    public String webclientTls() {
        return String.format("Response from webClient tls \"%s\": %s",
                SERVER_SIDE_ENDPOINT,
                webClientWithTLS.get().uri(SERVER_SIDE_ENDPOINT).retrieve().bodyToMono(String.class).block());
    }

    @GetMapping("webclient/mtls")
    public String webclientMtls() {
        return String.format("Response from webClient mtls \"%s\": %s",
                SERVER_SIDE_ENDPOINT,
                webClientWithMTLS.get().uri(SERVER_SIDE_ENDPOINT).retrieve().bodyToMono(String.class).block()
        );
    }
}
